import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.GraphicsContext;

public class VisualSortingAlgorithms extends BorderPane {
    VisualSortingAlgorithms() {
        VBox selectionBox = new VBox();
        HBox displayBox = new HBox();

        Label valueLabel = new Label("Number of values:");

        Spinner<Integer> valueSpinner = new Spinner<Integer>(2, 25, 1);
        valueSpinner.setEditable(true);

        // int numSwaps;

        Button submitBtn = new Button();
        submitBtn.setText("Submit");
        submitBtn.setOnAction(event -> calculateValues(displayBox, valueSpinner));

        setLeft(selectionBox);
        setCenter(displayBox);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner, submitBtn);
    }

    void calculateValues(HBox displayBox, Spinner<Integer> valueSpinner) {
        int[] values = new int[valueSpinner.getValue()];
        displayBox.getChildren().clear();
        Label[] valueLabels = new Label[values.length];
        for (int i = 0; i < values.length; i++) {
            valueLabels[i] = new Label();
            values[i] = (int) (500 * Math.random()) + 1;
            displayBox.getChildren().add(valueLabels[i]);
            valueLabels[i].setText("  " + values[i] + "  ");
        }
    }
}