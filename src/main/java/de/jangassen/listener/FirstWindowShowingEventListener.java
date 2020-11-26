package de.jangassen.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.stage.Window;

import java.util.List;
import java.util.Optional;

public class FirstWindowShowingEventListener implements ListChangeListener<Window> {
  private Runnable action;
  private boolean completed = false;

  public FirstWindowShowingEventListener() {
    watchWindows(Window.getWindows());
    Window.getWindows().addListener(this);
  }

  public void setAction(Runnable action) {
    this.action = action;
  }

  public boolean isCompleted() {
    return completed;
  }

  @Override
  public void onChanged(Change<? extends Window> change) {
    while (change.next()) {
      watchWindows(change.getAddedSubList());
    }
  }

  private void watchWindows(List<? extends Window> windows) {
    windows.forEach(added -> added.showingProperty().addListener(new ChangeListener<>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        Optional.ofNullable(action).ifPresent(Runnable::run);
        completed = true;

        added.showingProperty().removeListener(this);
        Window.getWindows().removeListener(FirstWindowShowingEventListener.this);
      }
    }));
  }
}
