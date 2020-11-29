package de.jangassen.platform.mac;

import de.jangassen.exception.LifecycleException;
import de.jangassen.jfa.JavaToObjc;
import de.jangassen.jfa.ObjcToJava;
import de.jangassen.jfa.appkit.*;
import de.jangassen.jfa.cleanup.NSCleaner;
import de.jangassen.jfa.foundation.Foundation;
import de.jangassen.jfa.foundation.ID;
import de.jangassen.listener.FirstWindowShowingEventListener;
import de.jangassen.model.AppearanceMode;
import de.jangassen.platform.NativeAdapter;
import de.jangassen.platform.mac.convert.ImageConverter;
import de.jangassen.platform.mac.convert.MenuConverter;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class MacNativeAdapter implements NativeAdapter {

  private static MacNativeAdapter instance;

  private final NSApplication sharedApplication;
  private final NSWorkspace sharedWorkspace;

  private boolean forceQuitOnCmdQ = true;

  // Keep references to prevent GC
  private Menu applicationMenu;
  private Menu trayMenu;
  private Menu dockIconMenu;
  private ApplicationDelegateWithMenu applicationDelegate;

  private NSStatusItem nsStatusItem;
  private FirstWindowShowingEventListener firstWindowShowingEventListener = null;

  private MacNativeAdapter() {
    sharedApplication = NSApplication.sharedApplication();
    sharedWorkspace = NSWorkspace.sharedWorkspace();
  }

  public static MacNativeAdapter getInstance() {
    if (instance == null) {
      instance = new MacNativeAdapter();
    }

    return instance;
  }

  public static boolean isAvailable() {
    return Foundation.isAvailable();
  }

  public void setApplicationMenu(Menu menu) {
    NSMenu nsMenu = sharedApplication.mainMenu();
    if (nsMenu == null || nsMenu.numberOfItems() == 0) {
      setApplicationMenuWhenAvailable(menu);
    } else {
      setApplicationMenuImmediately(menu, nsMenu);
    }
  }

  private void setApplicationMenuImmediately(Menu menu, NSMenu nsMenu) {
    NSMenuItem mainMenu = NSMenuItem.alloc().initWithTitle("", null, "");
    mainMenu.setSubmenu(MenuConverter.convert(menu));

    nsMenu.removeItemAtIndex(0);
    nsMenu.insertItem(mainMenu, 0);

    sharedApplication.setAppearance(NSAppearance.appearanceNamed(NSAppearance.NSAppearanceName.NSAppearanceNameVibrantDark));
    applicationMenu = menu;
  }

  private void setApplicationMenuWhenAvailable(Menu menu) {
    if (firstWindowShowingEventListener == null) {
      firstWindowShowingEventListener = new FirstWindowShowingEventListener();
    }

    if (!firstWindowShowingEventListener.isCompleted()) {
      firstWindowShowingEventListener.setAction(() -> setApplicationMenu(menu));
    } else {
      throw new LifecycleException("Application menu is not initialized");
    }
  }

  public void hide() {
    sharedApplication.hide(ID.NIL);
  }

  public void hideOtherApplications() {
    sharedWorkspace.hideOtherApplications();
  }

  public void showAllWindows() {
    sharedApplication.unhide(ID.NIL);
  }

  public void quit() {
    if (forceQuitOnCmdQ) {
      Platform.exit();
    }
  }

  public void setForceQuitOnCmdQ(boolean forceQuit) {
    this.forceQuitOnCmdQ = forceQuit;
  }

  @Override
  public void setTrayMenu(Menu menu) {
    NSStatusBar nsStatusBar = NSStatusBar.systemStatusBar();
    if (menu != null) {
      if (nsStatusItem == null) {
        nsStatusItem = createNewTrayMenu(nsStatusBar);
      }

      ImageConverter.convert(menu.getGraphic()).ifPresent(nsStatusItem.button()::setImage);
      NSMenu convertedMenu = MenuConverter.convert(menu);

      nsStatusItem.setMenu(convertedMenu);
      trayMenu = menu;
    } else if (nsStatusItem != null) {
      removeTrayMenu(nsStatusBar);
    }
  }

  private NSStatusItem createNewTrayMenu(NSStatusBar nsStatusBar) {
    NSStatusItem statusItem = nsStatusBar.statusItemWithLength(NSStatusBar.NSSquareStatusItemLength);
    ID statusItemId = ObjcToJava.toID(statusItem);
    Foundation.cfRetain(statusItemId);
    NSCleaner.register(statusItem, statusItemId);
    return statusItem;
  }

  private void removeTrayMenu(NSStatusBar nsStatusBar) {
    nsStatusBar.removeStatusItem(nsStatusItem);
    Foundation.cfRelease(ObjcToJava.toID(nsStatusBar));
    trayMenu = null;
  }

  @Override
  public void showContextMenu(Menu menu, MouseEvent event) {
    Optional.ofNullable(sharedApplication.keyWindow()).map(NSWindow::contentView).ifPresent(view -> {
      NSMenu nsMenu = MenuConverter.convert(menu);
      NSPoint nsPoint = new NSPoint();
      nsPoint.x = new Foundation.CGFloat(event.getSceneX());
      nsPoint.y = new Foundation.CGFloat(event.getSceneY());
      nsMenu.popUpMenuPositioningItem(null, nsPoint, view);
    });
  }

  public void setDocIconMenu(Menu menu) {
    if (applicationDelegate == null) {
      NSApplicationDelegate delegate = sharedApplication.delegate();
      applicationDelegate = new ApplicationDelegateWithMenu(delegate);

      ID mappedObject = JavaToObjc.map(applicationDelegate);
      sharedApplication.setDelegate(ObjcToJava.map(mappedObject, NSApplicationDelegate.class));
    }

    NSMenu nsMenu = MenuConverter.convert(menu);
    applicationDelegate.setMenu(nsMenu);
    dockIconMenu = menu;
  }

  @Override
  public boolean systemUsesDarkMode() {
    return "Dark".equals(NSUserDefaults.standardUserDefaults().objectForKey(NSUserDefaults.AppleInterfaceStyle));
  }

  @Override
  public void setAppearanceMode(AppearanceMode mode) {
    switch (mode) {
      case AUTO:
        NSAppearance.NSAppearanceName appearanceName = systemUsesDarkMode()
                ? NSAppearance.NSAppearanceName.NSAppearanceNameVibrantDark
                : NSAppearance.NSAppearanceName.NSAppearanceNameVibrantLight;
        sharedApplication.setAppearance(NSAppearance.appearanceNamed(appearanceName));
        break;
      case DARK:
        sharedApplication.setAppearance(NSAppearance.appearanceNamed(NSAppearance.NSAppearanceName.NSAppearanceNameVibrantDark));
        break;
      case LIGHT:
        sharedApplication.setAppearance(NSAppearance.appearanceNamed(NSAppearance.NSAppearanceName.NSAppearanceNameVibrantLight));
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
