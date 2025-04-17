import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class RecursiveDivisionLayout extends FlowPane {

    RecursiveDivisionLayout() {

        Canvas canvas = new Canvas(500, 500);
        FlowPane canFlowPane = new FlowPane(canvas);
        canFlowPane.setAlignment(Pos.CENTER);

        Button drawMazeBtn = new Button("Draw Maze");
        Button backBtn = new Button("Back");

        drawMazeBtn.setOnAction(
                event -> drawALine(canvas.getGraphicsContext2D()));

        // backBtn.setOnAction(
        //         event -> backBtn.setOnAction(FXUtils.setSceneRoot(getScene(), new MenuLayout())));

        getChildren().addAll(canFlowPane, drawMazeBtn, backBtn);

    }

    void drawALine(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, 0, 300, 300);
        gc.fillRect(0, 300, 300, 300);
    }
}
