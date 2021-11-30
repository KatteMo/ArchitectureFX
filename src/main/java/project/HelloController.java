package project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloController {
    int countOfDevices;
    int countOfSources;
    int bufferSize;
    double lambda;
    int countOfTasks;
    double min;
    double max;
    Modeling modeling;

    @FXML
    private Button stepMode;

    @FXML
    private Button autoMode;

    @FXML
    private TextField insBuffers;

    @FXML
    private TextField insDevices;

    @FXML
    private TextField insSources;

    @FXML
    private TextField insLambda;

    @FXML
    private TextField insTasks;

    @FXML
    private TextField insMin;

    @FXML
    private TextField insMax;

    @FXML
    void initialize() {
        stepMode.setOnAction(actionEvent ->
        {
            stepMode.getScene().getWindow().hide();
            countOfSources = Integer.parseInt(insSources.getText());
            bufferSize = Integer.parseInt(insBuffers.getText());
            countOfDevices = Integer.parseInt(insDevices.getText());
            lambda = Double.parseDouble(insLambda.getText());
            countOfTasks = Integer.parseInt(insTasks.getText());
            min = Integer.parseInt(insMin.getText());
            max = Integer.parseInt(insMax.getText());
            modeling = new Modeling(countOfDevices, countOfSources, bufferSize, lambda, countOfTasks, min, max);
            modeling.beginStepByStepMode();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("step.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            StepApplication ap = new StepApplication(modeling);
            try {
                ap.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        autoMode.setOnAction(actionEvent ->
        {
            autoMode.getScene().getWindow().hide();
            countOfSources = Integer.parseInt(insSources.getText());
            bufferSize = Integer.parseInt(insBuffers.getText());
            countOfDevices = Integer.parseInt(insDevices.getText());
            lambda = Double.parseDouble(insLambda.getText());
            lambda = Double.parseDouble(insLambda.getText());
            countOfTasks = Integer.parseInt(insTasks.getText());
            min = Integer.parseInt(insMin.getText());
            max = Integer.parseInt(insMax.getText());
            modeling = new Modeling(countOfDevices, countOfSources, bufferSize, lambda, countOfTasks, min, max);
            modeling.beginAutoMode();
        });
    }


}