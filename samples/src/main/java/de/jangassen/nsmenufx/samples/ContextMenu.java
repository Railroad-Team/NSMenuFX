package de.jangassen.nsmenufx.samples;

import de.jangassen.MenuToolkit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ContextMenu extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    MenuToolkit toolkit = MenuToolkit.toolkit();
    Menu context = new Menu();
    MenuItem menuItem = new MenuItem("Item1");
    menuItem.setOnAction(e -> System.out.println("Clicked"));

    Menu subMenu = new Menu("Submenu");
    subMenu.getItems().add(new MenuItem("Item 2"));
    context.getItems().addAll(menuItem, subMenu);

    stage.setTitle("Right click me");

    BorderPane pane = new BorderPane();
    pane.setOnMouseClicked(event ->
    {
      if (event.getButton() == MouseButton.SECONDARY) {
        toolkit.showContextMenu(context, event);
      }
    });
    stage.setScene(new Scene(pane, 200, 200));
    stage.show();
  }
}
