package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javax.swing.text.StyleConstants.Background;
import static javax.swing.text.StyleConstants.LineSpacing;

public class StepApplication extends Application {
    int countOfSources;
    int countOfDevices;
    int bufferSize;
    Modeling modeling;
    ArrayList<Event> events;
    int numStep = 0;
    int generateCount = 0;
    Map<Integer, String> dict = new HashMap<Integer, String>();

    public StepApplication(Modeling modeling) {
        this.modeling = modeling;
        bufferSize = modeling.getBufferSize();
        countOfDevices = modeling.getController().getCountOfDevices();
        countOfSources = modeling.getController().getCountOfSources();
        events = modeling.getController().getEvents();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ScrollPane root = new ScrollPane();
        GridPane gridPane = new GridPane();
//        int c = 0;
//        for (Event e: events){
//            Label l = new Label(e.toString());
//            gridPane.add(l, 3, countOfSources + countOfDevices + bufferSize + 5 + c);
//            c++;
//        }
        Button next = new Button("next");
        next.setOnAction(actionEvent -> {
            nextStep(gridPane);
        });
        next.setMaxWidth(100);
        gridPane.add(next, 1, 0);
        root.setContent(gridPane);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        stage.setTitle("Пошаговый режим");
        gridPane.setGridLinesVisible(true);
        makeTable(gridPane);

        Scene scene = new Scene(root, 1500, 900);
        stage.setScene(scene);
        stage.show();
    }

    private void nextStep(GridPane gridPane) {
        Event currEvent = events.get(numStep);
        EventType currType = currEvent.getType();
        Label currLabel = new Label("  " + currEvent.getTaskId() + "  ");
        Event prevEvent;
        if (numStep > 0) {
            prevEvent = events.get(numStep - 1);
        } else {
            prevEvent = events.get(numStep);
        }
        EventType prevType = prevEvent.getType();
        //Label prevLabel = new Label("  " + prevEvent.getTaskId() + "  ");
        Event nextEvent;
        if (numStep < events.size() - 1) {
            nextEvent = events.get(numStep + 1);
        } else {
            nextEvent = events.get(numStep);
        }

        EventType nextType = nextEvent.getType();
        Label nextLabel = new Label("  " + nextEvent.getTaskId() + "  ");

        if (currType == EventType.generateTask) {
            gridPane.add(currLabel, generateCount + 1, getNumOfSou(currEvent.getTaskId()));
            currLabel = new Label("  " + currEvent.getTaskId() + "  ");
            if (nextType == EventType.denyTask) {
                int num = findPlaceInTable(nextEvent.getTaskId());
                gridPane.add(nextLabel, num, countOfSources + countOfDevices + bufferSize + 4);
                gridPane.add(currLabel, generateCount + 1, getNumOfDev(nextEvent) + 1 + countOfSources);

                int len = 30 * (countOfDevices + bufferSize + 3 - getNumOfDev(nextEvent) - 1);
                Line line = new Line(0, 0, 0, len + 3);
                line.setStroke(Color.RED);
                line.getStrokeDashArray().addAll(25.0, 10.0);
                GridPane.setHalignment(line, HPos.CENTER);
                GridPane.setValignment(line, VPos.TOP);
                gridPane.add(line, num, getNumOfDev(nextEvent) + 2 + countOfSources);
            } else {
                gridPane.add(currLabel, generateCount + 1, getNumOfDev(currEvent) + 1 + countOfSources);
            }
            generateCount++;
            dict.put(generateCount, currEvent.getTaskId());
        } else if (currType == EventType.startDevice) {
            Line line = new Line(10, 0, 30, 0);
            GridPane.setHalignment(line, HPos.CENTER);
            Event genEvent = getNumOfEvent(EventType.generateTask, currEvent.getTaskId());
            int pointer = getNumOfDev(genEvent);

            if (prevType == EventType.finishDevice) {
                int num = findPlaceInTable(currEvent.getTaskId());
                gridPane.add(currLabel, num, getNumOfDev(currEvent) + bufferSize + 2 + countOfSources);
                gridPane.add(line, num, pointer + 1 + countOfSources);
            } else {
                gridPane.add(currLabel, generateCount, getNumOfDev(currEvent) + bufferSize + 2 + countOfSources);
                gridPane.add(line, generateCount, pointer + 1 + countOfSources);
            }
        }
        numStep++;
        makePart(gridPane);
    }

    public int findPlaceInTable(String id) {
        int num = 1;
        for (Integer curVal : dict.keySet()) {
            if (Objects.equals(dict.get(curVal), id)) {
                num = curVal;
                break;
            }
        }
        return num;
    }

    public int getNumOfDev(Event event) {
        String string = event.getAdditionInfo();
        int i = string.lastIndexOf(" ") + 1;
        string = string.substring(i);
        return Integer.parseInt(string);
    }

    public int getNumOfSou(String str) {
        int i = str.lastIndexOf(".");
        str = str.substring(0, i);
        return Integer.parseInt(str);
    }

    public Event getNumOfEvent(EventType type, String id) {
        for (Event cur : events) {
            if (cur.getType() == type && Objects.equals(cur.getTaskId(), id)) {
                return cur;
            }
        }
        return events.get(0);
    }

    public void makePart(GridPane gridPane) {
        Rectangle rect = new Rectangle(40, 30, Color.GRAY);
        GridPane.setHalignment(rect, HPos.CENTER);
        GridPane.setValignment(rect, VPos.CENTER);
        for (int i = 0; i < countOfSources + countOfDevices + bufferSize + 5; i++) {
            if (i == 0 && generateCount != 1) {
                gridPane.add(rect, generateCount, 0);
            } else if (i == countOfSources + 1) {
                rect = new Rectangle(40, 30, Color.GRAY);
                gridPane.add(rect, generateCount, i);
            } else if (i == countOfSources + bufferSize + 2) {
                rect = new Rectangle(40, 30, Color.GRAY);
                gridPane.add(rect, generateCount, i);
            } else if (i == countOfSources + countOfDevices + bufferSize + 3) {
                rect = new Rectangle(40, 30, Color.GRAY);
                gridPane.add(rect, generateCount, i);
            }
        }
    }

    public void makeTable(GridPane gridPane) {
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        for (int i = 0; i < countOfSources + countOfDevices + bufferSize + 5; i++) {
            gridPane.getRowConstraints().add(new RowConstraints(30));
            if (i == 0) {
                Label label = new Label(" Sources");
                gridPane.add(label, 0, 0);
            } else if (i == countOfSources + 1) {
                Label label = new Label("  Buffer");
                gridPane.add(label, 0, i);
            } else if (i == countOfSources + bufferSize + 2) {
                Label label = new Label(" Devises");
                gridPane.add(label, 0, i);
            } else if (i == countOfSources + countOfDevices + bufferSize + 3) {
                Label label = new Label("  Deny");
                gridPane.add(label, 0, i);
            } else {
                if (i <= countOfSources) {
                    Label label = new Label("   " + Integer.toString(i));
                    gridPane.add(label, 0, i);
                } else if (i - countOfSources - 1 <= bufferSize) {
                    Label label = new Label("   " + Integer.toString(i - countOfSources - 1));
                    gridPane.add(label, 0, i);
                } else if (i - countOfSources - bufferSize - 2 <= countOfDevices) {
                    Label label = new Label("   " + Integer.toString(i - countOfSources - bufferSize - 2));
                    gridPane.add(label, 0, i);
                }
            }
        }
    }
}