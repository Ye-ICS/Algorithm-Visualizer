import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
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
    
    public Sudoku() {
        showDifficultySelection();
    }

    private void showDifficultySelection() {
        
        StackPane container = new StackPane(); // Wrap VBox for centering
        VBox difficultySelection = new VBox(20);
        difficultySelection.setAlignment(Pos.CENTER);
    
        // Match Sudoku grid's window size
        int width = 577;  // Match Sudoku width
        int height = 740; // Match Sudoku height
    
        // Set window size to match the Sudoku UI
        Platform.runLater(() -> {
            getScene().getWindow().setWidth(width);
            getScene().getWindow().setHeight(height);
        });
    
        // Label for the difficulty selection screen
        Label difficultyLabel = new Label("Choose Sudoku Difficulty");
        difficultyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
    
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");
    
        // Style buttons to be uniform
        String buttonStyle = "-fx-font-size: 30px; -fx-pref-width: 300px; -fx-pref-height: 140px;";
        easyButton.setStyle(buttonStyle);
        mediumButton.setStyle(buttonStyle);
        hardButton.setStyle(buttonStyle);
    
        easyButton.setOnAction(e -> loadSudoku("data/sudoku/Easy.txt"));
        mediumButton.setOnAction(e -> loadSudoku("data/sudoku/Medium.txt"));
        hardButton.setOnAction(e -> loadSudoku("data/sudoku/Hard.txt"));
    
        difficultySelection.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton);
        container.getChildren().add(difficultySelection); // Center VBox inside StackPane
    
        // Ensure the entire StackPane itself is centered in the GridPane
        setAlignment(Pos.CENTER);
        add(container, 0, 9, 9, 1);
    }

    private void loadSudoku(String filename) {

        getChildren().clear(); // Clear difficulty buttons
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

        // Create a back button to go back to the difficulty selection screen
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if (!solving) {
                getChildren().clear(); 
                showDifficultySelection(); // Show the difficulty selection screen again
            }
        });

        // Align the back and reset buttons side by side in an HBox
        HBox buttonBox = new HBox(10, resetButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        speedLabel = new Label("Speed Control");
        speedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        speedSlider = new Slider(1, 10, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> { //obs and oldVal are not used but are still needed for addListener
            // Get the integer value of the new slider value to use as the speed factor
            int speedFactor = newVal.intValue();
            stepDelay = (long)(15000 / Math.pow(3, speedFactor)); 
            // The formula adjusts the delay by dividing 15000 by 3 raised to the power of the speed factor. Exponential increase
        });

        // Default speed and start posiiton of timer
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
   
                for (int i = 0; i < subGridSize; i++) {  // 3x3 cell inside each bigger 3x3 cell
                    for (int j = 0; j < subGridSize; j++) {
   
                        Rectangle innerCell = new Rectangle(cellSize, cellSize);
                        innerCell.setFill(Color.WHITE);
                        innerCell.setStroke(Color.LIGHTGRAY);
   
                        int globalRow = row * subGridSize + i; // Full 9x9 row index
                        int globalCol = col * subGridSize + j; // Full 9x9 column index
   
                        cellStacks[globalRow][globalCol] = new StackPane(innerCell);
                        StackPane cellStack = cellStacks[globalRow][globalCol];
   
                        int number = gridNumbers[globalRow][globalCol];
   
                        if (number != 0) {
                            Text text = new Text(String.valueOf(number));
                            text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                            cellStack.getChildren().add(text);
                            isOriginal[globalRow][globalCol] = true; // Mark as an original number
                        }
                        innerGrid.add(cellStack, j, i);
                    }
                }
   
                // Outer thick border
                Rectangle outerCell  = new Rectangle(cellSize * subGridSize, cellSize * subGridSize);
                outerCell.setStroke(Color.BLACK);
                outerCell.setStrokeWidth(7);
   
                StackPane stack = new StackPane(outerCell, innerGrid);
                add(stack, col, row);
            }
        }
    }

    public void solveSudoku(int[][] board) {
        backtrack(board, 0, 0);
    }

    private boolean backtrack(int[][] board, int row, int col) {
        
        if (row == 9)
        {
            return true; // If we reach past the last row, the Sudoku is solved
        }
        if (col == 9) 
        {
            return backtrack(board, row + 1, 0); // Move to the next row
        }
        if (board[row][col] != 0) 
        {
            return backtrack(board, row, col + 1); // Skip filled cells
        }

        // Trying to place numbers 1-9 in the current empty cell.
        for (int number = 1; number <= 9; number++) {
            if (isValid(board, row, col, number)) { // Check if the number is valid in this position
                
                board[row][col] = number; // Place the number
    
                // Check if it's the last number being placed
                boolean isLastCell = isLastCellToBeFilled(board, row, col);
                updateCell(row, col, number, isLastCell);
    
                // Recursively try to solve the next cell (move to the next column). If placing a number is possible in that cell, return true.
                if (backtrack(board, row, col + 1)) {
                    return true; 
                }
    
                // If placing 'number' didn't work, backtrack by resetting the cell to 0.
                board[row][col] = 0;
                updateCell(row, col, 0, false);
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

    private void updateCell(int row, int col, int number, boolean isLastCell) {
        Platform.runLater(() -> {
            StackPane cellStack = cellStacks[row][col];
            cellStack.getChildren().clear();
    
            Rectangle innerCell = new Rectangle(60, 60);
            innerCell.setFill(Color.WHITE);
            innerCell.setStroke(Color.LIGHTGRAY);
            cellStack.getChildren().add(innerCell);
    
            if (number != 0) {
                Text text = new Text(String.valueOf(number));
    
                // Reset all previously placed numbers to normal black
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) { 
                        if (!isOriginal[i][j] && cellStacks[i][j].getChildren().size() > 1) {
                            Text previousText = (Text) cellStacks[i][j].getChildren().get(1);
                            previousText.setFill(Color.BLACK);
                            previousText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
                        }
                    }
                }

                if (isOriginal[row][col]) { 
                    // Keep original numbers bold and black
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                    text.setFill(Color.BLACK);
                } 
                else 
                {
                    // If it's the last cell, do NOT apply green, bold, or bigger font
                    if (isLastCell) {
                        text.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
                        text.setFill(Color.BLACK);
                    } else {
                        text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
                        text.setFill(Color.GREEN);
                    }
                }
                cellStack.getChildren().add(text);
            }
        });
    
        try {
            Thread.sleep(stepDelay); // sleep time based on slider value
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isLastCellToBeFilled(int[][] board, int row, int col) {
        for (int i = row; i < 9; i++) {
            for (int j = (i == row ? col : 0); j < 9; j++) { // If in the starting row, start from the given column; otherwise, start from the first column
                if (board[i][j] == 0) {
                    return false; // More empty cells exist
                }
            }
        }
        return true; // This is the last cell being filled
    }
}