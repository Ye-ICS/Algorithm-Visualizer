import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Custom layout based on VBox for a menu to select which algorithm to visualize.
 */
class MaximMenuLayout extends VBox {
    MaximMenuLayout() {
        setAlignment(Pos.CENTER);

        Text title = new Text("Maxim's Algorithm Visualizer");
        title.setFont(Font.font(24));
        
        FlowPane buttonsBox = new FlowPane();
        buttonsBox.setAlignment(Pos.CENTER);

        // Buttons for each sorting algorithm
        Button bubbleSortBtn = new Button("Bubble Sort");
        bubbleSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new BBBubbleSortLayout()));

        Button selectionSortBtn = new Button("Selection Sort");
        selectionSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new SelectionSortLayout()));

        Button quickSortBtn = new Button("Quick Sort");
        quickSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new QuickSortLayout()));

        Button mergeSortBtn = new Button("Merge Sort");
        mergeSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MergeSortLayout()));

        Button bogoSortBtn = new Button("Bogo Sort");
        bogoSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new BogoSortLayout()));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        buttonsBox.getChildren().addAll(bubbleSortBtn, selectionSortBtn, quickSortBtn, mergeSortBtn, bogoSortBtn, backBtn);
        getChildren().addAll(title, buttonsBox);
    }
}
