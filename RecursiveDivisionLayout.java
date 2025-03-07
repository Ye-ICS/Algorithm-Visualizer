import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class RecursiveDivisionLayout extends FlowPane{
    RecursiveDivisionLayout(){
        BorderPane rootLayout = new BorderPane();

        Canvas canvas = new Canvas(500, 500);
        ScrollPane scrollPane = new ScrollPane(canvas);

        Spinner<Integer> levelSpinner = new Spinner<Integer>(0, 100000, 0, 1);
        levelSpinner.setEditable(true);

        Button drawRecDevMazeBtn = new Button("Draw Recursive Division Maze");
        drawRecDevMazeBtn.setOnAction(event -> RecursiveDivisionW.drawMaze(canvas.getGraphicsContext2D(), 500, 0, 600, levelSpinner.getValue()));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(backBtn);
    }
}
