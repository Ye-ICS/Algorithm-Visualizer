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
import javafx.scene.text.TextFlow;

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
        setStyle("-fx-background-color: #f4f4f9;"); // Light background for modern look

        setSpacing(30);
        // Title Text
         Text titleText = new Text("AES Algorithm Visualization");
         titleText.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #333333;");
         titleText.setTextAlignment(TextAlignment.CENTER);

        // Description Box
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setPrefSize(600, 200);
        Text descriptionText = new Text(
                "Welcome to the AES-256 Encryption Algorithm Visualizer! AES or Advanced Encryption Standard Algorithm is a very sophisticated encryption algorithm used by governments, nmillitaries and banks to encrypt some of the most confidential information on earth. It is a very complicated process that we will try to simplify throught this animation. Sit back, relax and Enjoy the encryption :)\n\n\nEnter your password and plaintext message below.");
        descriptionText.setStyle("-fx-padding: 10px;");
        descriptionText.setStyle("-fx-font-size: 18px; -fx-fill: #666666;");
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        javafx.scene.text.TextFlow description = new javafx.scene.text.TextFlow(descriptionText);
        description.setTextAlignment(TextAlignment.CENTER);
        description.prefWidthProperty().bind(descriptionBox.widthProperty());

        // Use TextFlow for wrapping and resizing
        TextFlow descriptionFlow = new TextFlow(descriptionText);
        descriptionFlow.setTextAlignment(TextAlignment.CENTER);
        descriptionFlow.prefWidthProperty().bind(descriptionBox.widthProperty());
        descriptionText.wrappingWidthProperty().bind(descriptionBox.widthProperty().subtract(20)); // Padding
        descriptionBox.getChildren().add(descriptionFlow);

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
        getChildren().addAll(titleText, descriptionText);
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

