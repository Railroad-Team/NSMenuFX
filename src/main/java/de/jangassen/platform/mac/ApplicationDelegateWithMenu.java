package de.jangassen.platform.mac;

import de.jangassen.jfa.FoundationProxy;
import de.jangassen.jfa.FoundationProxyHandler;
import de.jangassen.jfa.annotation.Unmapped;
import de.jangassen.jfa.appkit.NSApplication;
import de.jangassen.jfa.appkit.NSApplicationDelegate;
import de.jangassen.jfa.appkit.NSMenu;

public class ApplicationDelegateWithMenu extends FoundationProxy {
  private NSMenu menu;

  public ApplicationDelegateWithMenu(NSApplicationDelegate target) {
    super(target, new FoundationProxyHandler());
  }

  @Unmapped
  public void setMenu(NSMenu menu) {
    this.menu = menu;
  }

  public NSMenu applicationDockMenu(NSApplication application) {
    return menu;
  }
}
