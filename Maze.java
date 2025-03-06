import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class Maze extends FlowPane {
    Maze(Stage stage) {
        setAlignment(Pos.CENTER);
        Canvas canvas = new Canvas(500, 500);
        ScrollPane scrollPane = new ScrollPane(canvas);

        Button backBtn = new Button("Back");
        Button runBtn = new Button("run");
        
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        runBtn.setOnAction(event -> Maze.drawSquare(canvas.getGraphicsContext2D(), 0, 0, 100, 100));


        getChildren().addAll(backBtn, runBtn);
        
        
    }
    static void drawSquare(GraphicsContext graphicsContext, double x, double y, double w, double h) {
        graphicsContext.fillRect(x, y, w, h);
    }
}
