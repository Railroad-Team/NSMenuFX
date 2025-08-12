package dev.railroadide.nsmenufx.platform.mac.convert;

import de.jangassen.jfa.appkit.NSEventModifierFlags;
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
        var item = new MenuItem();
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

        Assertions.assertEquals("", nsMenuItem.title());
    }

    @Test
    void testItemWithTitle() {
        var item = new MenuItem("test");
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

        Assertions.assertEquals("test", nsMenuItem.title());
    }

    @Test
    void testItemWithAccelerator() {
        var item = new MenuItem("test");
        item.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN));
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);

        Assertions.assertEquals("q", nsMenuItem.keyEquivalent());
        Assertions.assertEquals(NSEventModifierFlags.NSEventModifierFlagOption, nsMenuItem.keyEquivalentModifierMask());
    }

    @Test
    void testItemWithAction() {
        var wasHandled = new AtomicBoolean(false);

        var item = new MenuItem("test");
        item.setOnAction(actionEvent -> wasHandled.set(true));
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);
        item.fire();

        Assertions.assertTrue(wasHandled.get());
        Assertions.assertNotNull(nsMenuItem); // Prevent it from being GC'ed too early
    }

    @Test
    void testItemUpdateTitle() {
        var item = new MenuItem();
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);
        item.setText("test");

        Assertions.assertEquals("test", nsMenuItem.title());
    }

    @Test
    void testItemUpdateAction() {
        var wasHandled = new AtomicBoolean(false);
        var item = new MenuItem("test");
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);
        item.setOnAction(actionEvent -> wasHandled.set(true));
        item.fire();

        Assertions.assertTrue(wasHandled.get());
        Assertions.assertNotNull(nsMenuItem); // Prevent it from being GC'ed too early
    }

    @Test
    void testItemUpdateAccelerator() {
        var item = new MenuItem("test");
        NSMenuItem nsMenuItem = MenuItemConverter.convert(item);
        item.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN));

        Assertions.assertEquals("q", nsMenuItem.keyEquivalent());
        Assertions.assertEquals(NSEventModifierFlags.NSEventModifierFlagOption, nsMenuItem.keyEquivalentModifierMask());
    }
}