import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class VisualSortingAlgorithms extends BorderPane {
    VisualSortingAlgorithms() {
        HBox infoBox = new HBox();
        VBox selectionBox = new VBox();
        HBox displayBox = new HBox();
        HBox buttonBox = new HBox();

        Label valueLabel = new Label("Number of values:");
        Spinner<Integer> valueSpinner = new Spinner<Integer>(1, 25, 1);
        valueSpinner.setEditable(true);

        int numSwaps;
        int[] values = new int[10];

        Label[] valueLabels = new Label[values.length];
        for (int i = 0; i < values.length; i++) {
            valueLabels[i] = new Label();
            values[i] = (int) (500 * Math.random()) + 1;
            displayBox.getChildren().add(valueLabels[i]);
            valueLabels[i].setText("[" + values[i] + "] ");
        }

        setTop(infoBox);
        setLeft(selectionBox);
        setCenter(displayBox);
        setBottom(buttonBox);

        selectionBox.getChildren().addAll(valueLabel, valueSpinner);
    }
}