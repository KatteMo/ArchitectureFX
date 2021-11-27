package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import static javax.swing.text.StyleConstants.Background;

public class AutoApplication extends Application {

    Modeling modeling;

    public AutoApplication(Modeling modeling) {
        this.modeling = modeling;
    }

    @Override
    public void start(Stage stage) throws IOException {
    }
}