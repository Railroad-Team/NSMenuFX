package dev.yodaforce.platform;

import dev.yodaforce.dialogs.about.AboutStageBuilder;
import dev.yodaforce.model.AppearanceMode;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

public class DummyNativeAdapter implements NativeAdapter {

  @Override
  public void setApplicationMenu(Menu menu) {
    // Only supported on macOS
  }

  @Override
  public void hide() {
    Window.getWindows().stream()
            .filter(Window::isFocused)
            .findFirst()
            .ifPresent(Window::hide);
  }

  @Override
  public void hideOtherApplications() {
    // Only supported on macOS
  }

  @Override
  public void showAllWindows() {
    Window.getWindows().stream()
            .filter(Stage.class::isInstance)
            .map(Stage.class::cast)
            .forEach(Stage::show);
  }

  @Override
  public void setDockIconMenu(Menu menu) {
    // Only supported on macOS
  }

  @Override
  public void quit() {
    Platform.exit();
  }

  @Override
  public void setForceQuitOnCmdQ(boolean forceQuit) {
    // Only supported on macOS
  }

  @Override
  public void setTrayMenu(Menu menu) {
    // Only supported on macOS
  }

  @Override
  public boolean systemUsesDarkMode() {
    return false;
  }

  @Override
  public void setMenuBar(List<Menu> menus) {
    // Only supported on macOS
  }

  @Override
  public void setAppearanceMode(AppearanceMode mode) {
    // Only supported on macOS
  }

  public void showContextMenu(Menu menu, MouseEvent event) {
    Window.getWindows().stream().filter(Window::isFocused).findFirst().ifPresent(window -> {
      ContextMenu contextMenu = new ContextMenu();
      contextMenu.getItems().addAll(menu.getItems());
      contextMenu.show(window, event.getScreenX(), event.getScreenY());
    });
  }

  @Override
  public void showAboutWindow(String title) {
    AboutStageBuilder.start(title).build().show();
  }
}
