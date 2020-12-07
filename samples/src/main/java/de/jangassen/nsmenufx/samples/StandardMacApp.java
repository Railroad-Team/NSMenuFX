package de.jangassen.nsmenufx.samples;

import de.jangassen.MenuToolkit;
import de.jangassen.model.AppearanceMode;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

/**
 * Skeleton for Standard Macintosh Application See:
 * "OS X Human Interface Guidelines"
 */
public class StandardMacApp extends Application {
  private static final String APP_NAME = "My macOS App";
  private static final String MAIN_WINDOW_TITLE = "Main";
  private static final String CHILD_WINDOW_TITLE = "Child";

  private final URL menuIcon = Objects.requireNonNull(StandardMacApp.class.getClassLoader().getResource("baseline_menu_white_18dp.png"));
  private final URL printIcon = Objects.requireNonNull(StandardMacApp.class.getClassLoader().getResource("baseline_print_white_18dp.png"));
  private final URL refreshIcon = Objects.requireNonNull(StandardMacApp.class.getClassLoader().getResource("baseline_refresh_white_18dp.png"));
  private final URL openIcon = Objects.requireNonNull(StandardMacApp.class.getClassLoader().getResource("baseline_open_in_new_white_18dp.png"));

  private static long counter = 1;

  @Override
  public void start(Stage primaryStage) {
    MenuToolkit tk = MenuToolkit.toolkit();

    Scene scene = new Scene(getRootPane(), 300, 200);

    Menu context = createSampleMenu();
    scene.setOnMouseClicked(event ->
    {
      if (event.getButton() == MouseButton.SECONDARY) {
        tk.showContextMenu(context, event);
      }
    });

    primaryStage.setScene(scene);
    primaryStage.requestFocus();
    primaryStage.setTitle(MAIN_WINDOW_TITLE);
    primaryStage.show();

    MenuBar bar = new MenuBar();

    // Application Menu
    Menu appMenu = new Menu(APP_NAME); // Name for appMenu can't be set at
    // Runtime
    MenuItem aboutItem = tk.createNativeAboutMenuItem(APP_NAME);
    MenuItem prefsItem = new MenuItem("Preferences...");
    prefsItem.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
    prefsItem.setOnAction(this::handleEvent);
    appMenu.getItems().addAll(aboutItem, new SeparatorMenuItem(), prefsItem, new SeparatorMenuItem(),
            tk.createHideMenuItem(APP_NAME), tk.createHideOthersMenuItem(), tk.createUnhideAllMenuItem(),
            new SeparatorMenuItem(), tk.createQuitMenuItem(APP_NAME));

    // File Menu
    Menu fileMenu = new Menu("File");
    MenuItem newItem = new MenuItem("New...");
    fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), tk.createCloseWindowMenuItem(),
            new SeparatorMenuItem(), new MenuItem("TBD"));

    // Edit
    Menu editMenu = new Menu("Edit");
    editMenu.getItems().addAll(createMenuItem("Undo"), createMenuItem("Redo"), new SeparatorMenuItem(),
            createMenuItem("Cut"), createMenuItem("Copy"), createMenuItem("Paste"), createMenuItem("Select All"));

    // Format
    Menu formatMenu = new Menu("Format");
    MenuItem menuItem = new MenuItem("TBD");
    formatMenu.getItems().addAll(menuItem);

    // View Menu
    Menu viewMenu = new Menu("View");
    viewMenu.getItems().addAll(new MenuItem("TBD"));

    // Window Menu
    Menu windowMenu = new Menu("Window");
    windowMenu.getItems().addAll(tk.createMinimizeMenuItem(), tk.createZoomMenuItem(), tk.createCycleWindowsItem(),
            new SeparatorMenuItem(), tk.createBringAllToFrontItem());

    // Help Menu
    Menu helpMenu = new Menu("Help");
    helpMenu.getItems().addAll(new MenuItem("Getting started"));

    bar.getMenus().addAll(appMenu, fileMenu, editMenu, formatMenu, viewMenu, windowMenu, helpMenu);

    tk.setAppearanceMode(AppearanceMode.AUTO);
    tk.setDockIconMenu(createDockMenu());
    tk.autoAddWindowMenuItems(windowMenu);
    tk.setGlobalMenuBar(bar);
    tk.setTrayMenu(createSampleMenu());
  }

  private MenuItem createMenuItem(String title) {
    MenuItem menuItem = new MenuItem(title);
    menuItem.setOnAction(this::handleEvent);
    return menuItem;
  }

  private StackPane getRootPane() {
    StackPane root = new StackPane();
    Button button = new Button();
    button.setText("Create new Stage");
    button.setOnAction(action -> createNewStage());
    root.getChildren().add(button);
    return root;
  }

  private Menu createDockMenu() {
    Menu dockMenu = createSampleMenu();
    MenuItem open = new MenuItem("New Window");
    open.setGraphic(new ImageView(new Image(openIcon.toString())));
    open.setOnAction(e -> createNewStage());
    dockMenu.getItems().addAll(new SeparatorMenuItem(), open);
    return dockMenu;
  }

  private Menu createSampleMenu() {
    Menu trayMenu = new Menu();
    trayMenu.setGraphic(new ImageView(new Image(menuIcon.toString())));
    MenuItem reload = new MenuItem("Reload");
    reload.setGraphic(new ImageView(new Image(refreshIcon.toString())));
    reload.setOnAction(this::handleEvent);
    MenuItem print = new MenuItem("Print");
    print.setGraphic(new ImageView(new Image(printIcon.toString())));
    print.setOnAction(this::handleEvent);

    Menu share = new Menu("Share");
    MenuItem mail = new MenuItem("Mail");
    mail.setOnAction(this::handleEvent);
    share.getItems().add(mail);

    trayMenu.getItems().addAll(reload, print, new SeparatorMenuItem(), share);
    return trayMenu;
  }

  private void handleEvent(ActionEvent actionEvent) {
    System.out.println("clicked " + actionEvent.getSource());  // NOSONAR
  }

  private static void createNewStage() {
    Stage stage = new Stage();
    stage.setScene(new Scene(new StackPane()));
    stage.setTitle(CHILD_WINDOW_TITLE + " " + (counter++));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
