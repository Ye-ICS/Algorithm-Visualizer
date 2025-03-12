import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.awt.Toolkit;
import javafx.scene.text.TextAlignment;

public class AEStart extends VBox {
    private static String password = ""; // Store the password

    public static String getPassword() {
        return password;
    }

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

        javafx.scene.text.TextFlow description = new javafx.scene.text.TextFlow(descriptionText);
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
        getStylesheets().add(getClass().getResource("css/StartButton.css").toExternalForm()); // Load CSS file


        startBtn.setOnAction(event -> {
            password = passwordBox.getText(); // Store the password
            if (password.length() == 0) {
                return;
            }
            if (password.length()>16)
            {
                Toolkit.getDefaultToolkit().beep();
                showError("Password length should be less than 16 characters");
                return;
            }
            FXUtils.setSceneRoot(getScene(), new AESPasswordArray(password)); // Pass password to next scene
        });

        passBox.getChildren().add(passwordBox);
        descriptionBox.getChildren().add(description);
        startBox.getChildren().add(startBtn);
        getChildren().addAll(descriptionBox, passBox, startBtn);
    }

        private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
