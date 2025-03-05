import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku {
    int[][] board = new int[9][9];
    public int[][] createBoard(){
        
        return board;
    }

    public int readBoard(String filename) throws FileNotFoundException{
        Scanner in = new Scanner(new File(filename));
        int num = 0;
        return num;
    }

    public int placeNum(){
        int num = 0;
        return num;
    }
}
