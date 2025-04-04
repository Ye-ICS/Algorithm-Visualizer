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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.ProgressBar;
import java.util.List;
import java.util.ArrayList;

/**
 * A* Pathfinding Visualization UI with Timeline Slider.
 */

public class AStar extends BorderPane {
    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;
    private static int nodesExplored = 0;
    private static int pathLength = 0;
    private static long startTime;
    private static long endTime;
    private static ProgressBar progressBar;
    private static List<Cell> stages = new ArrayList<>();

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
    private static VBox playback;
    private static boolean paused = false; // Move paused to class level
    private static Timeline currentTimeline; // Add this field at the class level

    // New fields for algorithm progress tracking
    private static List<AlgorithmStep> algorithmSteps = new ArrayList<>();
    private static int currentStepIndex = 0;
    private static Slider progressSlider;
    private static boolean userAdjustingSlider = false;
    private static int totalSteps = 0;
    
    public AStar() {
        paused = false; // Initialize paused in the constructor
        
        // Add this line right at the start of the constructor
        Platform.runLater(() -> {
            if (getScene() != null && getScene().getWindow() instanceof javafx.stage.Stage) {
                ((javafx.stage.Stage) getScene().getWindow()).setResizable(false);
            }
        });

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
        Button pauseBtn = new Button("Pause");
        
        Button aStarButton = new Button("Run A*");
        aStarButton.setPrefSize(300, 110);
        aStarButton.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");
        aStarButton.setTranslateY(2);

        Button clearButton = new Button("Clear Grid");
        Button backButton = new Button("Back");
        Button generateMaze = new Button("Generate Maze");

        HBox buttonbox = new HBox(10, clearButton, generateMaze, pauseBtn);
        clearButton.setPrefWidth(90);
        generateMaze.setPrefWidth(90);
        pauseBtn.setPrefWidth(90);

        Label title = new Label("A* Pathfinding Visualization");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setTextFill(Color.BLACK);

        VBox titleBox = new VBox(10, title);

        // Create progress slider that spans underneath the grid
        progressSlider = new Slider(0, 100, 0);
        progressSlider.setPrefWidth(GRID_SIZE * CELL_SIZE); // Match grid width
        progressSlider.setShowTickLabels(true);
        progressSlider.setShowTickMarks(true);
        progressSlider.setMajorTickUnit(25);
        progressSlider.setMinorTickCount(5);
        progressSlider.setDisable(true); // Initially disabled
        
        // Listen for user-driven changes to the slider
        progressSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (progressSlider.isValueChanging()) {
                    userAdjustingSlider = true;
                    int step = (int)((newValue.doubleValue() / 100) * (algorithmSteps.size() - 1));
                    if (step >= 0 && step < algorithmSteps.size()) {
                        jumpToStep(step);
                        
                    }
                } else if (userAdjustingSlider) {
                    userAdjustingSlider = false;
                }
            }
        });
        
        // Create a container for the progress controls
        HBox progressControls = new HBox(10, progressSlider);
        progressControls.setAlignment(Pos.CENTER);
        progressControls.setPadding(new Insets(10, 20, 0, 20));
        
        // Add grid and progress controls to a vertical layout
        VBox gridWithControls = new VBox(10);
        gridWithControls.getChildren().addAll(gridPane, progressControls);
        gridWithControls.setAlignment(Pos.CENTER_LEFT);
        gridWithControls.setPadding(new Insets(20, 0, 20, 20));
        
        setCenter(gridWithControls);

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
                threeByThreeGrid, buttonBoxLabel, buttonbox, speedLabel, speedSlider, heuristicLabel, heuristic,
                aStarButton);
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
                pauseBtn.setDisable(false); // Enable the pause button
                pauseBtn.setText("Pause"); // Ensure the pause button text is set to "Pause"
                
                // Reset and enable the progress slider
                progressSlider.setValue(0);
                progressSlider.setDisable(false);
                runAStar(startNode, endNode, pauseBtn);
            }
        });
        generateMaze.setOnAction(event -> {
            resetGrid();
            updateExplanation("Generating Maze...");
            generateMaze();
        });
    }

    private static double heuristic(Cell a, Cell b) {
        if (choice == 1){
            // Euclidean distance
                return Math.sqrt(Math.pow(a.row - b.row, 2) + Math.pow(a.col - b.col, 2));
        } else if (choice == 2){
            // Chebyshev distance
                return Math.max(Math.abs(a.row - b.row), Math.abs(a.col - b.col));
        } else if (choice == 0){
            // Manhattan distance
            return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
        }
    return choice;
    }

    public static void resetGrid() {
        // Stop any running animation first
        if (currentTimeline != null) {
            currentTimeline.stop();
        }
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].reset();
            }
        }
        startNode = null;
        endNode = null;
        closedList.clear();
        currentPath.clear();
        nodesExplored = 0;
        pathLength = 0;
        paused = false;  // Reset pause state
        algorithmSteps.clear(); // Clear algorithm steps
        currentStepIndex = 0;
        progressSlider.setValue(0);
        progressSlider.setDisable(true); // Ensure slider is disabled when grid is reset
        updateExplanation("Grid Cleared");
        updateInfoPanel();
    }

    /**
     * Class to store the state of the algorithm at each step
     */
    private static class AlgorithmStep {
        private Cell currentNode;
        private Set<Cell> openList;
        private Set<Cell> closedList;
        private String message;
        private boolean isPathFound;
        
        public AlgorithmStep(Cell currentNode, Set<Cell> openList, Set<Cell> closedList, String message, boolean isPathFound) {
            this.currentNode = currentNode;
            this.openList = new HashSet<>(openList);
            this.closedList = new HashSet<>(closedList);
            this.message = message;
            this.isPathFound = isPathFound;
        }
    }

    private static void jumpToStep(int stepIndex) { // Jump to a certain time
        if (stepIndex < 0 || stepIndex >= algorithmSteps.size()) {
            return;
        }
        
        currentStepIndex = stepIndex;
        AlgorithmStep step = algorithmSteps.get(stepIndex);
        
        // Reset grid visuals
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Cell cell = grid[row][col];
                if (cell != startNode && cell != endNode && !cell.isWall()) {
                    cell.getRectangle().setFill(Color.LIGHTGRAY);
                }
            }
        }
        
        // First show open list nodes (blue)
        for (Cell cell : step.openList) {
            if (cell != startNode && cell != endNode && !step.closedList.contains(cell)) {
                cell.getRectangle().setFill(Color.BLUE);
            }
        }
        
        // Then show closed list nodes (also blue)
        for (Cell cell : step.closedList) {
            if (cell != startNode && cell != endNode) {
                cell.getRectangle().setFill(Color.BLUE);
            }
        }
        
        // Finally highlight current node and path
        if (step.currentNode != null && step.currentNode != startNode && step.currentNode != endNode) {
            step.currentNode.getRectangle().setFill(Color.YELLOW);
            highlightPath(step.currentNode);
        }
        
        // If path found, reconstruct final path
        if (step.isPathFound) {
            reconstructPath();
        }
        
        updateExplanation(step.message);
        updateInfoPanel();
        
        if (step.currentNode != null) {
            updateThreeByThreeGrid(step.currentNode);
        }
    }

    public static void runAStar(Cell start, Cell end, Button pauseBtn) {
        // Make sure any existing timeline is stopped
        if (currentTimeline != null) {
            currentTimeline.stop();
        }
        
        // Reset algorithm steps and disable slider at start
        algorithmSteps.clear();
        currentStepIndex = 0;
        progressSlider.setDisable(true);
        
        PriorityQueue<Cell> openList = new PriorityQueue<>(
                Comparator.comparingDouble(c -> c.distance * 0.9 + heuristic(c, end))); //Decides which node to access
        Set<Cell> openSet = new HashSet<>(); // For tracking open nodes more efficiently
        Set<Cell> closedSet = new HashSet<>();

        // Reset states
        paused = false;
        start.distance = 0;
        openList.add(start);
        openSet.add(start);

        startTime = System.currentTimeMillis();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        currentTimeline = timeline;

        pauseBtn.setOnAction(e -> {
            if (paused) {
                timeline.play();
                paused = false;
                pauseBtn.setText("Pause"); // Set text to "Pause" when resuming
            } else {
                timeline.stop();
                paused = true;
                pauseBtn.setText("Play"); // Set text to "Play" when pausing
            }
        });

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), event -> {
            if (openList.isEmpty()) {
                String message = "No path found.";
                updateExplanation(message);
                endTime = System.currentTimeMillis();
                updateInfoPanel();
                timeline.stop();
                pauseBtn.setDisable(true); // Disable the pause button
                
                // Add final step
                algorithmSteps.add(new AlgorithmStep(null, openSet, closedSet, message, false));
                updateProgressSlider(algorithmSteps.size() - 1, algorithmSteps.size());
                progressSlider.setDisable(false); // Enable slider when algorithm finishes
                return;
            }

            Cell current = openList.poll();
            openSet.remove(current);
            nodesExplored++;
            String message = "Checking node (" + current.row + ", " + current.col + ")";
            updateExplanation(message);

            if (current == end) {
                end.getRectangle().setFill(Color.RED); // Ensure end node stays red
                reconstructPath();
                endTime = System.currentTimeMillis();
                updateInfoPanel();
                timeline.stop();
                pauseBtn.setDisable(true); // Disable the pause button
                
                // Add final step with path found
                algorithmSteps.add(new AlgorithmStep(current, openSet, closedSet, "Path found!", true));
                updateProgressSlider(algorithmSteps.size() - 1, algorithmSteps.size());
                progressSlider.setDisable(false); // Enable slider when path is found
                return;
            }

            closedSet.add(current);
            if (current != start && current != end) {
                current.getRectangle().setFill(Color.YELLOW); // Highlight current node
            }

            for (Cell neighbor : getNeighbors(current)) {
                if (closedSet.contains(neighbor) || neighbor.isWall()) {
                    updateInfoPanel();
                    continue;
                }

                double tentativeG = current.distance + 1; // Assuming uniform cost for moving to a neighbor

                if (tentativeG <= neighbor.distance) {
                    neighbor.distance = tentativeG;
                    neighbor.parent = current;
                    if (!openSet.contains(neighbor)) {
                        openList.add(neighbor);
                        openSet.add(neighbor);
                        String neighborMessage = "Adding node (" + neighbor.row + ", " + neighbor.col + ") to open list";
                        updateExplanation(neighborMessage);
                        if (neighbor != start && neighbor != end) {
                            neighbor.getRectangle().setFill(Color.BLUE); // Highlight open list nodes
                        }
                    }
                }
            }

            // Store current algorithm state
            algorithmSteps.add(new AlgorithmStep(current, openSet, closedSet, message, false));
            updateProgressSlider(algorithmSteps.size() - 1, algorithmSteps.size());
            
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
        pauseBtn.setDisable(false); // Ensure the pause button is enabled when the algorithm starts
    }
    
    /**
     * Update the progress slider position
     */
    private static void updateProgressSlider(int currentStep, int totalSteps) {
        if (totalSteps > 0) {
            double progress = (double) currentStep / totalSteps * 100;
            Platform.runLater(() -> {
                if (!userAdjustingSlider) {
                    progressSlider.setValue(progress);
                }
            });
        }
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
        Cell pathNode = current;
        while (pathNode != null) {
            if (pathNode != startNode && pathNode != endNode) {
                pathNode.getRectangle().setFill(Color.YELLOW); // Highlight the path
                currentPath.add(pathNode);
            }
            pathNode = pathNode.parent;
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
                    text.setText(String.format("%.1f", neighbor.distance * 0.9 + heuristicValue));
                } else {
                    text.setText(""); // Leave the text empty for unscanned nodes
                }
            }
        }
    }

    private static class Cell { //Contains information on the state, location, and colour of each cell
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
            if (isWall) {
                setWall(false);  // Allow removing walls by clicking
                return;
            }
            
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
            infoPanel.appendText("Current Step: " + currentStepIndex + "/" + algorithmSteps.size() + "\n");
        });
    }

    private static void resetAlgorithm() {
        if (currentTimeline != null) {
            currentTimeline.stop();
        }
        
        closedList.clear();
        currentPath.clear();
        nodesExplored = 0;
        pathLength = 0;
        paused = false;
        algorithmSteps.clear();
        currentStepIndex = 0;
        progressSlider.setValue(0);
        progressSlider.setDisable(true); // Ensure slider is disabled when algorithm resets
        
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