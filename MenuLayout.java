import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Custom layout based on VBox for a menu to select which algorithm to visualize.
 */
class MenuLayout extends VBox {
    MenuLayout() {
        setAlignment(Pos.CENTER);

        Text title = new Text("Maxim's Algorithm Visualizer");
        title.setFont(Font.font(24));
        
        FlowPane buttonsBox = new FlowPane();
        buttonsBox.setAlignment(Pos.CENTER);

        // Buttons for each sorting algorithm
        Button bubbleSortBtn = new Button("Bubble Sort");
        bubbleSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new BubbleSortLayout()));

        Button selectionSortBtn = new Button("Selection Sort");
        selectionSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new SelectionSortLayout()));

        Button quickSortBtn = new Button("Quick Sort");
        quickSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new QuickSortLayout()));

        Button mergeSortBtn = new Button("Merge Sort");
        mergeSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MergeSortLayout()));

        Button bogoSortBtn = new Button("Bogo Sort");
        bogoSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new BogoSortLayout()));

        buttonsBox.getChildren().addAll(bubbleSortBtn, selectionSortBtn, quickSortBtn, mergeSortBtn, bogoSortBtn);
        getChildren().addAll(title, buttonsBox);
    }
}
