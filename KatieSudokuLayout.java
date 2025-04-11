import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

public class KatieSudokuLayout extends TilePane {
    public Text[][] cell = new Text[9][9];
    int[][] board = readBoard("1.txt");

    KatieSudokuLayout() throws FileNotFoundException {
        TilePane boardTilePane = new TilePane();
        boardTilePane.setPrefColumns(9);
        boardTilePane.setPrefRows(9);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell[i][j] = new Text(board[i][j] + "");
                boardTilePane.getChildren().add(cell[i][j]);
            }
        }

        Text text = new Text("Hi");
        Button solveBtn = new Button("Solve");
        solveBtn.setOnAction(event -> {
            if (solve(board) == true) {
                text.setText("Yippee");
            } else if (solve(board) == false) {
                text.setText(":(");
            }
        });
        getChildren().addAll(boardTilePane, solveBtn, text);
    }

    public static int[][] readBoard(String filename) throws FileNotFoundException {
        int[][] board = new int[9][9];
        Scanner in = new Scanner(new File(filename));
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                board[x][y] = Integer.parseInt(in.nextLine());
            }
        }
        return board;
    }

    public void updateBoard() {
        // System.out.println("IM GOING TO KILL YOU");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell[i][j].setText(board[i][j] + "");
            }
        }
    }

    public boolean solve(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    for (int num = 1; num < 10; num++) {
                        if (validPlacement(board, i, j, num) == true) {
                            board[i][j] = num;
                            if (solve(board) == true) {
                                updateBoard();

                                return true;
                            } else {
                                board[i][j] = 0;
                                updateBoard();
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validPlacement(int[][] board, int x, int y, int num) {
        boolean valid = true;
        if (row(board, x, num) == false || col(board, y, num) == false || box(board, x, y, num) == false) {
            valid = false;
        }
        return valid;
    }

    public static boolean row(int[][] board, int x, int num) {
        boolean valid = true;
        for (int i = 0; i < 9; i++) {
            if (board[x][i] == num) {
                // row
                valid = false;
            }
        }
        return valid;
    }

    public static boolean col(int[][] board, int y, int num) {
        boolean valid = true;
        for (int i = 0; i < 9; i++) {
            if (board[i][y] == num) {
                // col
                valid = false;
            }
        }
        return valid;
    }

    public static boolean box(int[][] board, int x, int y, int num) {
        boolean valid = true;
        int boxRow = x - x % 3;
        int boxCol = y - y % 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == num) {
                    valid = false;
                }
            }
        }
        return valid;
    }
}