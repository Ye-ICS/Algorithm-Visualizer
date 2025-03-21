import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class VisualSortingAlgorithms extends BorderPane {
    boolean firstRender;
    int lowestLocation;
    int lowestValue = 500;
    int selectedLocation;
    int recentlySorted;
    int numSorted;

    int[] values;

    VisualSortingAlgorithms() {
        FlowPane selectionBox = new FlowPane();
        Canvas canvas = new Canvas(500, 500);

        Label valueLabel = new Label("");
        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 32, 1);
        valueSpinner.setEditable(true);
        Button submitBtn = new Button();
        submitBtn.setText("Submit");
        submitBtn.setOnAction(event -> onSubmitPress(canvas, valueSpinner.getValue()));

        Label timeLabel = new Label("                                ");
        Spinner<Integer> timeSpinner = new Spinner<Integer>(2, 32, 1);
        timeSpinner.setEditable(true);

        Button stepBtn = new Button();
        stepBtn.setText("Step");
        stepBtn.setOnAction(event -> onStepPress(canvas, valueSpinner.getValue()));

        setBottom(selectionBox);
        setCenter(canvas);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner, submitBtn, timeLabel, timeSpinner, stepBtn);
    }

    /**
     * describe
     * @param canvas 
     * @param valueSpinner spiiner containing how many bars to render
     */
    void onSubmitPress(Canvas canvas, int count) {
        firstRender = true;
        values = generateValues(canvas, count);
        resetSort();
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        firstRender = false;
    }

    void onStepPress(Canvas canvas, int count) {
        firstRender = false;
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

    void renderBars(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < values.length; i++) {
            if ((selectedLocation == i || lowestLocation == i) && !firstRender) {
                graphicsContext.setFill(Color.RED);
            } else if (recentlySorted == i && !firstRender) {
                graphicsContext.setFill(Color.GREEN);
            } else {
                graphicsContext.setFill(Color.BLACK);
            }
            graphicsContext.fillRect((i * 10) + i, canvas.getHeight() - values[i], 10, values[i]);
        }
    }

    void selectionSort(Canvas canvas, int count, GraphicsContext graphicsContext, int[] values) {

        if (selectedLocation == values.length) {
            /* for (int i = lowestLocation; i > 0; i--) {
                values[i] = values[i - i];
            }
            values[0] = lowestValue; */
            numSorted++;
            selectedLocation = numSorted;
        }
        if (values[selectedLocation] < lowestValue) {
            lowestValue = values[selectedLocation];
            lowestLocation = selectedLocation;
        }
        renderBars(canvas, count, canvas.getGraphicsContext2D(), values);
        selectedLocation++;
        System.out.println(selectedLocation);
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