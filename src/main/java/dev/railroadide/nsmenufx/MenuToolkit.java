package dev.railroadide.nsmenufx;

import dev.railroadide.nsmenufx.annotation.Beta;
import dev.railroadide.nsmenufx.dialogs.about.AboutStageBuilder;
import dev.railroadide.nsmenufx.icns.IcnsParser;
import dev.railroadide.nsmenufx.icns.IcnsType;
import dev.railroadide.nsmenufx.labels.LabelMaker;
import dev.railroadide.nsmenufx.labels.LabelName;
import dev.railroadide.nsmenufx.listener.MenuBarSyncListener;
import dev.railroadide.nsmenufx.listener.WindowMenuUpdateListener;
import dev.railroadide.nsmenufx.model.AppearanceMode;
import dev.railroadide.nsmenufx.platform.NativeAdapter;
import dev.railroadide.nsmenufx.platform.NativeAdapterProvider;
import dev.railroadide.nsmenufx.util.MenuBarUtils;
import dev.railroadide.nsmenufx.util.StageUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class MenuToolkit {
    private static final String APP_NAME = "Apple";

    private final NativeAdapter nativeAdapter;

    private final LabelMaker labelMaker;

    private MenuToolkit(NativeAdapter nativeAdapter, LabelMaker labelMaker) {
        this.nativeAdapter = nativeAdapter;
        this.labelMaker = labelMaker;
    }

    public static MenuToolkit toolkit() {
        return toolkit(Locale.ENGLISH);
    }

    public static MenuToolkit toolkit(Locale locale) {
        return toolkit(new LabelMaker(locale));
    }

    public static MenuToolkit toolkit(LabelMaker labelMaker) {
        return new MenuToolkit(NativeAdapterProvider.getNativeAdapter(), labelMaker);
    }

    public Menu createDefaultApplicationMenu(String appName) {
        return createDefaultApplicationMenu(appName, createDefaultAboutStage(appName), null);
    }

    public Menu createDefaultApplicationMenu(String appName, EventHandler<ActionEvent> eventHandler) {
        return createDefaultApplicationMenu(appName, createDefaultAboutStage(appName), eventHandler);
    }

    public Menu createDefaultApplicationMenu(String appName, Stage aboutStage, EventHandler<ActionEvent> eventHandler) {
        return new Menu(APP_NAME, null, createAboutMenuItem(appName, aboutStage), new SeparatorMenuItem(), createSettingsMenuItem(eventHandler), new SeparatorMenuItem(), createHideMenuItem(appName), createHideOthersMenuItem(),
                createUnhideAllMenuItem(), new SeparatorMenuItem(), createQuitMenuItem(appName));
    }

    public MenuItem createAboutMenuItem(String appName) {
        return createAboutMenuItem(appName, createDefaultAboutStage(appName));
    }

    @Beta
    public MenuItem createNativeAboutMenuItem(String appName) {
        return createAboutMenuItem(appName, event -> nativeAdapter.showAboutWindow(appName));
    }

    public MenuItem createAboutMenuItem(String appName, EventHandler<ActionEvent> actionEventEventHandler) {
        MenuItem about = new MenuItem(labelMaker.getLabel(LabelName.ABOUT, appName));
        about.setOnAction(actionEventEventHandler);
        return about;
    }

    private Stage createDefaultAboutStage(String appName) {
        AboutStageBuilder stageBuilder = AboutStageBuilder.start(labelMaker.getLabel(LabelName.ABOUT, appName))
                .withAppName(appName).withCloseOnFocusLoss().withCopyright("Copyright © " + Calendar
                        .getInstance().get(Calendar.YEAR));

        try {
            IcnsParser parser = IcnsParser.forFile(AboutStageBuilder.DEFAULT_APP_ICON);
            stageBuilder = stageBuilder.withImage(new Image(parser.getIconStream(IcnsType.ic09)));
        } catch (IOException e) {
            // Too bad, cannot load dummy image
            throw new RuntimeException(e);
        }

        return stageBuilder.build();
    }

    public MenuItem createAboutMenuItem(String appName, Stage aboutStage) {
        return createAboutMenuItem(appName, event -> aboutStage.show());
    }

    public MenuItem createSettingsMenuItem(EventHandler<ActionEvent> eventHandler) {
        MenuItem settings = new MenuItem(labelMaker.getLabel(LabelName.SETTINGS));
        settings.setOnAction(eventHandler);
        settings.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
        return settings;
    }

    public MenuItem createQuitMenuItem(String appName) {
        MenuItem quit = new MenuItem(labelMaker.getLabel(LabelName.QUIT, appName));
        quit.setOnAction(event -> nativeAdapter.quit());
        quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN));
        return quit;
    }

    public MenuItem createUnhideAllMenuItem() {
        MenuItem unhideAll = new MenuItem(labelMaker.getLabel(LabelName.SHOW_ALL));
        unhideAll.setOnAction(event -> nativeAdapter.showAllWindows());
        return unhideAll;
    }

    public MenuItem createHideOthersMenuItem() {
        MenuItem hideOthers = new MenuItem(labelMaker.getLabel(LabelName.HIDE_OTHERS));
        hideOthers.setOnAction(event -> nativeAdapter.hideOtherApplications());
        hideOthers.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.META_DOWN, KeyCombination.ALT_DOWN));
        return hideOthers;
    }

    public MenuItem createHideMenuItem(String appName) {
        MenuItem hide = new MenuItem(labelMaker.getLabel(LabelName.HIDE, appName));
        hide.setOnAction(event -> nativeAdapter.hide());
        hide.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.META_DOWN));
        return hide;
    }

    public MenuItem createMinimizeMenuItem() {
        MenuItem menuItem = new MenuItem(labelMaker.getLabel(LabelName.MINIMIZE));
        menuItem.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.META_DOWN));
        menuItem.setOnAction(event -> StageUtils.minimizeFocusedStage());
        return menuItem;
    }

    public MenuItem createZoomMenuItem() {
        MenuItem menuItem = new MenuItem(labelMaker.getLabel(LabelName.ZOOM));
        menuItem.setOnAction(event -> StageUtils.zoomFocusedStage());
        return menuItem;
    }

    public MenuItem createCloseWindowMenuItem() {
        MenuItem menuItem = new MenuItem(labelMaker.getLabel(LabelName.CLOSE_WINDOW));
        menuItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.META_DOWN));
        menuItem.setOnAction(event -> StageUtils.closeCurrentStage());
        return menuItem;
    }

    public MenuItem createBringAllToFrontItem() {
        MenuItem menuItem = new MenuItem(labelMaker.getLabel(LabelName.BRING_ALL_TO_FRONT));
        menuItem.setOnAction(event -> StageUtils.bringAllToFront());
        return menuItem;
    }

    public MenuItem createCycleWindowsItem() {
        MenuItem menuItem = new MenuItem(labelMaker.getLabel(LabelName.CYCLE_THROUGH_WINDOWS));
        menuItem.setAccelerator(new KeyCodeCombination(KeyCode.BACK_QUOTE, KeyCombination.META_DOWN));
        menuItem.setOnAction(event -> StageUtils.focusNextStage());
        return menuItem;
    }

    public void setApplicationMenu(Menu menu) {
        nativeAdapter.setApplicationMenu(menu);
    }

    public void setDockIconMenu(Menu menu) {
        nativeAdapter.setDockIconMenu(menu);
    }

    public void setTrayMenu(Menu menu) {
        nativeAdapter.setTrayMenu(menu);
    }

    public void showContextMenu(Menu menu, MouseEvent event) {
        nativeAdapter.showContextMenu(menu, event);
    }

    @SuppressWarnings("unused")
    public boolean systemUsesDarkMode() {
        return nativeAdapter.systemUsesDarkMode();
    }

    public void setAppearanceMode(AppearanceMode mode) {
        nativeAdapter.setAppearanceMode(mode);
    }

    public void setGlobalMenuBar(MenuBar menuBar) {
        setMenuBar(menuBar);
        MenuBarSyncListener.register(menuBar);
    }

    @SuppressWarnings("unused")
    public void unsetGlobalMenuBar() {
        MenuBarSyncListener.unregister();
    }

    public void setMenuBar(MenuBar menuBar) {
        if (Window.getWindows().isEmpty()) {
            nativeAdapter.setMenuBar(menuBar.getMenus());
        } else {
            setApplicationMenu(extractApplicationMenu(menuBar));

            Window.getWindows().stream()
                    .filter(Stage.class::isInstance)
                    .map(Stage.class::cast)
                    .forEach(stage -> MenuBarUtils.setMenuBar(stage, menuBar));
        }
    }

    public void setMenuBar(Stage stage, MenuBar menuBar) {
        Parent parent = stage.getScene().getRoot();
        if (parent instanceof Pane) {
            setMenuBar((Pane) parent, menuBar);
        }
    }

    public void setMenuBar(Pane pane, MenuBar menuBar) {
        setApplicationMenu(extractApplicationMenu(menuBar));
        MenuBarUtils.setMenuBar(pane, menuBar);
    }

    public void autoAddWindowMenuItems(Menu menu) {
        menu.getItems().add(new SeparatorMenuItem());
        StageUtils.getStages().addListener(new WindowMenuUpdateListener(menu));
    }

    @SuppressWarnings("unused")
    public void setForceQuitOnCmdQ(boolean forceQuit) {
        nativeAdapter.setForceQuitOnCmdQ(forceQuit);
    }

    protected Menu extractApplicationMenu(MenuBar menuBar) {
        return menuBar.getMenus().get(0);
    }
}
