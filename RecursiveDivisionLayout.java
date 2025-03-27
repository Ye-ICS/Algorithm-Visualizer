import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class RecursiveDivisionLayout extends FlowPane{

    RecursiveDivisionLayout() {
        BorderPane rootLayout = new BorderPane();

        Canvas canvas = new Canvas(500, 500);
        FlowPane canFlowPane = new FlowPane(canvas);
        canFlowPane.setAlignment(Pos.CENTER);

        Button drawRecDevMazeBtn = new Button("Draw Maze");
        Button backBtn = new Button("Back");

        // drawRecDevMazeBtn.setOnAction(
        //     event -> RecursiveDivisionW.drawMaze(canvas, levelSpinner.getValue()));
        backBtn.setOnAction(
            event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(rootLayout, canFlowPane, drawRecDevMazeBtn, backBtn);

    }
}
