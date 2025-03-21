import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class BruteSudoku extends FlowPane{
    
    TextField[][] sudokuCells = new TextField[9][9];
     Random random = new Random();
     Boolean noSolution = false;

    /**
     * Constructs layout for brute force sudoku solver
     */
    BruteSudoku() {

        Text title = new Text("Brute Force Sudoku Solver");

        GridPane sudokuTable = new GridPane();
        sudokuTable.setAlignment(Pos.CENTER);
        sudokuTable.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        sudokuTable.setPadding(new Insets(10));
        sudokuTable.setVgap(2);
        sudokuTable.setHgap(2);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField sudokuNumber = new TextField();
                sudokuNumber.setPrefHeight(40);
                sudokuNumber.setPrefWidth(40);
                sudokuNumber.setAlignment(Pos.CENTER);


                sudokuCells[i][j] = sudokuNumber;
                sudokuTable.add(sudokuNumber, i, j);
            }
        }

        setAlignment(Pos.CENTER);

        fillRandomNumbers();

      

        Text description = new Text("");

        Button sudokuBox = new Button("");

        Button solveButton = new Button("Solve");
        

        Button backsBtn = new Button("Back");
        backsBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        solveButton.setOnAction(event -> solveSudoku());

        getChildren().addAll(title, backsBtn, sudokuTable, solveButton);
    }

    void BruteForceNumbers() {
        int[][] board = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = sudokuCells[i][j].getText().trim();
                if (!text.isEmpty() && text.matches("[1-9]")) {
                    board[i][j] = Integer.parseInt(text);
                } else {
                    board[i][j] = 0; 
                }
            }
        }




}

void solveSudoku() {
    int[][] board = new int[9][9];

    // 
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            String text = sudokuCells[i][j].getText().trim();
            if (!text.isEmpty() && text.matches("[1-9]")) {
                board[i][j] = Integer.parseInt(text);
            } else {
                board[i][j] = 0;
            }
        }
    }

    // Solve the Sudoku
    if (solve(board)) {
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    } else {
        System.out.println("No solution exists.");
        fillRandomNumbers();
    }
}

// 
boolean solve(int[][] board) {
    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            if (board[row][col] == 0) {
                for (int num = 1; num <= 9; num++) {
                    if (isValid(board, row, col, num)) {
                        board[row][col] = num;
                        if (solve(board)) {
                            return true;
                        }
                        board[row][col] = 0; // 
                    }
                }
                return false; // No valid number found
            }
        }
    }
    return true; // Solved
}

//
boolean isValid(int[][] board, int row, int col, int num) {
    for (int i = 0; i < 9; i++) {
        if (board[row][i] == num || board[i][col] == num) {
            return false;
        }
    }

    // 
    int startRow = (row / 3) * 3;
    int startCol = (col / 3) * 3;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (board[startRow + i][startCol + j] == num) {
                return false;
            }
        }
    }

    return true;
}

    void fillRandomNumbers() {
        clearTable();

        int numbersToPlace = random.nextInt(20) + 10;

        for (int k = 0; k < numbersToPlace; k++) {
            int i, j;
            do {
                i = random.nextInt(9);
                j = random.nextInt(9);
            } while (!sudokuCells[i][j].getText().isEmpty());

            int randomNum = random.nextInt(9) + 1;
            sudokuCells[i][j].setText(String.valueOf(randomNum));
            sudokuCells[i][j].setEditable(false);
        }
    }

    void clearTable() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j].setText("");
                sudokuCells[i][j].setEditable(true);
            }
        }
    }
}
