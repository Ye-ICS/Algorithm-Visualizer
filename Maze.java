import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Maze extends BorderPane {
    Maze() {

        int mazeSize = 800;
        Canvas canvas = new Canvas(mazeSize, mazeSize);
        StackPane stack = new StackPane(canvas);

        Button backBtn = new Button("Back");
        Button runBtn = new Button("run");
        FlowPane topPannel = new FlowPane(backBtn, runBtn);
        
        int nodeSize = 25;
        int[] x = new int[mazeSize / nodeSize];
        int[] y = new int[mazeSize / nodeSize];
        int[] xArch = new int[(mazeSize / nodeSize) * (mazeSize / nodeSize)];
        int[] yArch = new int[(mazeSize / nodeSize) * (mazeSize / nodeSize)];
        int steps = 0;
        int back = 0;
        boolean started = false;

        for (int i = 0; i < mazeSize / nodeSize; i++) {
            x[i] = i;
            y[i] = i;
        }

        boolean[][] path = new boolean[x.length][y.length];
        boolean[][] wallA = new boolean[x.length][y.length];
        boolean[][] wallB = new boolean[x.length][y.length];
        boolean[][] wallC = new boolean[x.length][y.length];

        for (int i = 0; i < wallB.length; i++) {
            wallB[0][i] = true;
            wallB[i][0] = true;
            wallB[x.length - 1][i] = true;
            wallB[i][y.length - 1] = true;
        }

        wallB[0][1] = false;
        path[0][1] = true;
        int posX = 0;
        int posY = 1;
        boolean complete = false;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, mazeSize, mazeSize);
        if (posX == 0 && posY == 1) {
            xArch[steps] = posX;
            yArch[steps] = posY;
            wallA[1][0] = true;
            wallA[1][2] = true;
            wallA[2][1] = true;
            wallC[2][0] = true;
            wallC[2][2] = true;
            xArch[steps + 1] = posX + 1;
            yArch[steps + 1] = posY;
            
            gc.clearRect(x[posX] * nodeSize, y[posY] * nodeSize, nodeSize, nodeSize);
        }

        setCenter(stack);
        setTop(topPannel);

        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        runBtn.setOnAction(event -> {
            var executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                Maze.run(canvas.getGraphicsContext2D(), nodeSize, mazeSize, x, y, path, wallA,
                wallB, wallC, posX + 1, posY, complete, xArch, yArch, steps + 1, back, started);
            });
        });

        

    }

    static void run(GraphicsContext graphicsContext, int nodeSize, int mazeSize, int[] x, int[] y, boolean[][] path,
            boolean[][] wallA, boolean[][] wallB, boolean[][] wallC, int posX, int posY, boolean complete, int[] xArch, int[] yArch,
            int steps, int back, boolean started) {

                
        
            // Background work (calculating next r)

            // Update JavaFX
                // final value error
            int posX_ = posX;
            int posY_ = posY;

            Platform.runLater(() -> {
                
                graphicsContext.clearRect(x[posX_] * nodeSize, y[posY_] * nodeSize, nodeSize, nodeSize);
            }); 

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            

            int direction = 0 + (int) (Math.random() * (3 - 0 + 1));
            if (posX == 1 && posY == 1 && started == true) {
                if (path[x.length - 2][y.length - 2] == true) {
                    graphicsContext.clearRect(x[x.length - 1] * nodeSize, y[y.length - 2] * nodeSize, nodeSize, nodeSize);
                    return;
                } else if (path[x.length - 3][y.length - 2] == true) {
                    graphicsContext.clearRect(x[x.length - 3] * nodeSize, y[y.length - 1] * nodeSize, nodeSize, nodeSize);
                    return;
                } else if (path[x.length - 2][y.length - 3] == true) {
                    graphicsContext.clearRect(x[x.length - 1] * nodeSize, y[y.length - 3] * nodeSize, nodeSize, nodeSize);
                    return;
                }
            }

            if (wallB[posX + 1][posY] == true || path[posX + 1][posY] == true || wallB[posX + 1][posY] == true && path[posX + 1][posY] == true) {
                if (wallB[posX - 1][posY] == true || path[posX - 1][posY] == true || wallB[posX - 1][posY] == true && path[posX - 1][posY] == true) {
                    if (wallB[posX][posY + 1] == true || path[posX][posY + 1] == true || wallB[posX][posY + 1] == true && path[posX][posY + 1] == true) {
                        if (wallB[posX][posY - 1] == true || path[posX][posY - 1] == true || wallB[posX][posY - 1] == true && path[posX][posY - 1] == true) {
                            steps--;
                            posX = xArch[steps - 1];
                            posY = yArch[steps - 1];
                            //graphicsContext.strokeLine(x[posX_] * nodeSize, y[posY_] * nodeSize, x[posX_]* nodeSize + nodeSize, y[posY]* nodeSize + nodeSize);
                            run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete,
                                    xArch, yArch, steps, back, started);
                        }
                    }
                }
            }

            if (direction == 0 && wallB[posX + 1][posY] == false && path[posX + 1][posY] == false) {

                path[posX][posY] = true;
                
                posX++;
                steps++;
                back = steps - 1;
                xArch[steps] = posX;
                yArch[steps] = posY;
                path[posX][posY] = true;

                if (wallA[posX][posY + 1] == true) {
                    wallB[posX][posY + 1] = true;
                }
                if (wallA[posX][posY - 1] == true) {
                    wallB[posX][posY - 1] = true;
                }
                if (wallA[posX + 1][posY] == true || wallC[posX + 1][posY] == true) {
                    wallB[posX + 1][posY] = true;
                }
                if (wallA[posX + 1][posY - 1] == true || wallC[posX + 1][posY - 1] == true) {
                    wallB[posX + 1][posY - 1] = true;
                }
                if (wallA[posX + 1][posY + 1] == true || wallC[posX + 1][posY + 1] == true) {
                    wallB[posX + 1][posY + 1] = true;
                }

                wallA[posX][posY + 1] = true;
                wallA[posX][posY - 1] = true;
                wallA[posX + 1][posY] = true;
                wallC[posX + 1][posY - 1] = true;
                wallC[posX + 1][posY + 1] = true;
                started = true;

                run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete, xArch, yArch,
                        steps, back, started);

            } else if (direction == 1 && wallB[posX][posY + 1] == false && path[posX][posY + 1] == false) {

                path[posX][posY] = true;
                
                posY++;
                steps++;
                back = steps - 1;
                xArch[steps] = posX;
                yArch[steps] = posY;
                path[posX][posY] = true;

                if (wallA[posX + 1][posY] == true) {
                    wallB[posX + 1][posY] = true;
                }
                if (wallA[posX - 1][posY] == true) {
                    wallB[posX - 1][posY] = true;
                }
                if (wallA[posX][posY + 1] == true || wallC[posX][posY + 1] == true) {
                    wallB[posX][posY + 1] = true;
                }
                if (wallA[posX + 1][posY + 1] == true || wallC[posX + 1][posY + 1] == true) {
                    wallB[posX + 1][posY + 1] = true;
                }
                if (wallA[posX - 1][posY + 1] == true || wallC[posX - 1][posY + 1] == true) {
                    wallB[posX - 1][posY + 1] = true;
                }

                wallA[posX + 1][posY] = true;
                wallA[posX - 1][posY] = true;
                wallA[posX][posY + 1] = true;
                wallC[posX - 1][posY + 1] = true;
                wallC[posX + 1][posY + 1] = true;

                run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete, xArch, yArch,
                        steps, back, started);

            } else if (direction == 2 && wallB[posX - 1][posY] == false && path[posX - 1][posY] == false) {

                path[posX][posY] = true;
                
                posX--;
                steps++;
                back = steps - 1;
                xArch[steps] = posX;
                yArch[steps] = posY;
                path[posX][posY] = true;

                if (wallA[posX][posY + 1] == true) {
                    wallB[posX][posY + 1] = true;
                }
                if (wallA[posX][posY - 1] == true) {
                    wallB[posX][posY - 1] = true;
                }
                if (wallA[posX - 1][posY] == true || wallC[posX - 1][posY] == true) {
                    wallB[posX - 1][posY] = true;
                }
                if (wallA[posX - 1][posY - 1] == true || wallC[posX - 1][posY - 1] == true) {
                    wallB[posX - 1][posY - 1] = true;
                }
                if (wallA[posX - 1][posY + 1] == true || wallC[posX - 1][posY + 1] == true) {
                    wallB[posX - 1][posY + 1] = true;
                }

                wallA[posX][posY + 1] = true;
                wallA[posX][posY - 1] = true;
                wallA[posX - 1][posY] = true;
                wallC[posX - 1][posY + 1] = true;
                wallC[posX - 1][posY - 1] = true;

                run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete, xArch, yArch,
                        steps, back, started);

            } else if (direction == 3 && wallB[posX][posY - 1] == false && path[posX][posY - 1] == false) {

                path[posX][posY] = true;
                
                posY--;
                steps++;
                back = steps - 1;
                xArch[steps] = posX;
                yArch[steps] = posY;
                path[posX][posY] = true;

                if (wallA[posX - 1][posY] == true) {
                    wallB[posX - 1][posY] = true;
                }
                if (wallA[posX + 1][posY] == true) {
                    wallB[posX + 1][posY] = true;
                }
                if (wallA[posX][posY - 1] == true || wallC[posX][posY - 1] == true) {
                    wallB[posX][posY - 1] = true;
                }
                if (wallA[posX + 1][posY - 1] == true || wallC[posX + 1][posY - 1] == true) {
                    wallB[posX + 1][posY - 1] = true;
                }
                if (wallA[posX - 1][posY - 1] == true || wallC[posX - 1][posY - 1] == true) {
                    wallB[posX - 1][posY - 1] = true;
                }

                wallA[posX - 1][posY] = true;
                wallA[posX + 1][posY] = true;
                wallA[posX][posY - 1] = true;
                wallC[posX - 1][posY - 1] = true;
                wallC[posX + 1][posY - 1] = true;

                run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete, xArch, yArch,
                        steps, back, started);

            } 
                

             else {
                run(graphicsContext, nodeSize, mazeSize, x, y, path, wallA, wallB, wallC, posX, posY, complete, xArch, yArch,
                        steps, back, started);
            }
        

        

    }
}

