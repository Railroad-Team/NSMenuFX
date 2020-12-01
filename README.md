# NSMenuFX

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/61379f5f801c464fb8cf5488d7c9f0c3)](https://www.codacy.com/app/0x4a616e/NSMenuFX?utm_source=github.com&utm_medium=referral&utm_content=codecentric/NSMenuFX&utm_campaign=badger)
[![Maven Central](https://img.shields.io/maven-central/v/de.jangassen/nsmenufx.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22de.jangassen%22%20AND%20a:%22nsmenufx%22)
![Test](https://github.com/0x4a616e/NSMenuFX/workflows/Test/badge.svg)

A simple library to customize the macOS menu bar to give your JavaFX app
a more [native look and feel](https://developer.apple.com/library/mac/documentation/UserExperience/Conceptual/OSXHIGuidelines/MenuBarMenus.html).  

NSMenuFX provides a mapping layer between the JavaFX `Menu` and AppKits `NSMenu` objects. It uses [JNA](https://github.com/java-native-access/jna) to directly set the menus for your application using macOS native API.

## Features

Here are a few examples of what you can do with NSMenuFX.

### Application menu

![Custom App Menu Screenshot](./Assets/Screenshots/app_menu.png)

Customize the auto-generated application menu of your JavaFX app.

```java
// Create the default Application menu
Menu defaultApplicationMenu = tk.createDefaultApplicationMenu("test");

// Update the existing Application menu
MenuToolkit.toolkit().setApplicationMenu(defaultApplicationMenu);
```

### Window menu

![Custom App Menu Screenshot](./Assets/Screenshots/window_menu.png)

Create common macOS menus like the Window menu.

```java
// Create the window menu
Menu windowMenu = new Menu("Window");
// ...
// Add your own menu items

// Automatically add windows
MenuToolkit.toolkit().autoAddWindowMenuItems(windowMenu);
```

### Dock menu

![Custom App Menu Screenshot](./Assets/Screenshots/dock_menu.png)

Create a dock icon menu. Note that images for menu items in dock menus are not supported by macOS.

```java
// Create the dock menu
Menu menu = new Menu("Window");
// ...
// Add your own menu items

// Set the dock menu
MenuToolkit.toolkit().setDocIconMenu(menu);
```

### Tray menu

![Custom App Menu Screenshot](./Assets/Screenshots/tray_menu.png)

Add a tray menu. Pass `null` to remove the tray menu again.

```java
// Create the tray menu
Menu menu = new Menu("Window");
// ...
// Add your own menu items

// Set the try menu
MenuToolkit.toolkit().setTrayMenu(menu);
```

### Context menu

![Custom App Menu Screenshot](./Assets/Screenshots/context_light.png)
![Custom App Menu Screenshot](./Assets/Screenshots/context_dark.png)

Use the native context menu instead of a JavaFX based context menu.

```java
// Create the context menu
Menu menu = new Menu();
// ...
// Add your own menu items

// Show the context menu when right-clicking the stage
scene.setOnMouseClicked(event ->
{
  if (event.getButton() == MouseButton.SECONDARY) {
    MenuToolkit.toolkit().showContextMenu(context, event);
  }
});
```

To adapt the context menu appearence, you can switch between `LIGHT` and `DARK` mode, or use `AUTO` to adapt the appearence of macOS.

```java
// Set appearance automatically (or manually to DARK/LIGHT)
MenuToolkit.toolkit().setAppearanceMode(AppearanceMode.AUTO);
```

### And more

* Quickly create an "About" menu
* Automatically use the same menu bar for all stages

To find more examples, check out the sample applications [here](https://github.com/0x4a616e/NSMenuFX/tree/master/samples/src/main/java/de/jangassen/nsmenufx/samples).

## Maven

Add the following lines to the dependencies in your `pom.xml`
```xml
<dependency>
    <groupId>de.jangassen</groupId>
    <artifactId>nsmenufx</artifactId>
    <version>3.0.1</version>
</dependency>
```
## Gradle

Add the following line to the dependencies in your `build.gradle`

	compile "de.jangassen:nsmenufx:3.0.1"

## Migrating from 2.1

For most parts, the API has not changed. The most prominent difference is that the package name has changed from `de.codecentric.centerdevice` to `de.jangassen` to match the new maven coordinates. Also, the About dialog no longer uses a `WebView`, so it can either display plain text or a list of `Text` objects. If you want to continue using a `WebView`, you have to create one and pass that to the `AboutStageBuilder`:

```java
WebView webView = new WebView();
webView.getEngine().loadContet("<b>Credits</b>");

AboutStageBuilder.start("My App").withNode(webView).build();
```

## Known issues

NSMenuFX no longer supports changing the title of the application menu at
runtime. This has always been a bit "hacky" as it is not really supported
by macOS. As a result, the new name was no longer bold faced when it was
changed with previous versions of NSMenuFX.

To set the title of the application menu to the name of your application,
you need to bundle the application and set `CFBundleName` in `Info.plist`.
