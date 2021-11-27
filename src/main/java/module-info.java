module com.example.archfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens project to javafx.fxml;
    exports project;
}