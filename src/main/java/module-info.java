module com.example.console {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.console to javafx.fxml;
    exports com.example.console;
}