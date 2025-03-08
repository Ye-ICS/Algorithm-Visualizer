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

        Label totalCost = new Label("test");

        HBox buttonBox = new HBox(10, aStarButton, clearButton, backButton, generateMaze);
        VBox infoBox = new VBox(10, totalCost);

        setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);

        setBottom(buttonBox);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        infoBox.setAlignment(Pos.TOP_RIGHT);

        setTop(infoBox);

        // Back button returns to the main menu
        backButton.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
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
        System.out.println("Grid Cleared");
    }

    public static void runAStar(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(Comparator.comparingDouble(c -> c.distance + heuristic(c, end)));
        Set<Cell> closedList = new HashSet<>();

        start.distance = 0;
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();
            if (current == end) {
                reconstructPath();
                return;
            }

            closedList.add(current);

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
                    }
                }
            }
        }

        System.out.println("No path found.");
    }

    private static void reconstructPath() {
        Cell current = endNode;
        while (current != null) {
            current.getRectangle().setFill(Color.YELLOW); // Highlight the path
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