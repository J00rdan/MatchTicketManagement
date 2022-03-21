module com.example.projectjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens com.example.projectjavafx to javafx.fxml;
    exports com.example.projectjavafx;

    exports com.example.projectjavafx.Controllers;
    exports com.example.projectjavafx.Model;
}