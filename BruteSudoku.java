import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class BruteSudoku extends FlowPane{
    


    /**
     * Constructs layout for brute force sudoku solver
     */
    BruteSudoku() {
        GridPane sudokuTable = new GridPane();
        sudokuTable.setPadding(new Insets(10));
        sudokuTable.setVgap(5);
        sudokuTable.setHgap(5);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; i++) {
                TextField sudokuNumbers = new TextField();
                sudokuNumbers.setPrefHeight(40);
                sudokuNumbers.setPrefWidth(40);

                if (i == 0 && j == 0) {
                    sudokuNumbers.setText("5");
                    sudokuNumbers.setEditable(false);
                }

                sudokuTable.add(sudokuNumbers, i, j);
            }
        }

        setAlignment(Pos.CENTER);

        Text description = new Text("");

        Button sudokuBox = new Button("");
        

        Button backsBtn = new Button("Back");
        backsBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(description, backsBtn);
    }
}