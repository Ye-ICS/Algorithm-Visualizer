import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

public class SudokuLayout extends TilePane{
    SudokuLayout() throws FileNotFoundException{
        //setAlignment(Pos.CENTER);
        setPrefColumns(9);
        setPrefRows(9);
        Text [][] cell = new Text[9][9];
        int [][] board = Sudoku.readBoard("1.txt" );
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                cell[i][j] = new Text(board[i][j] + "");
                getChildren().add(cell[i][j]);
            }
        }
    }
    
}