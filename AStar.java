
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

    public AStar() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE, Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);
                gridPane.add(cell, col, row);
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
    }    
}

