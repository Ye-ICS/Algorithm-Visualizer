import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class RecursiveDivisionLayout extends FlowPane{
    public void recursiveDivisionLayout(Stage stage){
        BorderPane rootLayout = new BorderPane();

        Canvas canvas = new Canvas(500, 500);
        ScrollPane scrollPane = new ScrollPane(canvas);

        Spinner<Integer> levelSpinner = new Spinner<Integer>(0, 100000, 0, 1);
        levelSpinner.setEditable(true);

        Button drawRecDevMazeBtn = new Button("Draw Recursive Division Maze");
        Button backBtn = new Button("Back");
        FlowPane bottomPanel = new FlowPane(backBtn, drawRecDevMazeBtn);

        // drawRecDevMazeBtn.setOnAction(
        //     event -> RecursiveDivisionW.drawMaze(canvas, levelSpinner.getValue()));
        backBtn.setOnAction(
            event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        rootLayout.setCenter(scrollPane);
        rootLayout.setBottom(bottomPanel);

        Scene scene = new Scene(rootLayout, 300, 400);
        stage.setScene(scene);
        stage.setTitle("Fractals");
        stage.show();
    }
}
