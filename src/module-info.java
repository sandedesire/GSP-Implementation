module GSPImplementation {
    requires javafx.base;
    requires javafx.controls;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;
    requires javafx.fxml;
    requires jdk.jsobject;


    opens GSPImplementation to javafx.base,javafx.graphics;

}