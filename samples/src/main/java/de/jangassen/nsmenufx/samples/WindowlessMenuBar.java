package de.jangassen.nsmenufx.samples;

import de.jangassen.MenuToolkit;
import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class WindowlessMenuBar extends Application {

  @Override
  public void start(Stage primaryStage) {
    MenuToolkit tk = MenuToolkit.toolkit();

    Menu applicationMenu = tk.createDefaultApplicationMenu("MyApp");

    MenuItem item1 = new MenuItem("Show window");
    item1.setOnAction(e -> primaryStage.show());
    MenuItem item2 = new MenuItem("Item2");
    MenuItem item3 = new MenuItem("Item3");
    item3.setOnAction(event -> System.out.println("Item3 clicked"));

    Menu submenu = new Menu("Submenu");
    submenu.getItems().add(item2);
    Menu menu1 = new Menu("Menu1");
    menu1.getItems().addAll(item1, submenu);

    Menu menu2 = new Menu("Menu2");
    menu2.getItems().addAll(item3);

    MenuBar bar = new MenuBar();
    bar.getMenus().addAll(applicationMenu, menu1, menu2);
    tk.setMenuBar(bar);
  }
}
