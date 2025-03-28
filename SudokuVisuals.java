import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

public class SudokuVisuals {

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

    public static void innerCellDesign(Rectangle innerCell, int i, int j, Node cellStack) {
        
        // Create inner cell with rounded corners
        innerCell.setFill(Color.WHITE);
        innerCell.setStroke(Color.LIGHTGRAY);
        innerCell.setStrokeWidth(1);
        innerCell.setArcWidth(5);
        innerCell.setArcHeight(5);
        
        // Add inner shadow for depth
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(2);
        innerShadow.setColor(Color.rgb(0, 0, 0, 0.05));
        innerCell.setEffect(innerShadow);

        // Add shadow effect to cell
        DropShadow cellShadow = new DropShadow();
        cellShadow.setRadius(5);
        cellShadow.setColor(Color.rgb(0, 0, 0, 0.1));
        cellStack.setEffect(cellShadow);
    }

    public static void outerCellDesign(Shape outerCell) {
        
        outerCell.setFill(Color.TRANSPARENT);
        outerCell.setStroke(Color.rgb(52, 73, 94)); // Dark blue-gray color
        outerCell.setStrokeWidth(3);
        ((Rectangle) outerCell).setArcWidth(15);
        ((Rectangle) outerCell).setArcHeight(15);
        
        // Add shadow effect to outer grid
        DropShadow outerShadow = new DropShadow();
        outerShadow.setRadius(8);
        outerShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        outerCell.setEffect(outerShadow);
    }
}