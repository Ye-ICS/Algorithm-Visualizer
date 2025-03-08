import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A* Pathfinding Visualization UI.
 */
public class AStar extends BorderPane {
    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;

    private static Cell[][] grid = new Cell[GRID_SIZE][GRID_SIZE];
    private static Cell startNode = null;
    private static Cell endNode = null;

    public AStar() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Cell cell = new Cell(row, col);
                grid[row][col] = cell;
                gridPane.add(cell.getRectangle(), col, row);
            }
        }
        
        Button aStarButton = new Button("Run A*");
        Button clearButton = new Button("Clear Grid");
        Button backButton = new Button("Back");

        HBox buttonBox = new HBox(10, aStarButton, clearButton, backButton);
        setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);

        setBottom(buttonBox);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        // Back button returns to the main menu
        backButton.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        clearButton.setOnAction(event -> resetGrid());
        aStarButton.setOnAction(event -> RunAStar());

    } 
    
    private static double heuristic(Cell a, Cell b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    public static void resetGrid(){
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col].reset();
            }
        }
        startNode = null;
        endNode = null;
        System.out.println("Grid Cleared");
    }

    public static void RunAStar(){

    }

    private class Cell {
        private int row, col;
        private Rectangle rect;
        private boolean isWall = false;
        private double distance = Double.MAX_VALUE;
        private Cell parent = null;

        public Cell(int row, int col){
            this.row = row;
            this.col = col;
            this.rect = new Rectangle(CELL_SIZE, CELL_SIZE, Color.LIGHTGRAY);
            this.rect.setStroke(Color.BLACK);

            rect.setOnMouseClicked(event -> cellClicked());
        }

        public Rectangle getRectangle(){
            return rect;
        }

        public boolean isWall(){
            return isWall;
        }

        public void setWall(boolean wall){
            isWall = wall;
            if (wall){
                rect.setFill(Color.BLACK);
            } else {
                rect.setFill(Color.LIGHTGRAY);
            }
        }

        public void reset() {
            isWall = false;
            rect.setFill(Color.LIGHTGRAY);
            distance = Double.MAX_VALUE;
            parent = null;
        }

        private void cellClicked() {
            if (startNode == null) {
                startNode = this;
                rect.setFill(Color.GREEN);
            } else if (endNode == null) {
                endNode = this;
                rect.setFill(Color.RED);
            } else {
                setWall(!isWall);
            }
        }
    }
}








