package de.jangassen.listener;

import de.jangassen.util.MenuBarUtils;
import de.jangassen.util.StageUtils;
import javafx.collections.ListChangeListener;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class MenuBarSyncListener implements ListChangeListener<Stage> {

	private static MenuBar menuBar;
	private static MenuBarSyncListener instance = null;

	public static void register(MenuBar menuBar) {
		MenuBarSyncListener.menuBar = menuBar;

		if (instance == null) {
			instance = new MenuBarSyncListener();
			StageUtils.getStages().addListener(instance);
		}
	}

	public static void unregister() {
		if (instance != null) {
			StageUtils.getStages().removeListener(instance);
			instance = null;
		}
	}

	private MenuBarSyncListener() {
	}

	@Override
	public void onChanged(ListChangeListener.Change<? extends Stage> stageChanges) {
		while (stageChanges.next()) {
			stageChanges.getAddedSubList().forEach(stage -> MenuBarUtils.setMenuBar(stage, menuBar));
		}
	}

}
