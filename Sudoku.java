import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.geometry.HPos;

public class Sudoku extends GridPane {

    public Sudoku() {

        createSudokuGrid();

        // Solve button below the grid
        Button solveButton = new Button("Solve Sudoku");
        solveButton.setOnAction(e -> new Thread(() -> solveSudoku(gridNumbers)).start());

        add(solveButton, 0, 9, 9, 1); // Span across all 9 columns
        GridPane.setHalignment(solveButton, HPos.CENTER); // Center horizontally

    }

    private int[][] gridNumbers = {
        {8, 0, 1, 3, 4, 0, 0, 2, 0},
        {0, 5, 0, 6, 0, 0, 8, 0, 3},
        {0, 0, 0, 0, 9, 5, 1, 0, 0},
        {6, 0, 0, 0, 5, 9, 0, 0, 4},
        {0, 0, 3, 0, 0, 0, 7, 5, 0},
        {0, 0, 5, 2, 3, 0, 6, 8, 0},
        {0, 0, 9, 5, 0, 8, 4, 0, 6},
        {5, 7, 0, 1, 0, 0, 2, 0, 8},
        {3, 0, 6, 0, 0, 0, 0, 0, 0},
    };

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

    public void solveSudoku(int[][] board) {
        backtrack(board, 0, 0);
    }

    private boolean backtrack(int[][] board, int row, int col) {
        return false;
    }

    private boolean isValid (int[][] board, int row, int col, int num) {
        return false;
    }

    private boolean updateCell (int row, int col, int num) {
        return false;
    }

}