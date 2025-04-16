import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
   
        Button easyButton = createDifficultyButton("Easy", "#27ae60", "#219955");
        Button mediumButton = createDifficultyButton("Medium", "#f39c12", "#d58512");
        Button hardButton = createDifficultyButton("Hard", "#e74c3c", "#c0392b");

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

    public static Button createDifficultyButton(String text, String baseColor, String hoverColor) {
                
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        button.setPrefSize(300, 140);
        button.setStyle(
            "-fx-background-color: " + baseColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10px;"
        );
       
        // Add hover effect
        button.setOnMouseEntered(e ->
            button.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10px;" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);"
            )
        );
       
        // Reset on mouse exit
        button.setOnMouseExited(e ->
            button.setStyle(
                "-fx-background-color: " + baseColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);"
            )
        );
       
        // Add pressed effect
        button.setOnMousePressed(e ->
            button.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10px;" +
                "-fx-scale-x: 0.98;" +
                "-fx-scale-y: 0.98;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 0);"
            )
        );
       
        // Reset on mouse release
        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle(
                    "-fx-background-color: " + hoverColor + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-scale-x: 1.05;" +
                    "-fx-scale-y: 1.05;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);"
                );
            } else {
                button.setStyle(
                    "-fx-background-color: " + baseColor + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);"
                );
            }
        });
       
        // Add drop shadow
        button.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.2)));
       
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

    public static void speedSlider(Label speedLabel, Slider speedSlider) {
                
        speedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        speedLabel.setStyle("-fx-text-fill: black;");

        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);
        speedSlider.setStyle("-fx-text-fill:black; -fx-font-size: 18px;");
    }

    public static void buttonStyle(Node solveButton, Node resetButton, Node backButton) {
                            
        solveButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 160px; -fx-background-color: #32CD32; -fx-text-fill: white;");
        resetButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 80px; -fx-background-color: #FFA500; -fx-text-fill: white;");
        backButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 80px; -fx-background-color: #FF4500; -fx-text-fill: white;");
    }
}