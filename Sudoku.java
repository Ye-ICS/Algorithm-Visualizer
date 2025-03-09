import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class Sudoku extends GridPane{
    Sudoku(){
        
        int subGridSize = 3;   // Size of subgrid (3x3)
        int cellSize = 200;    // Size of each cell

        int[][] gridNumbers = {
            {8,0,1,3,4,0,0,2,0},
            {0,5,0,6,0,0,8,0,3},
            {0,0,0,0,9,5,1,0,0},
            {6,0,0,0,5,9,0,0,4},
            {0,0,3,0,0,0,7,5,0},
            {0,0,5,2,3,0,6,8,0},
            {0,0,9,5,0,8,4,0,6},
            {5,7,0,1,0,0,2,0,8},
            {3,0,6,0,0,0,0,0,0},
        };

        // 3x3 grid UI with nested 3x3 cells inside each main cell
        for (int row = 0; row < subGridSize; row++) { //Bigger 3x3 cell
            for (int col = 0; col < subGridSize; col++) {
                
                GridPane innerGrid = new GridPane();
                
                for (int i = 0; i < subGridSize; i++) { //3x3 cell inside each bigger 3x3 cell
                    for (int j = 0; j < subGridSize; j++) {
                
                        Rectangle innerCell = new Rectangle(cellSize / subGridSize, cellSize / subGridSize);
                        innerCell.setFill(Color.WHITE);
                        innerCell.setStroke(Color.LIGHTGRAY);
                        innerCell.setStrokeWidth(1);
                
                        StackPane cellStack = new StackPane();
                        cellStack.getChildren().add(innerCell);

                        int globalRow = row * subGridSize + i; //globalRow is the full row of 9x9
                        int globalCol = col * subGridSize + j; //globalCol is the full coloumn of 9x9
                        int existingNumber = gridNumbers[globalRow][globalCol]; // Get the number for the current cell in the full 9x9 grid

                        if (existingNumber != 0) { //Pasting numbers
                            Text text = new Text(String.valueOf(existingNumber));
                            text.setFont(Font.font(24)); 
                            cellStack.getChildren().add(text);
                        }
                
                        innerGrid.add(cellStack, j, i); 
                    }
                }
                
                Rectangle outerCell = new Rectangle(cellSize, cellSize);
                outerCell.setFill(Color.TRANSPARENT);
                outerCell.setStroke(Color.BLACK);
                outerCell.setStrokeWidth(7);

                StackPane stack = new StackPane();
                stack.getChildren().addAll(outerCell, innerGrid);
                add(stack, col, row);
            }
        }

    }
}
