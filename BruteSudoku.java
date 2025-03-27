import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BruteSudoku extends FlowPane {
    
    TextField[][] sudokuCells = new TextField[9][9];
    Random random = new Random();

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

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(event -> solveSudoku());

        Button backsBtn = new Button("Back");
        backsBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(title, backsBtn, sudokuTable, solveButton);
    }

    // 🔹 Modified to use animated solving
    void solveSudoku() {
        int[][] board = new int[9][9];

        // Read board from UI
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

        // Solve with animation
        new Thread(() -> solveWithAnimation(board)).start();
    }

    // 🔹 Runs the solving process step by step with animation
    void solveWithAnimation(int[][] board) {
        solveStep(board, 0, 0);
    }

    // 🔹 Solves recursively and updates UI dynamically
    void solveStep(int[][] board, int row, int col) {
        if (row == 9) return; // Solved successfully

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col + 1) % 9;

        if (board[row][col] != 0) {
            solveStep(board, nextRow, nextCol);
            return;
        }

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                animateSolving(row, col, num, () -> solveStep(board, nextRow, nextCol));

                return; // Stop recursion until animation is done
            }
        }

        // Backtrack
        board[row][col] = 0;
        animateSolving(row, col, 0, () -> solveStep(board, nextRow, nextCol));
    }

    // 🔹 Animates number placement and backtracking
    void animateSolving(int row, int col, int num, Runnable nextStep) {
        PauseTransition pause = new PauseTransition(Duration.millis(50)); // Delay per step
        pause.setOnFinished(e -> {
            Platform.runLater(() -> sudokuCells[row][col].setText(num == 0 ? "" : String.valueOf(num)));
            nextStep.run();
        });
        pause.play();
    }


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
                            board[row][col] = 0; 
                        }
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; 
    }

    boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }


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
        int[][] board = new int[9][9];

        solve(board);


        int numbersToRemove = 50; 
        for (int k = 0; k < numbersToRemove; k++) {
            int i, j;
            do {
                i = random.nextInt(9);
                j = random.nextInt(9);
            } while (board[i][j] == 0);

            board[i][j] = 0;
        }


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    sudokuCells[i][j].setText(String.valueOf(board[i][j]));
                    sudokuCells[i][j].setEditable(false);
                }
            }
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
