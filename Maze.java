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
        int nodeSize = 25;
        int[] x = new int[mazeSize/nodeSize];
        int[] y = new int[mazeSize/nodeSize];
        int[] xArch = new int[5000];
        int[] yArch = new int[5000];
        int steps = 0;
        int back = 0;
        
        for (int i = 0; i < mazeSize/nodeSize; i++) {
            x[i] = i;
            y[i] = i;
        }

        boolean [][] path = new boolean[x.length][y.length];
        boolean [][] wallA = new boolean[x.length][y.length];
        boolean [][] wallB = new boolean[x.length][y.length];
        
        for (int i = 0; i < wallB.length; i++) {
            wallB[0][i] = true;
            wallB[i][0] = true;
            wallB[mazeSize/nodeSize - 1][i] = true;
            wallB[i][y.length - 1] = true;
        }

        wallB[0][1] = false;
        path[0][1] = true;
        int posX = 0;
        int posY = 1;
        boolean complete = false;
        
        setCenter(stack);
        setBottom(bottomPannel);
        
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        runBtn.setOnAction(event -> Maze.run(canvas.getGraphicsContext2D(), nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, mazeSize, mazeSize);
        
    }
    
    static void run(GraphicsContext graphicsContext, int nodeSize, int mazeSize, int[] x, int[] y, boolean[][] path, boolean[][] wallA, boolean[][] wallB, int posX, int posY, boolean complete, int[] xArch, int[] yArch, int steps, int back) {     
              
        
        graphicsContext.clearRect(x[posX]*nodeSize, y[posY]*nodeSize, nodeSize, nodeSize);
       
        if (posX == 0 && posY == 1) {
            xArch[steps] = posX;
            yArch[steps] = posY;
            posX ++;
            graphicsContext.clearRect(x[posX]*nodeSize, y[posY]*nodeSize, nodeSize, nodeSize);
        }

        int direction = 0 + (int) (Math.random() * (3 - 0 + 1));

        if (wallB[posX + 1][posY] == true || path[posX + 1][posY] == true) {
            if (wallB[posX - 1][posY] == true || path[posX - 1][posY] == true) {
                if (wallB[posX][posY + 1] == true || path[posX][posY + 1] == true) {
                    if (wallB[posX][posY - 1] == true || path[posX][posY - 1] == true) {
                        back --;
                        posX = xArch[back];
                        posY = yArch[back];
                        run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
                    }
                }
            }
        }
       
        if (direction == 0 && wallB[posX + 1][posY] == false && path[posX + 1][posY] == false) {
            
            path[posX][posY] = true;
            steps ++;
            back = steps;
            xArch[steps] = posX;
            yArch[steps] = posY;
            posX ++;
            
            if (wallA[posX][posY + 1] == true) {
                wallB[posX][posY + 1] = true;
            }  
            if (wallA[posX][posY - 1] == true) {
                wallB[posX][posY - 1] = true;
            }
            if (wallA[posX + 1][posY] == true) {
                wallB[posX + 1][posY] = true;
            }
            if (wallA[posX + 1][posY - 1] == true) {
                wallB[posX + 1][posY - 1] = true;
            }
            if (wallA[posX + 1][posY + 1] == true) {
                wallB[posX + 1][posY + 1] = true;
            }
            
            wallA[posX][posY + 1] = true;
            wallA[posX][posY - 1] = true;
            wallA[posX + 1][posY] = true;

            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
        
        } else if (direction == 1 && wallB[posX][posY + 1] == false && path[posX][posY + 1] == false) {
          
            path[posX][posY] = true;
            steps ++;
            back = steps;
            xArch[steps] = posX;
            yArch[steps] = posY;
            posY ++;

            if (wallA[posX + 1][posY] == true) {
                wallB[posX + 1][posY] = true;
            }  
            if (wallA[posX - 1][posY] == true) {
                wallB[posX - 1][posY] = true;
            }
            if (wallA[posX][posY + 1] == true) {
                wallB[posX][posY + 1] = true;
            }
            if (wallA[posX + 1][posY + 1] == true) {
                wallB[posX + 1][posY + 1] = true;
            }
            if (wallA[posX - 1][posY + 1] == true) {
                wallB[posX - 1][posY + 1] = true;
            }

            wallA[posX + 1][posY] = true;
            wallA[posX - 1][posY] = true;
            wallA[posX][posY + 1] = true;

            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
     
        } else if (direction == 2 && wallB[posX - 1][posY] == false && path[posX - 1][posY] == false) {
           
            path[posX][posY] = true;
            steps ++;
            back = steps;
            xArch[steps] = posX;
            yArch[steps] = posY;
            posX --;

            if (wallA[posX][posY + 1] == true) {
                wallB[posX][posY + 1] = true;
            }  
            if (wallA[posX][posY - 1] == true) {
                wallB[posX][posY - 1] = true;
            }
            if (wallA[posX - 1][posY] == true) {
                wallB[posX - 1][posY] = true;
            }
            if (wallA[posX - 1][posY - 1] == true) {
                wallB[posX - 1][posY - 1] = true;
            }
            if (wallA[posX - 1][posY + 1] == true) {
                wallB[posX - 1][posY + 1] = true;
            }

            wallA[posX][posY + 1] = true;
            wallA[posX][posY - 1] = true;
            wallA[posX - 1][posY] = true;

            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
      
        } else if (direction == 3 && wallB[posX][posY - 1] == false && path[posX][posY - 1] == false) {
           
            path[posX][posY] = true;
            steps ++;
            back = steps;
            xArch[steps] = posX;
            yArch[steps] = posY;
            posY --;

            if (wallA[posX - 1][posY] == true) {
                wallB[posX - 1][posY] = true;
            }  
            if (wallA[posX + 1][posY] == true) {
                wallB[posX + 1][posY] = true;
            }
            if (wallA[posX][posY - 1] == true) {
                wallB[posX][posY - 1] = true;
            }
            if (wallA[posX + 1][posY - 1] == true) {
                wallB[posX + 1][posY - 1] = true;
            }
            if (wallA[posX - 1][posY - 1] == true) {
                wallB[posX - 1][posY - 1] = true;
            }
            
            wallA[posX - 1][posY] = true;
            wallA[posX + 1][posY] = true;
            wallA[posX][posY - 1] = true;
            
            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
        
        } else if (posX == 0 && posY == 1) {

            return;

        } else {
            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, posX, posY, complete, xArch, yArch, steps, back);
        }
        
        
    }
}
