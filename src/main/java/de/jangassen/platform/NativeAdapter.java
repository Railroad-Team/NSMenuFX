package de.jangassen.platform;

import de.jangassen.model.AppearanceMode;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;

public interface NativeAdapter {
  void setApplicationMenu(Menu menu);

  void setDocIconMenu(Menu menu);

  void hide();

  void hideOtherApplications();

  void showAllWindows();

  void quit();

  void setForceQuitOnCmdQ(boolean forceQuit);

  void showContextMenu(Menu menu, MouseEvent event);

  void setTrayMenu(Menu menu);

  boolean systemUsesDarkMode();

  void setAppearanceMode(AppearanceMode mode);
}
