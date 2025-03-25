import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.security.Key;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class VisualSortingAlgorithms extends BorderPane {
    boolean blankRender;
    boolean sorted;
    int lowestLocation;
    int lowestValue = 500;
    int selectedLocation;
    int recentlySorted;
    int numSorted;
    int timer;
    Timeline autoSort;
    int[] values;

    VisualSortingAlgorithms() {
        FlowPane selectionBox = new FlowPane();
        Canvas canvas = new Canvas(500, 500);

        Label valueLabel = new Label("");
        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 32, 1);
        valueSpinner.setEditable(true);

        Spinner<Integer> timeSpinner = new Spinner<Integer>(1, 10, 1);
        timeSpinner.setEditable(true);

        autoSort = new Timeline(new KeyFrame(Duration.millis(25),
                event -> selectionSort(canvas, valueSpinner.getValue(), canvas.getGraphicsContext2D(), values)));
        autoSort.setCycleCount(Timeline.INDEFINITE);

        Button submitBtn = new Button();
        submitBtn.setText("Create");
        submitBtn.setOnAction(event -> onCreatePress(canvas, valueSpinner.getValue(), autoSort, submitBtn));

        Label formatLabel = new Label("                   ");

        Button startBtn = new Button();
        startBtn.setText("Start");
        startBtn.setOnAction(event -> {
            onStartPress(canvas, timeSpinner.getValue(), valueSpinner.getValue(), autoSort);
        });

        setBottom(selectionBox);
        setCenter(canvas);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner, submitBtn, formatLabel, timeSpinner, startBtn);
    }

    /**
     * describe
     * 
     * @param canvas
     * @param submitBtn
     * @param valueSpinner spiiner containing how many bars to render
     */
    void onCreatePress(Canvas canvas, int count, Timeline autoSort, Button submitBtn) {
        sorted = false;
        autoSort.stop();
        blankRender = true;
        submitBtn.setText("Reset");
        values = generateValues(canvas, count);
        resetSort();
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        blankRender = false;
    }

    void onStartPress(Canvas canvas, int time, int count, Timeline autoSort) {
        autoSort.stop();
        autoSort.play();
        selectionSort(canvas, count, canvas.getGraphicsContext2D(), values);
    }

    int[] generateValues(Canvas canvas, int count) {
        int[] values = new int[count];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (500 * Math.random()) + 1;
        }
        return values;
    }

    void resetSort() {
        lowestLocation = 0;
        lowestValue = 500;
        selectedLocation = 0;
        recentlySorted = -1;
        numSorted = 0;
    }

    void checkSort(Timeline autoSort, Canvas canvas, int count) {
        if (!sorted) {
            resetSort();
            sorted = true;
        } else {
            autoSort.stop();
        }
    }

    void renderBars(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < values.length; i++) {
            if ((selectedLocation == i || lowestLocation == i) && !blankRender && !sorted) {
                graphicsContext.setFill(Color.RED);
            } else if ((recentlySorted == i && !blankRender) || sorted && i <= selectedLocation) {
                graphicsContext.setFill(Color.GREEN);
            } else {
                graphicsContext.setFill(Color.BLACK);
            }
            graphicsContext.fillRect((i * 10) + i, canvas.getHeight() - values[i], 10, values[i]);
        }
    }

    void selectionSort(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {
        if (numSorted == values.length - 1 || selectedLocation == values.length && sorted) {
            checkSort(autoSort, canvas, count);
        }
        if (!sorted) {
            if (selectedLocation == values.length) {
                for (int i = lowestLocation; i > numSorted; i--) {
                    values[i] = values[i - 1];
                }
                values[numSorted] = lowestValue;
                recentlySorted = numSorted;
                lowestValue = 500;
                numSorted++;
                selectedLocation = numSorted;
            }
            if (values[selectedLocation] < lowestValue) {
                lowestValue = values[selectedLocation];
                lowestLocation = selectedLocation;
            }
        }
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        selectedLocation++;
    }
}
/*
 * void selectionSort(Canvas canvas, Spinner<Integer> valueSpinner,
 * GraphicsContext graphicsContext, int[] values) {
 * for (int i = 0; i < valueSpinner.getValue(); i++) {
 * int selected = 0;
 * int selectedLocation = 0;
 * int lowest = 500;
 * int lowestLocation = 0;
 * 
 * for (int j = i; j < valueSpinner.getValue(); j++) {
 * selected = values[j];
 * selectedLocation = j;
 * renderValues(canvas, valueSpinner, graphicsContext);
 * 
 * if (selected < lowest) {
 * lowest = selected;
 * lowestLocation = j;
 * }
 * }
 * values[lowestLocation] = values[i];
 * values[i] = lowest;
 * }
 * }
 */