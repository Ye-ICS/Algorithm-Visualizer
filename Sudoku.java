import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sudoku extends GridPane{
    Sudoku(){
        
        int subGridSize = 3;   // Size of subgrid (3x3)
        int cellSize = 200;     // Size of each cell

        // Generate 3x3 grid UI with nested 3x3 cells inside each main cell
        for (int row = 0; row < subGridSize; row++) {
            for (int col = 0; col < subGridSize; col++) {
                
                GridPane innerGrid = new GridPane();
                
                for (int i = 0; i < subGridSize; i++) {
                    for (int j = 0; j < subGridSize; j++) {
                        Rectangle innerCell = new Rectangle(cellSize / subGridSize, cellSize / subGridSize);
                        innerCell.setFill(Color.WHITE);
                        innerCell.setStroke(Color.LIGHTGRAY);
                        innerCell.setStrokeWidth(1);
                        innerGrid.add(innerCell, j, i);
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
