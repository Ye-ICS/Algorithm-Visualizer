import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Custom layout based on VBox for a menu to select which algorithm to
 * visualize.
 */
class MenuLayout extends VBox {
    /**
     * Basic constructor, initializes the menu with a button to each algorithm.
     */
    @SuppressWarnings("unused")
    MenuLayout() {
        setAlignment(Pos.CENTER);

        Text title = new Text("Algorithm Visualizer");
        title.setFont(Font.font(24));

        FlowPane buttonsBox = new FlowPane();
        buttonsBox.setAlignment(Pos.CENTER);

        Button bubbleSortBtn = new Button("Bubble Sort");
        Button AESBtn = new Button("Advanced Encryption Standard algorithm");
        AESBtn.setMinSize(300, 50);
        AESBtn.setOnAction(event -> {
            FXUtils.setSceneRoot(getScene(), new AEStart());
        });

        AESBtn.getStyleClass().add("AEStyling"); // Corrected CSS class name


        Button mazeBtn = new Button("Maze Generator");
        mazeBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new Maze()));

        Button aStarBtn = new Button("A*");
        aStarBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new AStar()));

        
        Button mazeSortBtn = new Button("Maze Solver - Declan");
        mazeSortBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new DeclanJonesLayout()));
        

        buttonsBox.getChildren().addAll(mazeSortBtn, bubbleSortBtn, aStarBtn, AESBtn, mazeBtn, aStarBtn);
        getChildren().addAll(title, buttonsBox);

        // Load CSS file
        getStylesheets().add(getClass().getResource("css/AEStyling.css").toExternalForm());
    }

}
