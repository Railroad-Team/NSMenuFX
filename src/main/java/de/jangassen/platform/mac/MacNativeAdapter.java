package de.jangassen.platform.mac;

import de.jangassen.jfa.JavaToObjc;
import de.jangassen.jfa.ObjcToJava;
import de.jangassen.jfa.appkit.*;
import de.jangassen.jfa.cleanup.NSCleaner;
import de.jangassen.jfa.foundation.Foundation;
import de.jangassen.jfa.foundation.ID;
import de.jangassen.listener.WindowShowingEventListener;
import de.jangassen.model.AppearanceMode;
import de.jangassen.platform.NativeAdapter;
import de.jangassen.platform.mac.convert.ImageConverter;
import de.jangassen.platform.mac.convert.MenuConverter;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MacNativeAdapter implements NativeAdapter {

  public static final String DARK = "Dark";
  private static MacNativeAdapter instance;

  private boolean forceQuitOnCmdQ = true;

  private final NSApplication sharedApplication;
  private final NSWorkspace sharedWorkspace;

  private NSStatusItem nsStatusItem;
  private ApplicationDelegateWithMenu applicationDelegate;
  private Menu applicationMenu;

  private MacNativeAdapter() {
    sharedApplication = NSApplication.sharedApplication();
    sharedWorkspace = NSWorkspace.sharedWorkspace();

    Window.getWindows().forEach(window -> window.showingProperty().addListener((observableValue, oldValue, newValue) -> {
      if (Boolean.TRUE.equals(newValue)) {
        this.handleWindowShowing();
      }
    }));
    Window.getWindows().addListener(new WindowShowingEventListener(this::handleWindowShowing));
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

  public void setMenuBar(List<Menu> menus) {
    NSMenu menu = NSMenu.alloc().init();
    menus.stream().map(this::getMenuBarItem).forEach(menu::addItem);

    sharedApplication.setMainMenu(menu);
    if (!menus.isEmpty()) {
      applicationMenu = menus.get(0);
    }
  }

  private NSMenuItem getMenuBarItem(Menu menu) {
    NSMenu nsMenu = MenuConverter.convert(menu);
    NSMenuItem wrapperItem = NSMenuItem.alloc().init();
    wrapperItem.setSubmenu(nsMenu);

    NSCleaner.register(menu, wrapperItem);
    return wrapperItem;
  }

  private void handleWindowShowing() {
    if (applicationMenu != null) {
      setApplicationMenu(applicationMenu);
    }
  }

  public void setApplicationMenu(Menu menu) {
    NSMenu nsMenu = sharedApplication.mainMenu();
    if (nsMenu == null) {
      setMenuBar(Collections.singletonList(menu));
    } else {
      replaceApplicationMenu(menu, nsMenu);
    }
  }

  private void replaceApplicationMenu(Menu menu, NSMenu nsMenu) {
    NSMenuItem mainMenu = getMenuBarItem(menu);

    nsMenu.removeItemAtIndex(0);
    nsMenu.insertItem(mainMenu, 0);

    applicationMenu = menu;
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
        nsStatusItem = nsStatusBar.statusItemWithLength(NSStatusBar.NSSquareStatusItemLength);
        Foundation.cfRetain(ObjcToJava.toID(nsStatusItem));
      }

      ImageConverter.convert(menu.getGraphic()).ifPresent(nsStatusItem.button()::setImage);
      NSMenu convertedMenu = MenuConverter.convert(menu);

      nsStatusItem.setMenu(convertedMenu);
    } else if (nsStatusItem != null) {
      nsStatusBar.removeStatusItem(nsStatusItem);
      Foundation.cfRetain(ObjcToJava.toID(nsStatusItem));
    }
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
      applicationDelegate = new ApplicationDelegateWithMenu(sharedApplication.delegate());

      ID mappedObject = JavaToObjc.map(applicationDelegate);
      sharedApplication.setDelegate(ObjcToJava.map(mappedObject, NSApplicationDelegate.class));
    }

    NSMenu nsMenu = MenuConverter.convert(menu);
    applicationDelegate.setMenu(nsMenu);
  }

  @Override
  public boolean systemUsesDarkMode() {
    return DARK.equals(NSUserDefaults.standardUserDefaults().objectForKey(NSUserDefaults.AppleInterfaceStyle));
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
