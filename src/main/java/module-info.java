module nsmenufx {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires com.sun.jna;
    requires jfa;

    exports de.jangassen;
    exports de.jangassen.labels;
    exports de.jangassen.dialogs.about;
    exports de.jangassen.icns;
    exports de.jangassen.model;

    exports de.jangassen.platform.mac to jfa;
}