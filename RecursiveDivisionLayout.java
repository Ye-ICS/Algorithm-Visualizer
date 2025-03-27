import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class RecursiveDivisionLayout extends FlowPane{

    RecursiveDivisionLayout() {
        BorderPane rootLayout = new BorderPane();

        Canvas canvas = new Canvas(500, 500);
        ScrollPane scrollPane = new ScrollPane(canvas);

        Button drawRecDevMazeBtn = new Button("Draw Recursive Division Maze");
        Button backBtn = new Button("Back");
        FlowPane bottomPanel = new FlowPane(backBtn, drawRecDevMazeBtn);

        // drawRecDevMazeBtn.setOnAction(
        //     event -> RecursiveDivisionW.drawMaze(canvas, levelSpinner.getValue()));
        backBtn.setOnAction(
            event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        rootLayout.setCenter(scrollPane);
        rootLayout.setBottom(bottomPanel);

    }
}
