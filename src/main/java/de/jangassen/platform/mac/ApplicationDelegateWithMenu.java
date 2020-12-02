package de.jangassen.platform.mac;

import de.jangassen.jfa.FoundationProxy;
import de.jangassen.jfa.FoundationProxyHandler;
import de.jangassen.jfa.ObjcToJava;
import de.jangassen.jfa.annotation.InheritMethodsUpTo;
import de.jangassen.jfa.annotation.Unmapped;
import de.jangassen.jfa.appkit.NSApplication;
import de.jangassen.jfa.appkit.NSApplicationDelegate;
import de.jangassen.jfa.appkit.NSMenu;
import de.jangassen.jfa.foundation.Foundation;

@InheritMethodsUpTo(FoundationProxy.class)
public class ApplicationDelegateWithMenu extends FoundationProxy {
  private NSMenu menu;

  public ApplicationDelegateWithMenu(NSApplicationDelegate target) {
    this(target, new FoundationProxyHandler());
  }

  public ApplicationDelegateWithMenu(NSApplicationDelegate target, FoundationProxyHandler foundationProxyHandler) {
    super(target, foundationProxyHandler);
  }

  @Unmapped
  public void setMenu(NSMenu menu) {
    if (this.menu != null) {
      Foundation.cfRelease(ObjcToJava.toID(this.menu));
    }
    if (menu != null) {
      Foundation.cfRetain(ObjcToJava.toID(menu));
    }
    this.menu = menu;
  }

  @SuppressWarnings("unused")
  public NSMenu applicationDockMenu(NSApplication application) {
    return menu;
  }
}
