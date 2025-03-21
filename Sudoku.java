import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku {
    // place lowest number check if valid then move on
    int[][] board = new int[9][9];

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

    public int placeNum(int[] numTry, int step) {
        int num = 0;
        if (numTry[step] < 9) {
            num = numTry[step]++;
        } else {
            num = -1;
        }
        return num;
    }

    public void solve(int[][] board) {

        int steps = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    steps++;
                }
            }
        }
        // maybe add something for last placed number

        int[][][] boardState = new int[steps][9][9];
        int[] numTry = new int[steps];
        int step = 0;
        int x = 0;
        int y = 0;
        while (solved(board) == false) {
            // find out what x should be
            int temp = placeNum(numTry, step);
            if (temp < 0) {
                step--;
                numTry[step]++;
            } else {
                boardState[step][x][y] = temp;
                step++;
            }
        }
    }

    public boolean validPlacement(int[][] board) {
        boolean valid = true;

        for (int i = 0; i < 9; i++) {
            // constant num
            for (int j = 0; j < 9; j++) {
                // first num
                for (int k = 0; k < 9; k++) {
                    // second num
                    // if()
                    if (board[j][i] == board[k][i] && (j != k)) {
                        // row
                        valid = false;
                    }
                    if (board[i][j] == board[i][k] && (j != k)) {
                        // col
                        valid = false;
                    }

                }
            }
        }
        int placement = 0;
        for (int a = 0; a < 3; a++) {
            placement += 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (board[i + placement][j = placement] == board[i + placement][k + placement]) {
                                valid = false;
                            }
                            if (board[j + placement][i + placement] == board[k + placement][i + placement]) {
                                valid = false;
                            }
                            if (board[i + placement][j + placement] == board[k + placement][l + placement]) {
                                valid = false;
                            }
                        }
                    }
                }
            }
        }
        /*
         * for (int i = 0; i < 3; i++) {
         * for (int j = 0; j < 3; j++) {
         * for (int k = 0; k < 3; k++) {
         * for (int l = 0; l < 3; l++) {
         * if (board[i][j] == board[i][k]) {
         * valid = false;
         * }
         * if (board[j][i] == board[k][i]) {
         * valid = false;
         * }
         * if (board[i][j] == board[k][l]) {
         * valid = false;
         * }
         * }
         * }
         * }
         * }
         */
        // hklsdhjldjs
        return valid;
    }

    public boolean solved(int[][] board) {
        boolean solved = false;
        return solved;
    }
}
