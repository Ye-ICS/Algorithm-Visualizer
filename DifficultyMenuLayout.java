import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DifficultyMenuLayout extends VBox {

    public DifficultyMenuLayout() {
        getStylesheets().add(getClass().getResource("css/SudokuStyle.css").toExternalForm());
        getStyleClass().add("root"); 
        
        showDifficultySelection();
    }

    public void showDifficultySelection() {
        StackPane container = new StackPane(); // Wrap VBox for centering
        VBox difficultySelection = new VBox(20);
        difficultySelection.setAlignment(Pos.CENTER);
   
        // Match Sudoku grid's window size
        int width = 577;  // Match Sudoku width
        int height = 752; // Match Sudoku height
   
        // Set window size to match the Sudoku UI
        Platform.runLater(() -> {
            getScene().getWindow().setWidth(width);
            getScene().getWindow().setHeight(height);
        });
   
        Label difficultyLabel = SudokuVisuals.difficultyLabel();
   
        Button easyButton = SudokuVisuals.createDifficultyButton("Easy", "#27ae60", "#219955");
        Button mediumButton = SudokuVisuals.createDifficultyButton("Medium", "#f39c12", "#d58512");
        Button hardButton = SudokuVisuals.createDifficultyButton("Hard", "#e74c3c", "#c0392b");

        easyButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Easy.txt")));
        mediumButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Medium.txt")));
        hardButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Hard.txt")));
    
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        backButton.getStyleClass().add("back-button"); // Apply CSS class to the back button
    
        difficultySelection.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton, backButton);
        container.getChildren().add(difficultySelection); // Center VBox inside StackPane

        // Ensure the entire StackPane itself is centered in the GridPane
        setAlignment(Pos.CENTER);
        getChildren().add(container);
    }
}