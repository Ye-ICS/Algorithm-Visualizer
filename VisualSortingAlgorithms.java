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

    Timeline autoSort;
    int[] values;

    boolean selectionSort;
    boolean bubbleSort;
    boolean shakerSort;
    boolean insertionSort;
    boolean mergeSort;
    boolean quickSort;
    boolean timSort;

    VisualSortingAlgorithms() {
        FlowPane selectionBox = new FlowPane();
        Canvas canvas = new Canvas(1100, 500);

        Label valueLabel = new Label("");
        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 100, 1);
        valueSpinner.setEditable(true);

        Spinner<Integer> timeSpinner = new Spinner<Integer>(1, 5, 1);
        timeSpinner.setEditable(true);

        autoSort = new Timeline(new KeyFrame(Duration.millis(100), // lowest = .0833
                event -> selectionSort(canvas, valueSpinner.getValue(), canvas.getGraphicsContext2D(), values)));
        autoSort.setCycleCount(Timeline.INDEFINITE);

        Button createBtn = new Button();
        createBtn.setText("Create");
        createBtn.setOnAction(event -> onCreatePress(canvas, valueSpinner.getValue(), autoSort, createBtn));

        Label formatLabel = new Label("                   ");

        Button selSortBtn = new Button();
        selSortBtn.setText("Selection Sort");
        selSortBtn.setOnAction(event -> {
            onSortPress(canvas, timeSpinner.getValue(), valueSpinner.getValue(), autoSort);
        });

        setBottom(selectionBox);
        setCenter(canvas);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner, createBtn, formatLabel, timeSpinner, selSortBtn);
    }

    void onCreatePress(Canvas canvas, int count, Timeline autoSort, Button createBtn) {
        sorted = false;
        autoSort.stop();
        blankRender = true;
        values = generateValues(canvas, count);
        resetSort();
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        blankRender = false;
    }

    int[] generateValues(Canvas canvas, int count) {
        int[] values = new int[count];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (500 * Math.random()) + 1;
        }
        return values;
    }

    void renderBars(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < values.length; i++) {
            if ((selectedLocation == i || lowestLocation == i) && !blankRender && !sorted) {
                graphicsContext.setFill(Color.RED);
            } else if ((recentlySorted == i && !blankRender) || sorted && i <= selectedLocation && !blankRender) {
                graphicsContext.setFill(Color.GREEN);
            } else {
                graphicsContext.setFill(Color.BLACK);
            }
            graphicsContext.fillRect((i * 10) + i, canvas.getHeight() - values[i], 10, values[i]);
        }
    }

    void onSortPress(Canvas canvas, int time, int count, Timeline autoSort) {
        if (!sorted) {
            autoSort.stop();
            if (time == 1) {
                autoSort.setRate(time);
            } else if (time == 2) {
                autoSort.setRate(5);
            } else if (time == 3) {
                autoSort.setRate(25);
            } else if (time == 4) {
                autoSort.setRate(100);
            } else {
                autoSort.setRate(2147483647);
            }
            autoSort.play();
            selectionSort(canvas, count, canvas.getGraphicsContext2D(), values);
        }
    }

    void manageSort(Canvas canvas, int count) {
        if (selectionSort) {
            selectionSort(canvas, count, canvas.getGraphicsContext2D(), values);
        } else if (bubbleSort) {

        } else if (shakerSort) {

        } else if (insertionSort) {

        } else if (mergeSort) {

        } else if (quickSort) {

        } else if (timSort) {

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
            blankRender = true;
            renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        }
    }
}
