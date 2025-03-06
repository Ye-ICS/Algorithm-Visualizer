import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class Maze extends BorderPane {
     Maze() {
        Canvas canvas = new Canvas(500, 500);
        
        
        Button backBtn = new Button("Back");
        Button runBtn = new Button("run");
        
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        runBtn.setOnAction(event -> Maze.drawSquare(canvas.getGraphicsContext2D(), 0, 0, 100, 100));
        Text title = new Text("Algorithm Visualizer");
        title.setFont(Font.font(24));
        getChildren().addAll(title);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeLine(0, 0, 100, 100);

        getChildren().addAll(backBtn, runBtn);
        
    }
    
    static void drawSquare(GraphicsContext graphicsContext, double x, double y, double w, double h) {
        
    }
}
