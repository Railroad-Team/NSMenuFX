package dev.railroadide.nsmenufx.platform;

import dev.railroadide.nsmenufx.platform.mac.MacNativeAdapter;

public class NativeAdapterProvider {
    private NativeAdapterProvider() {
    }

    public static NativeAdapter getNativeAdapter() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac") && MacNativeAdapter.isAvailable()) {
            return MacNativeAdapter.getInstance();
        }

        return new DummyNativeAdapter();
    }
}
