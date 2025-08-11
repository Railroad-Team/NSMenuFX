package dev.yodaforce.platform;

import dev.yodaforce.platform.mac.MacNativeAdapter;

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
