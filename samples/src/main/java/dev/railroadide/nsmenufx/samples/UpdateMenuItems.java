package dev.railroadide.nsmenufx.samples;

import de.jangassen.MenuToolkit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UpdateMenuItems extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.requestFocus();
        primaryStage.show();

        // Get the toolkit
        MenuToolkit tk = MenuToolkit.toolkit();
        MenuBar menuBar = new MenuBar();

        Menu defaultApplicationMenu = tk.createDefaultApplicationMenu("test");

        MenuItem addMenu = new MenuItem("Add menu");
        addMenu.setOnAction(event -> menuBar.getMenus().add(new Menu("Added " + menuBar.getMenus().size())));

        MenuItem addItem = new MenuItem("Add menu item");
        addItem.setOnAction(actionEvent -> defaultApplicationMenu.getItems().add(4, new MenuItem("Added")));

        // Create the default Application menu
        defaultApplicationMenu.getItems().add(2, new SeparatorMenuItem());
        defaultApplicationMenu.getItems().add(2, addItem);
        defaultApplicationMenu.getItems().add(2, addMenu);
        defaultApplicationMenu.getItems().add(2, new SeparatorMenuItem());

        // Update the existing Application menu
        menuBar.getMenus().add(defaultApplicationMenu);
        tk.setMenuBar(menuBar);

        // Since we now have a reference to the menu, we can rename items
        defaultApplicationMenu.getItems().get(1).setText("Hide all the otters");
    }

}
