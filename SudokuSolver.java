import javax.swing.JSlider;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
// note the  negative numbers are to distinguish the difference between set values and potential solutions.
public class SudokuSolver extends FlowPane{

    SudokuSolver() {
            GridPane grid = new GridPane();
         


        
        int[][] board = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                board[i][j] = 0;
            }
        }

        board[0][0] = 5;
        board[0][3] = 8;
        board[0][4] = 3;
        board[0][5] = 2;
        board[0][6] = 4;
        board[0][8] = 6;
        board[1][1] = 6;
        board[1][2] = 3;
        board[1][3] = 7;
        board[1][4] = 4;
        board[2][0] = 8;
        board[2][2] = 2;
        board[2][3] = 1;
        board[2][4] = 9;
        board[2][8] = 3;
        board[3][1] = 3;
        board[3][4] = 2;
        board[3][5] = 9;
        board[3][6] = 1;
        board[3][8] = 5;
        board[4][0] = 1;
        board[4][5] = 8;
        board[4][6] = 9;
        board[4][7] = 6;
        board[4][8] = 2;
        board[5][3] = 5;
        board[5][7] = 7;
        board[6][4] = 1;
        board[6][7] = 2;
        board[6][8] = 7;
        board[7][1] = 2;
        board[7][2] = 6;
        board[7][6] = 5;
        board[8][0] = 3;
        board[8][1] = 1;
        board[8][2] = 8;
        board[8][4] = 5;
        board[8][7] = 4;
        

        int gCoordinate = 0;
        

        TextField[][] fields = new TextField[9][9];

        int delay = 50;
        // generating text boxes

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){

                if(board[i][j] != 0){
                    fields[i][j] = new TextField("" + board[i][j]);
                    grid.add(fields[i][j], i, j);
                }
                else{
                    fields[i][j] = new TextField("_");
                    grid.add(fields[i][j], i, j);
                    
                }
                fields[i][j].setMaxWidth(40.0);
            }
        }
        Slider slider = new Slider(1, 100, 50);
        slider.setShowTickMarks(true);  // Show actual ticks
        slider.setMajorTickUnit(1);     // Major tick spacing
        slider.setBlockIncrement(1);   // Increment per key press
        

        // Add listener to update TextField when the slider value changes
        grid.setVgap(10);
        grid.setHgap(15);

        setAlignment(Pos.CENTER);
        Button backBtn = new Button("Back");
        Button start = new Button("start");
        Button resetBtn = new Button("reset");
        Text speed_text = new Text("Speed!!!");
        VBox speed = new VBox();

        speed.getChildren().addAll(speed_text, slider);

        start.setOnAction(event -> {
                    Thread work = new Thread(() -> {
            solve(board, gCoordinate, fields, slider);
         });
            updateBoard(board, fields); work.start();;});
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        resetBtn.setOnAction(event -> {reset(board, fields);;});

        getChildren().addAll(grid, backBtn, start, resetBtn, speed);

    }
    //Main function
    boolean solve(int board[][],int coordinate, TextField[][] fields, Slider slider){
        updateGrid(board, fields);
        try {
            // made so that right is faster
            Thread.sleep(3000/(long)slider.getValue());
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        System.out.println("coordinate: " + coordinate);
        int x = coordinate / 9;
        int y = coordinate % 9;
        int new_coordinate = 0;
        for(int i = coordinate + 1; i <= 80; i++){
            if(board[i / 9][i % 9] <= 0){
                new_coordinate = i;
                break;
            }
        }
        for(int i = 1; i <= 9; i++){
            if(check(board, coordinate / 9, coordinate % 9, i)){
                board[x][y] = -i;
                if(coordinate == 80){
                    System.out.println("done");
                    return true;
                }
                //for if its all done
                if(solve(board, new_coordinate, fields, slider)){
                    return true;
                }
            }
            board[x][y] = 0;
        }
        if(coordinate == 0){
            System.out.println("unsolvable");
        }
        return false;
    }
    //checking if the board posistion is valid
    private static boolean check(int[][] board, int x, int y, int num){
        for(int i = 0; i < 9; i++){
            if(Math.abs(board[x][i]) == num || Math.abs(board[i][y]) == num || boxContains(board, x, y, num)){
                return false;
            }
        }
        return true;
    }
    //checks for others of the same number in the same 3x3 grid
    private static boolean boxContains(int[][] board, int x, int y, int num){
        for(int i = 0; i < 9; i++){
            if(Math.abs(board[x/3 * 3 + i / 3][y/3 * 3 + i % 3]) == num){
                return true;
            }
        }
        return false;

    }
    private static void reset(int[][] board, TextField[][] fields){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                board[i][j] = 0;
                updateGrid(board, fields);
            }
        }
    }
private static void updateGrid(int[][] board, TextField[][] fields) {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            int value = Math.abs(board[i][j]);
            TextField tf = fields[i][j];  // this is effectively final
            System.out.print(value);
            if (j == 8) System.out.println();

            Platform.runLater(() -> {
                if (value > 0) {
                    tf.setText("" + value);
                } else {
                    tf.setText("_");
                }
            });
        }
    }
}
    private static void updateBoard(int[][] board, TextField[][] fields){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                try{
                    board[i][j] = Integer.parseInt(fields[i][j].getText());
                }
                catch(Exception NumberFormatException){
                    board[i][j] = 0;
                }

            }
        }
    }
    
}
