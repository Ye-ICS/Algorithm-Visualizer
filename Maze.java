import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class Maze extends BorderPane {
     Maze() {
        
        Canvas canvas = new Canvas(500, 500);
        StackPane stack = new StackPane(canvas);
        
        Button backBtn = new Button("Back");
        Button runBtn = new Button("run");
        FlowPane bottomPannel = new FlowPane(backBtn, runBtn);
        int mazeSize = 500;
        
        setCenter(stack);
        setBottom(bottomPannel);
        
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        runBtn.setOnAction(event -> Maze.run(canvas.getGraphicsContext2D(), 25, mazeSize, 100, 100));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, mazeSize, mazeSize);
        
    }
    
    static void run(GraphicsContext graphicsContext, int nodeSize, int mazeSize, double w, double h) {
        
        int[] x = new int[mazeSize/nodeSize];
        int[] y = new int[mazeSize/nodeSize];
        
        for (int i = 0; i < mazeSize/nodeSize; i++) {
            x[i] = i;
            y[i] = i;
        }

        boolean [][] path = new boolean[x.length][y.length];
        boolean [][] wall = new boolean[x.length][y.length];
        
        for (int i = 0; i < wall.length; i++) {
            wall[0][i] = true;
            wall[i][0] = true;
            wall[mazeSize/nodeSize - 1][i] = true;
            wall[i][y.length - 1] = true;
        }

        wall[0][1] = false;
        path[0][1] = true;
        int posX = 0;
        int posY = 1;
        boolean complete = false;
        int direction;

        graphicsContext.clearRect(x[0]*nodeSize, y[1]*nodeSize, nodeSize, nodeSize);

        if (posX == 0 && posY == 1) {
            posX ++;
        }
        while (complete == false) {

            direction = 0 + (int) (Math.random() * (3 - 0 + 1));
            graphicsContext.clearRect(x[posX]*nodeSize, y[posY]*nodeSize, nodeSize, nodeSize);
            if (direction == 0 && wall[posX + 1][posY] == false && path[posX + 1][posY] == false) {
                posX ++;
                path[posX][posY] = true;
                wall[posX - 1][posY + 1] = true;
                wall[posX - 1][posY - 1] = true;
            } else if (direction == 1 && wall[posX][posY + 1] == false && path[posX][posY + 1] == false) {
                posY ++;
                path[posX][posY] = true;
                wall[posX + 1][posY - 1] = true;
                wall[posX - 1][posY - 1] = true;
            } else if (direction == 2 && wall[posX - 1][posY] == false && path[posX - 1][posY] == false) {
                posX --;
                path[posX][posY] = true;
                wall[posX + 1][posY + 1] = true;
                wall[posX + 1][posY - 1] = true;
            } else if (direction == 3 && wall[posX][posY - 1] == false && path[posX][posY - 1] == false) {
                posY --;
                path[posX][posY] = true;
                wall[posX - 1][posY + 1] = true;
                wall[posX + 1][posY + 1] = true;
            }
            if (posX == 0 && posY == 1) {
                complete = true;
            }
            
        }





    }
}
