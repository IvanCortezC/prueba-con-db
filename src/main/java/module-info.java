module com.duoc.adoptame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.compiler;
    requires java.sql;

    opens com.duoc.adoptame to javafx.fxml;
    exports com.duoc.adoptame;
    exports com.duoc.adoptame.controller;
    opens com.duoc.adoptame.controller to javafx.fxml;
}