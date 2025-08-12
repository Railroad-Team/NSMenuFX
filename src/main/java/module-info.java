module nsmenufx {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires com.sun.jna;
    requires jfa;

    exports dev.railroadide.nsmenufx;
    exports dev.railroadide.nsmenufx.labels;
    exports dev.railroadide.nsmenufx.dialogs.about;
    exports dev.railroadide.nsmenufx.icns;
    exports dev.railroadide.nsmenufx.model;

    exports dev.railroadide.nsmenufx.platform.mac to jfa;
}