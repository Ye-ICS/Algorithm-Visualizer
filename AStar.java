import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * A* Pathfinding Visualization UI.
 */
public class AStar extends BorderPane {
    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;
    private static int nodesExplored = 0;
    private static int pathLength = 0;
    private static long startTime;
    private static long endTime;

    private static Cell[][] grid = new Cell[GRID_SIZE][GRID_SIZE];
    private static Cell startNode = null;
    private static Cell endNode = null;
    private static Set<Cell> closedList = new HashSet<>();
    private static Set<Cell> currentPath = new HashSet<>();

    private static TextArea explanationPanel;
    private static GridPane threeByThreeGrid;
    private static TextArea infoPanel;
    private static VBox controlPanel;
    private static Slider speedSlider;
    private static double speed = 0.1; // Default speed
    private static HBox heuristic;
    private static int choice = 0;

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

        // Initialize the 3x3 currentNode grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane stackPane = new StackPane();
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE, Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                Text text = new Text();
                text.setFill(Color.WHITE);
                stackPane.getChildren().addAll(rect, text);
                currentNode.add(stackPane, col, row);
            }
        }

        Button aStarButton = new Button("Run A*");
        aStarButton.setPrefSize(300, 110);
        aStarButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        aStarButton.setTranslateY(2);

        Button clearButton = new Button("Clear Grid");
        Button backButton = new Button("Back");
        Button generateMaze = new Button("Generate Maze");

        HBox buttonbox = new HBox(10, clearButton, generateMaze);
        clearButton.setPrefWidth(90);
        generateMaze.setPrefWidth(90);


        Label title = new Label("A* Pathfinding Visualization");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setTextFill(Color.BLACK);

        VBox titleBox = new VBox(10, title);

        setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setPadding(new Insets(20, 0, 20, 20));

        

        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.setTranslateX(275);
        titleBox.setTranslateY(15);
        setTop(titleBox);

        // Create the Info Panel
        infoPanel = new TextArea();
        infoPanel.setEditable(false);
        infoPanel.setWrapText(true);
        infoPanel.setPrefSize(50, 75);
        infoPanel.setStyle("-fx-font-size: 14px;");

        // Create the Explanation Panel
        explanationPanel = new TextArea();
        explanationPanel.setEditable(false);
        explanationPanel.setWrapText(true);
        explanationPanel.setPrefSize(300, 100);
        explanationPanel.setStyle("-fx-font-size: 14px;");

        // Create the Speed Slider
        speedSlider = new Slider(0.01, 1.0, 0.1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.1);
        speedSlider.setMinorTickCount(4);
        speedSlider.setBlockIncrement(0.01);
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed = newValue.doubleValue();
        });

        // Create the Heuristic buttons
        Button manhattan = new Button("Manhattan");
        Button euclidean = new Button("Euclidean");
        Button chebyshev = new Button("Chebyshev");

        euclidean.setPrefWidth(90);
        manhattan.setPrefWidth(90);
        chebyshev.setPrefWidth(90);

        choice = 0; // Default choice is Manhattan

        manhattan.setOnAction(event -> {
            choice = 0;
            updateExplanation("Selected: Manhattan");
        });
        euclidean.setOnAction(event -> {
            choice = 1;
            updateExplanation("Selected: Euclidean");
        });
        chebyshev.setOnAction(event -> {
            choice = 2;
            updateExplanation("Selected: Chebyshev");
        });

        heuristic = new HBox(10, manhattan, euclidean, chebyshev);


        // Create a new 3x3 grid below the explanationPanel
        threeByThreeGrid = new GridPane();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane stackPane = new StackPane();
                Rectangle rect = new Rectangle(100, 100, Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                Text text = new Text();
                text.setFill(Color.BLACK);
                stackPane.getChildren().addAll(rect, text);
                threeByThreeGrid.add(stackPane, col, row);
            }
        }

        // Create labels for each component
        Label infoLabel = new Label("Info Panel:");
        Label explanationLabel = new Label("Explanation Panel:");
        Label gridLabel = new Label("Current Node:");
        Label buttonBoxLabel = new Label("Control Panel:");
        Label speedLabel = new Label("Speed Slider:");
        Label heuristicLabel = new Label("Heuristic:");
        Label pauseLabel = new Label("Pause Button:");

        // Create the right panel with the labels and components
        VBox rightPanel = new VBox(5, infoLabel, infoPanel, explanationLabel, explanationPanel, gridLabel,
                threeByThreeGrid, buttonBoxLabel, buttonbox, speedLabel, speedSlider, heuristicLabel, heuristic, aStarButton );
        rightPanel.setPadding(new Insets(20));
        setRight(rightPanel);

        // Back button returns to the main menu
        backButton.setOnAction(event -> {
            resetGrid();
            FXUtils.setSceneRoot(getScene(), new MenuLayout());
        });

        clearButton.setOnAction(event -> resetGrid());
        aStarButton.setOnAction(event -> {
            if (startNode == null || endNode == null) {
                updateExplanation("Please select a start and end node.");
                return;
            } else {
                resetAlgorithm();
                updateExplanation("Running A* Algorithm...");
                runAStar(startNode, endNode);
            }
        });
        generateMaze.setOnAction(event -> {
            resetGrid();
            updateExplanation("Generating Maze...");
            generateMaze();
        });

    }

    private static double heuristic(Cell a, Cell b) {
        switch (choice) {
            case 0:
                // Euclidean distance
                return Math.sqrt(Math.pow(a.row - b.row, 2) + Math.pow(a.col - b.col, 2));
            case 2:
                // Chebyshev distance
                return Math.max(Math.abs(a.row - b.row), Math.abs(a.col - b.col));
            default:
                // Manhattan distance
                return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
        }
    }

    public static void resetGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].reset();
            }
        }
        startNode = null;
        endNode = null;
        closedList.clear(); // Clear the closed list
        currentPath.clear(); // Clear the current path
        nodesExplored = 0;
        pathLength = 0;
        updateExplanation("Grid Cleared");
        updateInfoPanel();
    }

    public static void runAStar(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(
                Comparator.comparingDouble(c -> c.distance + heuristic(c, end)));
        Set<Cell> closedList = new HashSet<>();

        start.distance = 0;
        openList.add(start);

        startTime = System.currentTimeMillis();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), event -> {
            if (openList.isEmpty()) {
                updateExplanation("No path found.");
                endTime = System.currentTimeMillis();
                updateInfoPanel();
                timeline.stop();
                return;
            }

            Cell current = openList.poll();
            nodesExplored++;
            updateExplanation("Checking node (" + current.row + ", " + current.col + ")");

            if (current == end) {
                end.getRectangle().setFill(Color.RED); // Ensure end node stays red
                reconstructPath();
                endTime = System.currentTimeMillis();
                updateInfoPanel();
                timeline.stop();
                return;
            }

            closedList.add(current);
            if (current != start && current != end) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight current node
            }

            for (Cell neighbor : getNeighbors(current)) {
                if (closedList.contains(neighbor) || neighbor.isWall()) {
                    updateInfoPanel();
                    continue;
                }

                double tentativeG = current.distance + 1; // Assuming uniform cost for moving to a neighbor

                if (tentativeG < neighbor.distance) {
                    neighbor.distance = tentativeG;
                    neighbor.parent = current;
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                        updateExplanation("Adding node (" + neighbor.row + ", " + neighbor.col + ") to open list");
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
                updateThreeByThreeGrid(current); // Update the 3x3 grid
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
        for (Cell cell : currentPath) {
            if (cell != startNode && cell != endNode) {
                cell.getRectangle().setFill(Color.BLUE); // Reset path color
            }
        }
        currentPath.clear();

        // Rebuild the path
        Cell current = endNode;
        while (current != null) {
            if (current == endNode) {
                current.getRectangle().setFill(Color.RED); // Ensure end node is always red
            } else if (current != startNode) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight path
            }
            currentPath.add(current);
            current = current.parent;
            pathLength++;
        }
        updateThreeByThreeGrid(endNode); // Update the 3x3 grid with the final path
    }

    private static Set<Cell> getNeighbors(Cell cell) {
        Set<Cell> neighbors = new HashSet<>();
        int[] dRow = { -1, 0, 1, 0, -1, 1, -1, 1 };
        int[] dCol = { 0, -1, 0, 1, -1, -1, 1, 1 };

        for (int i = 0; i < 4; i++) {
            int newRow = cell.row + dRow[i];
            int newCol = cell.col + dCol[i];

            if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
                neighbors.add(grid[newRow][newCol]);
            }
        }

        return neighbors;
    }

    private static void updateThreeByThreeGrid(Cell current) {
        // Clear the 3x3 grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane stackPane = (StackPane) threeByThreeGrid.getChildren().get(row * 3 + col);
                Rectangle rect = (Rectangle) stackPane.getChildren().get(0);
                Text text = (Text) stackPane.getChildren().get(1);
                rect.setFill(Color.LIGHTGRAY);
                text.setText(""); // Clear the text
            }
        }

        // Update the 3x3 grid with the current node and its neighbors
        int[] dRow = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
        int[] dCol = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };

        for (int i = 0; i < 9; i++) {
            int newRow = current.row + dRow[i];
            int newCol = current.col + dCol[i];

            if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
                Cell neighbor = grid[newRow][newCol];
                StackPane stackPane = (StackPane) threeByThreeGrid.getChildren().get(i);
                Rectangle rect = (Rectangle) stackPane.getChildren().get(0);
                Text text = (Text) stackPane.getChildren().get(1);

                // Set the rectangle color based on the cell state
                rect.setFill(neighbor.getRectangle().getFill());

                // Only display the value if the node has been scanned (distance is not
                // Double.MAX_VALUE)
                if (neighbor.distance != Double.MAX_VALUE) {
                    double heuristicValue = heuristic(neighbor, endNode);
                    text.setText(String.format("%.1f", neighbor.distance + heuristicValue));
                } else {
                    text.setText(""); // Leave the text empty for unscanned nodes
                }
            }
        }
    }

    private static class Cell {
        private int row, col;
        private Rectangle rect;
        private boolean isWall = false;
        private double distance = Double.MAX_VALUE;
        private Cell parent = null;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            this.rect = new Rectangle(CELL_SIZE, CELL_SIZE, Color.LIGHTGRAY);
            this.rect.setStroke(Color.BLACK);

            rect.setOnMouseClicked(event -> cellClicked());
        }

        public Rectangle getRectangle() {
            return rect;
        }

        public boolean isWall() {
            return isWall;
        }

        public void setWall(boolean wall) {
            isWall = wall;
            if (wall) {
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

    private static void updateExplanation(String message) {
        Platform.runLater(() -> explanationPanel.appendText(message + "\n"));
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

    private static void updateInfoPanel() {
        Platform.runLater(() -> {
            infoPanel.setText("Nodes Explored: " + nodesExplored + "\n");
            infoPanel.appendText("Path Length: " + pathLength + "\n");
            infoPanel.appendText("Execution Time: " + (endTime - startTime) + " ms\n");
        });
    }

    private static void resetAlgorithm() {
        closedList.clear(); // Clear the closed list
        currentPath.clear(); // Clear the current path
        nodesExplored = 0;
        pathLength = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].distance = Double.MAX_VALUE;
                grid[row][col].parent = null;
                if (!grid[row][col].isWall() && grid[row][col] != startNode && grid[row][col] != endNode) {
                    grid[row][col].getRectangle().setFill(Color.LIGHTGRAY);
                }
            }
        }
        updateInfoPanel();
    }
}