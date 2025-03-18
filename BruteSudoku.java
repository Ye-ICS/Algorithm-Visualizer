import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class BruteSudoku extends FlowPane{
    
    TextField[][] sudokuCells = new TextField[9][9];

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

                if (i == 0 && j == 0) {
                    sudokuNumbers.setText("5"); // math.random this later
                    sudokuNumbers.setEditable(false);
                }
                sudokuCells[i][j] = sudokuNumbers;
                sudokuTable.add(sudokuNumbers, i, j);
            }
        }

        setAlignment(Pos.CENTER);

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
}