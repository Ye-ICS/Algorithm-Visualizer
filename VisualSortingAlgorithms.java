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

    int savedLocation;
    int savedValue = 500;
    int selectedLocation;
    int recentlySorted;
    int numSorted;

    Timeline autoSort;
    int[] values;

    boolean bubbleSort;
    boolean shakerSort;
    boolean selectionSort;
    boolean insertionSort;
    boolean mergeSort;
    boolean quickSort;
    boolean timSort;

    VisualSortingAlgorithms() {
        FlowPane selectionBox = new FlowPane();
        Canvas canvas = new Canvas(1100, 500);
        Label formatLabel = new Label("                                                                                                             ");

        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 100, 2);
        valueSpinner.setEditable(true);

        Spinner<Integer> timeSpinner = new Spinner<Integer>(0, 5, 1);
        timeSpinner.setEditable(true);

        autoSort = new Timeline(
                new KeyFrame(Duration.millis(100), event -> manageSort(canvas, valueSpinner.getValue())));
        autoSort.setCycleCount(Timeline.INDEFINITE);

        Button createBtn = new Button();
        createBtn.setText("Create");
        createBtn.setOnAction(event -> onCreatePress(canvas, valueSpinner.getValue(), autoSort, createBtn));

        Button bblSortBtn = new Button();
        bblSortBtn.setText("Bubble Sort");
        bblSortBtn.setOnAction(event -> onBblSortPress(canvas, timeSpinner.getValue(), valueSpinner.getValue(), autoSort));

        Button selSortBtn = new Button();
        selSortBtn.setText("Selection Sort");
        selSortBtn.setOnAction(event -> onSelSortPress(canvas, timeSpinner.getValue(), valueSpinner.getValue(), autoSort));

        setBottom(selectionBox);
        setCenter(canvas);

        selectionBox.getChildren().addAll(valueSpinner, createBtn, formatLabel, timeSpinner, bblSortBtn, selSortBtn);
    }

    void onCreatePress(Canvas canvas, int count, Timeline autoSort, Button createBtn) {
        resetSort();
        sorted = false;
        autoSort.stop();
        blankRender = true;
        values = generateValues(canvas, count);
        resetVariables();
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        blankRender = false;
    }

    void onBblSortPress(Canvas canvas, int time, int count, Timeline autoSort) {
                if (!bubbleSort) {
                    resetVariables();
                    resetSort();
                    bubbleSort = true;
                }
                onSortPress(canvas, time, count, autoSort);
    }

    void onSelSortPress(Canvas canvas, int time, int count, Timeline autoSort) {
        if (!selectionSort) {
            resetVariables();
            resetSort();
            selectionSort = true;
        }
        onSortPress(canvas, time, count, autoSort);
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
            if ((((selectedLocation == i || savedLocation == i) && selectionSort)
                    || ((selectedLocation == i || (selectedLocation + 1) == i) && bubbleSort)) && !blankRender
                    && !sorted) {
                graphicsContext.setFill(Color.RED);
            } else if ((recentlySorted == i || sorted && i <= selectedLocation) && !blankRender) {
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
            if (time == 0) {
                autoSort.setRate(0.01);
            } else if (time == 1) {
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
            manageSort(canvas, count);
        }
    }

    void manageSort(Canvas canvas, int count) {
        if (selectionSort) {
            selectionSort(canvas, count, canvas.getGraphicsContext2D(), values);
        } else if (bubbleSort) {
            bubbleSort(canvas, count);
        } else if (shakerSort) {

        } else if (insertionSort) {

        } else if (mergeSort) {

        } else if (quickSort) {

        } else if (timSort) {

        }
    }

    void bubbleSort(Canvas canvas, int count) {
        if (numSorted == values.length - 1 || selectedLocation == values.length && sorted) {
            checkSort(autoSort, canvas, count);
        }
        if (!sorted) {
            if (selectedLocation + 1 <= (values.length - 1) - numSorted) {
                if (values[selectedLocation] > values[selectedLocation + 1]) {
                    savedValue = values[selectedLocation];
                    values[selectedLocation] = values[selectedLocation + 1];
                    values[selectedLocation + 1] = savedValue;
                }
            }
            if (selectedLocation + 1 == values.length - numSorted) {
                numSorted++;
                recentlySorted = values.length - numSorted;
                selectedLocation = -1;
            }
        }
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        selectedLocation++;

    }

    void selectionSort(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {
        if (numSorted == values.length - 1 || selectedLocation == values.length && sorted) {
            checkSort(autoSort, canvas, count);
        }
        if (!sorted) {
            if (selectedLocation == values.length) {
                for (int i = savedLocation; i > numSorted; i--) {
                    values[i] = values[i - 1];
                }
                values[numSorted] = savedValue;
                recentlySorted = numSorted;
                savedValue = 500;
                numSorted++;
                selectedLocation = numSorted;
            }
            if (values[selectedLocation] < savedValue) {
                savedValue = values[selectedLocation];
                savedLocation = selectedLocation;
            }
        }
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        selectedLocation++;
    }

    void resetSort() {
        bubbleSort = false;
        shakerSort = false;
        selectionSort = false;
        insertionSort = false;
        mergeSort = false;
        quickSort = false;
        timSort = false;
    }

    void resetVariables() {
        savedLocation = 0;
        savedValue = 500;
        selectedLocation = 0;
        recentlySorted = -1;
        numSorted = 0;
    }

    void checkSort(Timeline autoSort, Canvas canvas, int count) {
        if (!sorted) {
            resetVariables();
            sorted = true;
        } else {
            autoSort.stop();
            blankRender = true;
            renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        }
    }
}

/*
 * static void bubbleSort(double[] numbers) {
 * boolean sorted;
 * 
 * do {
 * sorted = true;
 * for (int i = 0; i < numbers.length - 1; i++) {
 * if (numbers[i] > numbers[i + 1]) {
 * sorted = false;
 * // Swap:
 * double temp = numbers[i];
 * numbers[i] = numbers[i + 1];
 * numbers[i + 1] = temp;
 * }
 * }
 * } while (!sorted);
 * }
 */