import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.HPos;

public class Sudoku extends GridPane {

    private int[][] gridNumbers;

    public Sudoku() {
        try {
            gridNumbers = getTable("data/sudoku/Example.txt"); // Handle exception properly
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Print error message to console
            gridNumbers = new int[9][9]; // Default empty grid in case of failure
        }

        createSudokuGrid();

        // Solve button below the grid
        Button solveButton = new Button("Solve Sudoku");
        solveButton.setOnAction(e -> new Thread(() -> solveSudoku(gridNumbers)).start());

        add(solveButton, 0, 9, 9, 1); // Span across all 9 columns
        GridPane.setHalignment(solveButton, HPos.CENTER); // Center horizontally
    }

    private void createSudokuGrid() {
        int subGridSize = 3; // Size of subgrid (3x3)
        int cellSize = 60; // Size of each cell

        // 3x3 grid UI with nested 3x3 cells inside each main cell
        for (int row = 0; row < subGridSize; row++) { //Bigger 3x3 cell
            for (int col = 0; col < subGridSize; col++) {

                GridPane innerGrid = new GridPane();

                for (int i = 0; i < subGridSize; i++) {  //3x3 cell inside each bigger 3x3 cell
                    for (int j = 0; j < subGridSize; j++) {

                        Rectangle innerCell = new Rectangle(cellSize, cellSize);
                        innerCell.setFill(Color.WHITE);
                        innerCell.setStroke(Color.LIGHTGRAY);

                        StackPane cellStack = new StackPane(innerCell);

                        int globalRow = row * subGridSize + i; //globalRow is the full row of 9x9
                        int globalCol = col * subGridSize + j; //globalCol is the full coloumn of 9x9
                        int number = gridNumbers[globalRow][globalCol]; // Get the number for the current cell in the full 9x9 grid

                        if (number != 0) { //Pasting numbers
                            Text text = new Text(String.valueOf(number));
                            text.setFont(Font.font(24));
                            cellStack.getChildren().add(text);
                        }

                        innerGrid.add(cellStack, j, i);
                    }
                }

                // Outer thick border
                Rectangle outerCell  = new Rectangle(cellSize * subGridSize, cellSize * subGridSize);
                outerCell .setStroke(Color.BLACK);
                outerCell .setStrokeWidth(7);

                StackPane stack = new StackPane(outerCell, innerGrid);
                add(stack, col, row);
            }
        }
    }

    int[][] getTable(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        int[][] gridNumbers = new int[9][9];

        for (int k = 0; k < 9; k++) {
            String row = scanner.nextLine();
            String[] rowStrings = row.split(",");

            for (int j = 0; j < 9; j++) {
                gridNumbers[k][j] = Integer.parseInt(rowStrings[j]); // Properly populate grid
            }
        }

        scanner.close(); 
        return gridNumbers;
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
                
                number = board[row][col]; // Place the number
                updateCell(row, col, number); 
                
                // Recursively attempt to solve the rest of the board.
                if (backtrack(board, row, col + 1)) {
                    return true; // solution found
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

    private void updateCell (int row, int col, int number) {
        
    }
}