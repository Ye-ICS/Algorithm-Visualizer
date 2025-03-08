import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.geometry.Insets;

/**
 * A* Pathfinding Visualization UI.
 */
public class AStar extends BorderPane {
    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;

    private static Cell[][] grid = new Cell[GRID_SIZE][GRID_SIZE];
    private static Cell startNode = null;
    private static Cell endNode = null;
    private static Set<Cell> closedList = new HashSet<>();
    private static Set<Cell> currentPath = new HashSet<>();

    public AStar() {
        GridPane gridPane = new GridPane();
        GridPane currentNode = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Cell cell = new Cell(row, col);
                grid[row][col] = cell;
                gridPane.add(cell.getRectangle(), col, row);
            }
        }

        Button aStarButton = new Button("Run A*");
        Button clearButton = new Button("Clear Grid");
        Button backButton = new Button("Back");
        Button generateMaze = new Button("Generate Maze");

        Label title = new Label("A* Pathfinding Visualization");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setTextFill(Color.BLACK);

        HBox buttonBox = new HBox(10, aStarButton, clearButton, backButton, generateMaze);
        VBox titleBox = new VBox(10, title);

        setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setPadding(new Insets(20, 0, 20, 20));

        setBottom(buttonBox);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setTranslateX(275);
        buttonBox.setTranslateY(-40);

        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.setTranslateX(275);
        titleBox.setTranslateY(40);
        setTop(titleBox);

        // Back button returns to the main menu
        backButton.setOnAction(event -> {
            resetGrid();
            FXUtils.setSceneRoot(getScene(), new MenuLayout());
        });

        clearButton.setOnAction(event -> resetGrid());
        aStarButton.setOnAction(event -> {
            if (startNode == null || endNode == null) {
                System.out.println("Please select a start and end node.");
                return;
            } else {
                System.out.println("Running A*");
                runAStar(startNode, endNode);
            }
        });
        generateMaze.setOnAction(event -> {
            resetGrid();
            System.out.println("Generating Maze");
            generateMaze();
        });

    } 
    
    private static double heuristic(Cell a, Cell b) {
        // Use Manhattan distance for grid-based pathfinding
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    public static void resetGrid(){
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].reset();
            }
        }
        startNode = null;
        endNode = null;
        closedList.clear(); // Clear the closed list
        currentPath.clear(); // Clear the current path
        System.out.println("Grid Cleared");
    }

    public static void runAStar(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(Comparator.comparingDouble(c -> c.distance + heuristic(c, end)));
        Set<Cell> closedList = new HashSet<>();

        start.distance = 0;
        openList.add(start);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), event -> {
            if (openList.isEmpty()) {
                System.out.println("No path found.");
                timeline.stop();
                return;
            }

            Cell current = openList.poll();
            if (current == end) {
                reconstructPath();
                timeline.stop();
                return;
            }

            closedList.add(current);
            if (current != start && current != end) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight current node
            }

            for (Cell neighbor : getNeighbors(current)) {
                if (closedList.contains(neighbor) || neighbor.isWall()) {
                    continue;
                }

                double tentativeG = current.distance + 1; // Assuming uniform cost for moving to a neighbor

                if (tentativeG < neighbor.distance) {
                    neighbor.distance = tentativeG;
                    neighbor.parent = current;
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                        if (neighbor != start && neighbor != end) {
                            neighbor.getRectangle().setFill(Color.BLUE); // Highlight open list nodes
                        }
                    }
                }
            }

            Platform.runLater(() -> {
                if (current != start && current != end) {
                    current.getRectangle().setFill(Color.BLUE); // Mark as visited
                }
                highlightPath(current); // Highlight the path from the current node to the start node
            });
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private static void highlightPath(Cell current) {
        // Clear the previous path
        for (Cell cell : currentPath) {
            if (cell != startNode && cell != endNode) {
                cell.getRectangle().setFill(Color.BLUE);
            }
        }
        currentPath.clear();

        // Highlight the new path
        while (current != null) {
            if (current != startNode && current != endNode) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight the path
                currentPath.add(current);
            }
            current = current.parent;
        }
    }

    private static void reconstructPath() {
        // Clear the previous path
        for (Cell cell : currentPath) {
            if (cell != startNode && cell != endNode) {
                cell.getRectangle().setFill(Color.BLUE);
            }
        }
        currentPath.clear();

        // Highlight the final path
        Cell current = endNode;
        while (current != null) {
            if (current != startNode && current != endNode) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight the path
                currentPath.add(current);
            }
            current = current.parent;
        }
    }

    private static Set<Cell> getNeighbors(Cell cell) {
        Set<Cell> neighbors = new HashSet<>();
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = cell.row + dRow[i];
            int newCol = cell.col + dCol[i];

            if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
                neighbors.add(grid[newRow][newCol]);
            }
        }

        return neighbors;
    }

    private class Cell {
        private int row, col;
        private Rectangle rect;
        private boolean isWall = false;
        private double distance = Double.MAX_VALUE;
        private Cell parent = null;

        public Cell(int row, int col){
            this.row = row;
            this.col = col;
            this.rect = new Rectangle(CELL_SIZE, CELL_SIZE, Color.LIGHTGRAY);
            this.rect.setStroke(Color.BLACK);

            rect.setOnMouseClicked(event -> cellClicked());
        }

        public Rectangle getRectangle(){
            return rect;
        }

        public boolean isWall(){
            return isWall;
        }

        public void setWall(boolean wall){
            isWall = wall;
            if (wall){
                rect.setFill(Color.BLACK);
            } else {
                rect.setFill(Color.LIGHTGRAY);
            }
        }

        public void reset() {
            isWall = false;
            rect.setFill(Color.LIGHTGRAY);
            distance = Double.MAX_VALUE;
            parent = null;
        }

        private void cellClicked() {
            if (startNode == null) {
                startNode = this;
                rect.setFill(Color.GREEN);
            } else if (endNode == null) {
                endNode = this;
                rect.setFill(Color.RED);
            } else {
                setWall(!isWall);
            }
        }
    }

    private void generateMaze() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].reset();
                if (Math.random() < 0.3) { // 30% chance to be a wall
                    grid[row][col].setWall(true);
                }
            }
        }
        if (startNode != null)
            startNode.setWall(false);
        if (endNode != null)
            endNode.setWall(false);
    }
}








