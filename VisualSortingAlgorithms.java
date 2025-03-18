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
    VisualSortingAlgorithms() {
        FlowPane selectionBox = new FlowPane();
        Canvas canvas = new Canvas(500, 500);

        Label valueLabel = new Label("");
        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 32, 1);
        valueSpinner.setEditable(true);
        Button submitBtn = new Button();
        submitBtn.setText("Submit");
        submitBtn.setOnAction(event -> calculateValues(canvas, valueSpinner, canvas.getGraphicsContext2D()));

        Label timeLabel = new Label("                 ");
        Spinner<Integer> timeSpinner = new Spinner<Integer>(2, 32, 1);
        timeSpinner.setEditable(true);
        Button startBtn = new Button();
        startBtn.setText("Start");
        //startBtn.setOnAction(event -> );
        Button pauseBtn = new Button();
        pauseBtn.setText("Pause");
        // pauseBtn.setOnAction(event -> );

        setBottom(selectionBox);
        setCenter(canvas);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner, submitBtn, timeLabel, timeSpinner, startBtn, pauseBtn);
    }

    void calculateValues(Canvas canvas, Spinner<Integer> valueSpinner, GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int[] values = new int[valueSpinner.getValue()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (500 * Math.random()) + 1;
            graphicsContext.fillRect((i * 10) + i, canvas.getHeight() - values[i], 10, values[i]);
        }
    }
}