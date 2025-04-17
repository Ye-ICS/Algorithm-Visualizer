import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.application.Platform;

/**
 * This method initializes the solving process by marking the original cells of the puzzle and then
 * calls a recursive backtracking function to solve the puzzle. 
 */
public class Sudoku {
   
    public static void solveSudoku(int[][] board, long stepDelay, StackPane[][] cellStacks) {
        boolean[][] isOriginal = new boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                isOriginal[row][col] = board[row][col] != 0; 
            }
        }
        backtrack(board, 0, 0, isOriginal, cellStacks, stepDelay);
    }

    private static boolean backtrack(int[][] board, int row, int col, boolean[][] isOriginal, StackPane[][] cellStacks, long delayAmount) {
        if (row == 9) {
            return true; // If we reach past the last row, the Sudoku is solved
        }
        if (col == 9) {
            return backtrack(board, row + 1, 0,isOriginal, cellStacks, delayAmount ); // Move to the next row
        }
        if (board[row][col] != 0) {
            return backtrack(board, row, col + 1, isOriginal, cellStacks, delayAmount); // Skip filled cells
        }

        // Trying to place numbers 1-9 in the current empty cell.
        for (int number = 1; number <= 9; number++) {
            if (isValid(board, row, col, number)) { // Check if the number is valid in this position
                board[row][col] = number; // Place the number

                updateCell(row, col, number, isOriginal, cellStacks, delayAmount); // Update the cell visually

                // Recursively try to solve the next cell (move to the next column). If placing a number is possible in that cell, return true.
                if (backtrack(board, row, col + 1, isOriginal, cellStacks, delayAmount)) {
                    return true;
                }

                // If placing 'number' didn't work, backtrack by resetting the cell to 0.
                board[row][col] = 0;
                updateCell(row, col, 0, isOriginal, cellStacks, delayAmount); // Reset the cell visually
            }
        }

        // No valid number found
        return false;
    }

    private static boolean isValid(int[][] board, int row, int col, int numExists) {
        // Check if number (numExists) already exists in the same row/col
        for (int i = 0; i < 9; i++) {
            if (numExists == board[row][i] || numExists == board[i][col]) {
                return false;
            }
        }

        // Calculate starting row and column index of 3x3 subgrid that contains (row, col)
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        // Checking if 'numExists' already exists in the same 3x3 subgrid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (numExists == board[startRow + i][startCol + j]) {
                    return false;
                }
            }
        }

        // If number doesn't exist in row, column, and subgrid, it is a valid placement
        return true;
    }

    private static void updateCell(int row, int col, int number, boolean[][]isOriginal, StackPane[][] cellStacks, long delayAmount) {
        Platform.runLater(() -> {
            StackPane cellStack = cellStacks[row][col]; 
            if (cellStack == null) return;
            cellStack.getChildren().clear();

            Rectangle innerCell = new Rectangle(60, 60);
            innerCell.getStyleClass().add("inner-cell");
            cellStack.getChildren().add(innerCell);

            if (number != 0) {
                Text text = new Text(String.valueOf(number));

                if (isOriginal[row][col]) { // Access isOriginal through the visuals instance
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
            Thread.sleep(delayAmount); // sleep time based on slider value
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }

        // Reset the cell to black after the delay
        Platform.runLater(() -> {
            if (!isOriginal[row][col] && number != 0) {
                StackPane cellStack = cellStacks[row][col];
                if (cellStack != null && cellStack.getChildren().size() > 1) {
                    Text text = (Text) cellStack.getChildren().get(1);
                    text.setFill(Color.BLACK);
                    text.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
                }
            }
        });
    }
}