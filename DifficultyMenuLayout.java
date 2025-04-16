import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

        Label difficultyLabel = difficultyLabel();

        Button easyButton = createDifficultyButton("Easy", "easy-button");
        Button mediumButton = createDifficultyButton("Medium", "medium-button");
        Button hardButton = createDifficultyButton("Hard", "hard-button");

        easyButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Easy.txt")));
        mediumButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Medium.txt")));
        hardButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new SudokuVisuals("data/sudoku/Hard.txt")));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        backButton.getStyleClass().add("back-button"); // Apply CSS class to the back button

        difficultySelection.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton, backButton);
        container.getChildren().add(difficultySelection); // Center VBox inside StackPane

        // Ensure the entire StackPane itself is centered in the GridPane
        setAlignment(Pos.CENTER);
        getChildren().add(container);
    }

    public static Button createDifficultyButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().addAll("difficulty-button", styleClass);
        return button;
    }

    public static Label difficultyLabel() {
                
        // Label for the difficulty selection screen
        Label difficultyLabel = new Label("Choose Sudoku Difficulty");
        difficultyLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
        difficultyLabel.setTextFill(Color.WHITE);
        difficultyLabel.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.5)));
        return difficultyLabel;
    }
}