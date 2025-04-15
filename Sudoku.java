import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Platform;

public class Sudoku extends GridPane {

    private int[][] gridNumbers;
    private StackPane[][] cellStacks = new StackPane[9][9]; // Store references to UI cells
    private boolean[][] isOriginal = new boolean[9][9];

    private Slider speedSlider; // Slider to control solving speed
    private Label speedLabel;   // Label to display speed control text
    private volatile long stepDelay; // Sleep time

    private boolean solving = false; // Keeps track of sudoku and checks if it is being solved or not (used for solve, reset and back button)

    public Sudoku(String filename) {
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
                solving = true; // Cannot press more than once (to prevent being pressed during solving) 
                new Thread(() -> {
                    solveSudoku(gridNumbers);
                    solving = false;
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

        SudokuVisuals.buttonStyle(solveButton, resetButton, backButton); // Apply button styles to solve, reset, and back buttons

        HBox buttonBox = new HBox(10, resetButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Initialize speedLabel before using it
        speedLabel = new Label("Speed Control");
        speedSlider = new Slider(1, 10, 1);
        SudokuVisuals.speedSlider(speedLabel, speedSlider); // Details of speedslider

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> { //obs and oldVal are not used but are still needed for addListener
            // Get the integer value of the new slider value to use as the speed factor
            int speedFactor = newVal.intValue();
            stepDelay = (long)(15000 / Math.pow(3, speedFactor)); 
            // The formula adjusts the delay by dividing 15000 by 3 raised to the power of the speed factor. Exponential increase
        });

        // Default speed and start position of timer
        speedSlider.setValue(10); 
        speedSlider.setValue(1);

        VBox controls = new VBox(10, solveButton, buttonBox, speedLabel, speedSlider);
        controls.setAlignment(Pos.CENTER);

        add(controls, 0, 9, 9, 1);
        GridPane.setHalignment(solveButton, HPos.CENTER);
    }

    int[][] getTable(String filename) throws FileNotFoundException { //getting values from the file
        Scanner scanner = new Scanner(new File(filename));

        int[][] gridNumbers = new int[9][9];

        // Split the row by commas to get individual values as strings and then parse it to integers to be stored in gridNumbers array
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
   
        for (int row = 0; row < subGridSize; row++) { // Bigger 3x3 cell
            for (int col = 0; col < subGridSize; col++) {
                
                GridPane innerGrid = new GridPane();
                innerGrid.setPadding(new Insets(2));
   
                for (int i = 0; i < subGridSize; i++) {  // 3x3 cell inside each bigger 3x3 cell
                    for (int j = 0; j < subGridSize; j++) {

                        Rectangle innerCell = new Rectangle(cellSize, cellSize);

                        int globalRow = row * subGridSize + i; // Full 9x9 row index
                        int globalCol = col * subGridSize + j; // Full 9x9 column index
   
                        cellStacks[globalRow][globalCol] = new StackPane(innerCell);
                        StackPane cellStack = cellStacks[globalRow][globalCol];
                       
                        SudokuVisuals.innerCellDesign(innerCell, i, j, cellStack);
   
                        int number = gridNumbers[globalRow][globalCol];
   
                        if (number != 0) {
                            Text text = new Text(String.valueOf(number));
                            text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                            text.setFill(Color.rgb(44, 62, 80)); // Dark blue-gray color for original numbers

                            cellStack.getChildren().add(text);

                            isOriginal[globalRow][globalCol] = true; // Mark as an original number
                        }

                        innerGrid.add(cellStack, j, i);
                    }
                }
   
                // Outer cell with rounded corners and shadow
                Rectangle outerCell = new Rectangle(cellSize * subGridSize + 4, cellSize * subGridSize + 4);
                outerCell.getStyleClass().add("outer-cell"); // Apply CSS class to the outer cell
   
                StackPane stack = new StackPane(outerCell, innerGrid);
                add(stack, col, row);
            }
        }
    }

    public void solveSudoku(int[][] board) {
        backtrack(board, 0, 0);
    }

    private boolean backtrack(int[][] board, int row, int col) {
        if (row == 9) {
            return true; // If we reach past the last row, the Sudoku is solved
        }
        if (col == 9) {
            return backtrack(board, row + 1, 0); // Move to the next row
        }
        if (board[row][col] != 0) {
            return backtrack(board, row, col + 1); // Skip filled cells
        }

        // Trying to place numbers 1-9 in the current empty cell.
        for (int number = 1; number <= 9; number++) {
            if (isValid(board, row, col, number)) { // Check if the number is valid in this position
                
                board[row][col] = number; // Place the number

                updateCell(row, col, number);

                // Recursively try to solve the next cell (move to the next column). If placing a number is possible in that cell, return true.
                if (backtrack(board, row, col + 1)) {
                    return true;
                }

                // If placing 'number' didn't work, backtrack by resetting the cell to 0.
                board[row][col] = 0;
                updateCell(row, col, 0);
            }
        }

        // No valid number found
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int numExists) {
        
        //  Check if number (numExists) already exists in the same row/col
        for (int i = 0; i < 9; i++) {
            if (numExists == board[row][i] || numExists == board[i][col]) // If number is found in the row/col, it's invalid
            {
                return false;
            }    
        }
    
        // Calculate starting row and column index of 3x3 subgrid that contains (row, col)
        int startRow = (row / 3) * 3; 
        int startCol = (col / 3) * 3; 
    
        // Checking if 'numExists' already exists in the same 3x3 subgrid
        for (int i = 0; i < 3; i++) { // Loop over 3 rows of subgrid
            for (int j = 0; j < 3; j++) { // Loop over 3 columns of subgrid
                
                if (numExists == board[startRow + i][startCol + j]) // If number is found in subgrid, invalid
                {
                    return false;
                }
            }
        }
    
        // If number doesn't exist in row, column, and subgrid, it is a valid placement
        return true;
    }

    private void updateCell(int row, int col, int number) {
        Platform.runLater(() -> {
            StackPane cellStack = cellStacks[row][col];
            cellStack.getChildren().clear();
    
            Rectangle innerCell = new Rectangle(60, 60);
            innerCell.getStyleClass().add("inner-cell");
            cellStack.getChildren().add(innerCell);
    
            if (number != 0) {
                Text text = new Text(String.valueOf(number));
    
                if (isOriginal[row][col]) {
                    // Keep original numbers bold and black
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                    text.setFill(Color.BLACK);
                } else {
                    // Apply green, bold, or bigger font for non-original numbers
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
                    text.setFill(Color.GREEN);
                }
                cellStack.getChildren().add(text);
            }
        });
    
        try {
            Thread.sleep(stepDelay); // sleep time based on slider value
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        // Reset the cell to black after the delay
        Platform.runLater(() -> {
            if (!isOriginal[row][col] && number != 0) {
                StackPane cellStack = cellStacks[row][col];
                if (cellStack.getChildren().size() > 1) {
                    Text text = (Text) cellStack.getChildren().get(1);
                    text.setFill(Color.BLACK);
                    text.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
                }
            }
        });
    }
}