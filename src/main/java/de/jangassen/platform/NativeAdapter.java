package de.jangassen.platform;

import de.jangassen.model.AppearanceMode;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;

import java.util.List;

public interface NativeAdapter {
  void setMenuBar(List<Menu> menus);

  void setApplicationMenu(Menu menu);

  void setDockIconMenu(Menu menu);

  void hide();

  void hideOtherApplications();

  void showAllWindows();

  void quit();

  void setForceQuitOnCmdQ(boolean forceQuit);

  void showAboutWindow(String title);

  void showContextMenu(Menu menu, MouseEvent event);

  void setTrayMenu(Menu menu);

  boolean systemUsesDarkMode();

  void setAppearanceMode(AppearanceMode mode);
}
