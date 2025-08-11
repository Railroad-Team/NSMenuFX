package dev.yodaforce.listener;

import javafx.collections.ListChangeListener;
import javafx.stage.Window;

import java.util.List;

public class WindowShowingEventListener implements ListChangeListener<Window> {
  private final Runnable action;

  public WindowShowingEventListener(Runnable action) {
    this.action = action;
  }

  @Override
  public void onChanged(Change<? extends Window> change) {
    while (change.next()) {
      watchWindows(change.getAddedSubList());
    }
  }

  private void watchWindows(List<? extends Window> windows) {
    windows.forEach(this::handleWindowAdded);
  }

  private void handleWindowAdded(Window added) {
    if (added.isShowing()) {
      action.run();
    } else {
      added.showingProperty().addListener((observableValue, oldValue, newValue) -> {
        if (Boolean.TRUE.equals(newValue)) {
          action.run();
        }
      });
    }
  }
}
