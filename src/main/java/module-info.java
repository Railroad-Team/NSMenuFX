module nsmenufx {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires com.sun.jna;
    requires jfa;

    exports dev.yodaforce;
    exports dev.yodaforce.labels;
    exports dev.yodaforce.dialogs.about;
    exports dev.yodaforce.icns;
    exports dev.yodaforce.model;

    exports dev.yodaforce.platform.mac to jfa;
}