package de.jangassen.platform;

import de.jangassen.platform.mac.MacNativeAdapter;

public class NativeAdapterProvider {
  private NativeAdapterProvider() {}

  public static NativeAdapter getNativeAdapter() {
    String os = System.getProperty("os.name");
    if (os.startsWith("Mac") && MacNativeAdapter.isAvailable()) {
      return MacNativeAdapter.getInstance();
    }

    return new DummyNativeAdapter();
  }
}
