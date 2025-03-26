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

        Text title = new Text("Algorithm Visualizer");
        title.setFont(Font.font(24));
        
        FlowPane buttonsBox = new FlowPane();
        buttonsBox.setAlignment(Pos.CENTER);

        // Buttons for each sorting algorithm
        Button Maximbtn = new Button("Maxim");
        Maximbtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MaximMenuLayout()));

        buttonsBox.getChildren().addAll(Maximbtn);
       //buttonsBox.getChildren().addAll(bubbleSortBtn);
        
        Button mazeSortBtn = new Button("Maze Solver - Declan");
        mazeSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new DeclanJonesLayout()));

        buttonsBox.getChildren().addAll(mazeSortBtn, Maximbtn);
        getChildren().addAll(title, buttonsBox);
    }
}
