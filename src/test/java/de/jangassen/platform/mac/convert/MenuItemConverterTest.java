package de.jangassen.platform.mac.convert;

import de.jangassen.jfa.appkit.NSMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class MenuItemConverterTest {

  @Test
  void testUnnamedItem() {
    MenuItem item = new MenuItem();

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    Assertions.assertEquals("", nsMenuItem.title());
  }

  @Test
  void testItemWithTitle() {
    MenuItem item = new MenuItem("test");

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    Assertions.assertEquals("test", nsMenuItem.title());
  }

  @Test
  void testItemWithAccelerator() {
    MenuItem item = new MenuItem("test");
    item.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN));

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    Assertions.assertEquals("alt+q", nsMenuItem.keyEquivalent());
  }

  @Test
  void testItemWithAction() {
    AtomicBoolean wasHandled = new AtomicBoolean(false);

    MenuItem item = new MenuItem("test");
    item.setOnAction(actionEvent -> wasHandled.set(true));

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    item.fire();
    Assertions.assertTrue(wasHandled.get());
    Assertions.assertNotNull(nsMenuItem); // Prevent it from being GC'ed to early
  }

  @Test
  void testItemUpdateTitle() {
    MenuItem item = new MenuItem();

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    item.setText("test");
    Assertions.assertEquals("test", nsMenuItem.title());
  }

  @Test
  void testItemUpdateAction() {
    AtomicBoolean wasHandled = new AtomicBoolean(false);

    MenuItem item = new MenuItem("test");
    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    item.setOnAction(actionEvent -> wasHandled.set(true));
    item.fire();
    Assertions.assertTrue(wasHandled.get());
    Assertions.assertNotNull(nsMenuItem); // Prevent it from being GC'ed to early
  }

  @Test
  void testItemUpdateAccelerator() {
    MenuItem item = new MenuItem("test");

    NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

    item.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN));
    Assertions.assertEquals("alt+q", nsMenuItem.keyEquivalent());
  }

}