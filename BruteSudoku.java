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

    /**
     * Constructs layout for brute force sudoku solver
     */
    BruteSudoku() {

        Text title = new Text("Brute Force Sudoku Solver");

        GridPane sudokuTable = new GridPane();
        sudokuTable.setPadding(new Insets(10));
        sudokuTable.setVgap(2);
        sudokuTable.setHgap(2);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField sudokuNumbers = new TextField();
                sudokuNumbers.setPrefHeight(40);
                sudokuNumbers.setPrefWidth(40);


                sudokuCells[i][j] = sudokuNumbers;
                sudokuTable.add(sudokuNumbers, i, j);
            }
        }

        setAlignment(Pos.CENTER);

        fillRandomNumbers();

        Text description = new Text("");

        Button sudokuBox = new Button("");
        

        Button backsBtn = new Button("Back");
        backsBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(title, backsBtn, sudokuTable);
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
