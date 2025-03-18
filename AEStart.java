// AESStart.java
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
    private static String password = "";
    private static String plaintext = ""; // Store plaintext

    public static String getPassword() {
        return password;
    }

    public static String getPlaintext() {
        return plaintext;
    }

    @SuppressWarnings("unused")
    AEStart() {
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(50);

        // Description Box
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setPrefSize(600, 200);
        Text descriptionText = new Text(
                "Welcome to the AES-256 Encryption Algorithm Visualizer! Enter your password and plaintext message below.");
        descriptionText.setStyle("-fx-padding: 10px;");
        javafx.scene.text.TextFlow description = new javafx.scene.text.TextFlow(descriptionText);
        description.setTextAlignment(TextAlignment.CENTER);
        description.prefWidthProperty().bind(descriptionBox.widthProperty());

        // Password Input
        VBox passBox = new VBox();
        passBox.setAlignment(Pos.CENTER);
        passBox.setSpacing(10);
        TextField passwordBox = new TextField();
        passwordBox.setMaxWidth(200);
        passwordBox.setPromptText("Enter Password (Max 16 Characters)");

        // Plaintext Input
        TextField plaintextBox = new TextField();
        plaintextBox.setMaxWidth(200);
        plaintextBox.setPromptText("Enter Plaintext");

        // Start Button
        VBox startBox = new VBox();
        startBox.setAlignment(Pos.CENTER);
        startBox.setSpacing(10);
        Button startBtn = new Button("Start Animation !");
        startBtn.setMinSize(200, 50);
        startBtn.getStyleClass().add("StartButton");
        getStylesheets().add(getClass().getResource("css/StartButton.css").toExternalForm());

        startBtn.setOnAction(event -> {
            password = passwordBox.getText();
            plaintext = plaintextBox.getText();

            if (password.length() == 0 || plaintext.length() == 0) {
                showError("Both password and plaintext are required.");
                return;
            }

            if (password.length() > 16) {
                Toolkit.getDefaultToolkit().beep();
                showError("Password length should be 16 characters or less.");
                return;
            }

            FXUtils.setSceneRoot(getScene(), new AESPasswordArray(password, plaintext));
        });

        passBox.getChildren().addAll(passwordBox, plaintextBox);
        descriptionBox.getChildren().add(description);
        startBox.getChildren().add(startBtn);
        getChildren().addAll(descriptionBox, passBox, startBox);
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

// Next, I will adjust AESPasswordArray.java to visualize both password and plaintext in a 4x4 matrix.
