import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MenuLayout extends VBox { 

    public MenuLayout() {
        
        TextField word1Input = new TextField();
        word1Input.setPromptText("Enter first word");

        TextField word2Input = new TextField();
        word2Input.setPromptText("Enter second word");

        Button calcButton = new Button("Calculate Edit Distance");
        Button clearButton = new Button("Clear");

        Label resultLabel = new Label("Edit Distance: ");

        calcButton.setOnAction(e -> {
            String w1 = word1Input.getText();
            String w2 = word2Input.getText();
            int distance = EditDistanceC.computeEditDistance(w1, w2);
            resultLabel.setText("Edit Distance: " + distance);
        });

        clearButton.setOnAction(e -> {
            word1Input.clear();
            word2Input.clear();
            resultLabel.setText("Edit Distance: ");
        });

        this.setSpacing(10);
        this.setStyle("-fx-padding: 20; -fx-alignment: center;");
        this.getChildren().addAll(word1Input, word2Input, calcButton, clearButton, resultLabel);
    }
}
