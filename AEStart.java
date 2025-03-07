import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;

/**
 * Placeholder. May delete later.
 */
public class AEStart extends VBox {
    AEStart() {
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(50); // Set vertical spacing between elements
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setPrefSize(600, 200);

        Text descriptionText = new Text(
                "Welcome to our AES-256 Encryption Algorithm Visualizer! AES-256 (Advanced Encryption Standard) is a military standard encryption algorithm used to protect the most sensitive data on the planet. Not only that, you use it every day to protect your ZIP files! Go ahead and enter your password here and click the button below:");
        descriptionText.setStyle("-fx-padding: 10px;");
        
        TextFlow description = new TextFlow(descriptionText);
        description.setTextAlignment(TextAlignment.CENTER);
        description.prefWidthProperty().bind(descriptionBox.widthProperty());



        VBox passBox = new VBox();
        passBox.setAlignment(Pos.CENTER);
        passBox.setSpacing(10);
        TextField passwordBox = new TextField();
        passwordBox.setMaxWidth(200);
        passwordBox.setStyle("-fx-padding: 10px;");

        VBox startBox = new VBox();
        startBox.setAlignment(Pos.CENTER);
        startBox.setSpacing(10);
        
        Button startBtn = new Button("Start Animation !");
        startBtn.setMinSize(200, 50); // Adjust as needed
        startBtn.getStyleClass().add("StartButton"); // Apply CSS class
        
        // Load the CSS file
        startBox.getStylesheets().add(getClass().getResource("CSS/StartButton.css").toExternalForm());

        startBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        passBox.getChildren().add(passwordBox);
        descriptionBox.getChildren().add(description);
        startBox.getChildren().add(startBtn);
        getChildren().addAll(descriptionBox, passBox, startBtn);
    }
}