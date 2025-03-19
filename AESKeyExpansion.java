// Import necessary classes from the JavaFX library for creating the UI elements
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

// Import classes for handling cryptographic operations
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class AESKeyExpansion extends VBox {

    // Constructor that sets up the AESKeyExpansion scene and initializes UI components
    @SuppressWarnings("unused")
    public AESKeyExpansion(String password) throws RuntimeException {
        // Set alignment and size for the VBox container
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
        
        // Set the background color of the VBox to light blue
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Set the spacing between child elements in the VBox
        setSpacing(50);

        // Create a title label and style it
        Label title = new Label(bytesToHex(get256BitKey(password))); // Display the 256-bit key as a hexadecimal string
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create a Back button to navigate back to the AESPasswordArray screen
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("nav-button"); // Apply CSS class for styling
        // Set action on the back button to return to the AESPasswordArray screen
        backButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AESPasswordArray(AEStart.getPassword(), ""))); 

        // Add the title label and back button to the VBox container
        getChildren().addAll(title, backButton);
    }

    // Method to generate a 256-bit key from a password using the SHA-256 hashing algorithm
    public static byte[] get256BitKey(String password) {
        try {
            // Initialize the MessageDigest instance with the SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Digest the password and return the 32-byte hash as the key
            return digest.digest(password.getBytes(StandardCharsets.UTF_8)); // 32 bytes output
        } catch (NoSuchAlgorithmException e) {
            // Handle the case where the SHA-256 algorithm is not available
            throw new RuntimeException("SHA-256 Algorithm Not Found");
        }
    }

    // Method to convert byte array to hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Converts each byte to 2-char hex
        }
        return hexString.toString();
    }
     // Main method to test the get256BitKey method
}