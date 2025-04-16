import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuVisuals extends GridPane {

    private int[][] gridNumbers;
    private StackPane[][] cellStacks = new StackPane[9][9]; // Store references to UI cells
    public boolean[][] isOriginal = new boolean[9][9];

    private Slider speedSlider; // Slider to control solving speed
    private Label speedLabel;   // Label to display speed control text

    private boolean solving = false; // Keeps track of sudoku and checks if it is being solved or not (used for solve, reset and back button)
    private volatile long stepDelay; // Sleep time


    public SudokuVisuals(String filename) {
        setAlignment(Pos.CENTER);

        // Add padding to ensure proper spacing
        setPadding(new Insets(3));

        // Load the external CSS file
        getStylesheets().add(getClass().getResource("css/SudokuStyle.css").toExternalForm());
        loadSudoku(filename); // Load the Sudoku puzzle based on the selected difficulty
    }


    private void loadSudoku(String filename) {
        isOriginal = new boolean[9][9]; // Reset isOriginal array

        try {
            gridNumbers = getTable(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        createSudokuGrid();

        Button solveButton = new Button("Solve Sudoku");
        solveButton.setOnAction(e -> {
            if (!solving) {
                solving = true; // Prevent pressing during solving
                new Thread(() -> {
                    Sudoku.solveSudoku(gridNumbers, stepDelay, isOriginal, cellStacks); // Pass isOriginal and cellStacks
                    solving = false; // Reset solving flag
                }).start();
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            if (!solving) {
                loadSudoku(filename);
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if (!solving) {
                FXUtils.setSceneRoot(getScene(), new DifficultyMenuLayout()); // Load DifficultyMenuLayout
            }
        });

        DifficultyMenuLayout.buttonStyle(solveButton, resetButton, backButton); // Apply button styles to solve, reset, and back buttons

        HBox buttonBox = new HBox(10, resetButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Initialize speedLabel before using it
        speedLabel = new Label("Speed Control");
        speedSlider = new Slider(1, 10, 1);
        DifficultyMenuLayout.speedSlider(speedLabel, speedSlider); // Details of speedslider

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int speedFactor = newVal.intValue();
            stepDelay = (long) (15000 / Math.pow(3, speedFactor));
        });

        // Default speed and start position of timer
        speedSlider.setValue(10);
        speedSlider.setValue(1);

        VBox controls = new VBox(10, solveButton, buttonBox, speedLabel, speedSlider);
        controls.setAlignment(Pos.CENTER);

        add(controls, 0, 9, 9, 1);
        GridPane.setHalignment(solveButton, HPos.CENTER);
    }

    private int[][] getTable(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        int[][] gridNumbers = new int[9][9];

        for (int i = 0; i < 9; i++) {
            String row = scanner.nextLine();
            String[] rowStrings = row.split(",");

            for (int j = 0; j < 9; j++) {
                gridNumbers[i][j] = Integer.parseInt(rowStrings[j]);
            }
        }

        scanner.close();
        return gridNumbers;
    }

    private void createSudokuGrid() {
        int subGridSize = 3; // Size of subgrid (3x3)
        int cellSize = 60; // Size of each cell

        for (int row = 0; row < subGridSize; row++) {
            for (int col = 0; col < subGridSize; col++) {

                GridPane innerGrid = new GridPane();
                innerGrid.setPadding(new Insets(2));

                for (int i = 0; i < subGridSize; i++) {
                    for (int j = 0; j < subGridSize; j++) {

                        Rectangle innerCell = new Rectangle(cellSize, cellSize);

                        int globalRow = row * subGridSize + i;
                        int globalCol = col * subGridSize + j;

                        cellStacks[globalRow][globalCol] = new StackPane(innerCell);
                        StackPane cellStack = cellStacks[globalRow][globalCol];

                        innerCellDesign(innerCell, i, j, cellStack);

                        int number = gridNumbers[globalRow][globalCol];

                        if (number != 0) {
                            Text text = new Text(String.valueOf(number));
                            text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                            text.setFill(Color.rgb(44, 62, 80));

                            cellStack.getChildren().add(text);

                            isOriginal[globalRow][globalCol] = true;
                        }

                        innerGrid.add(cellStack, j, i);
                    }
                }

                Rectangle outerCell = new Rectangle(cellSize * subGridSize + 4, cellSize * subGridSize + 4);
                outerCell.getStyleClass().add("outer-cell");

                StackPane stack = new StackPane(outerCell, innerGrid);
                add(stack, col, row);
            }
        }
    }

    

    public static void innerCellDesign(Rectangle innerCell, int i, int j, Node cellStack) {
        
        // Create inner cell with rounded corners
        innerCell.setFill(Color.WHITE);
        innerCell.setStroke(Color.LIGHTGRAY);
        innerCell.setStrokeWidth(1);
        innerCell.setArcWidth(5);
        innerCell.setArcHeight(5);
        
        // Add inner shadow for depth
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(2);
        innerShadow.setColor(Color.rgb(0, 0, 0, 0.05));
        innerCell.setEffect(innerShadow);

        // Add shadow effect to cell
        DropShadow cellShadow = new DropShadow();
        cellShadow.setRadius(5);
        cellShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        cellStack.setEffect(cellShadow);
    }

    public static void outerCellDesign(Shape outerCell) {
        
        outerCell.setFill(Color.TRANSPARENT);
        outerCell.setStroke(Color.rgb(52, 73, 94)); // Dark blue-gray color
        outerCell.setStrokeWidth(3);
        ((Rectangle) outerCell).setArcWidth(15);
        ((Rectangle) outerCell).setArcHeight(15);
        
        // Add shadow effect to outer grid
        DropShadow outerShadow = new DropShadow();
        outerShadow.setRadius(8);
        outerShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        outerCell.setEffect(outerShadow);
    }
}