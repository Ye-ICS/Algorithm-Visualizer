import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class AESPasswordArray extends VBox { // A custom JavaFX component

    private static final int BLOCK_SIZE = 16; // AES block size (16 bytes)
    private static final int GRID_SIZE = 4; // 4x4 grid

    /**
     * Constructor: Accepts a password, converts it into a 4x4 hex grid
     * (column-wise), and displays it.
     * 
     * @param password The input string to be converted into a hex grid.
     */
    public AESPasswordArray(String password) {
        List<String> hexBytes = convertToHexBytes(password); // Convert password to hex byte array
        String[][] grid = fillGridColumnWise(hexBytes); // Fill the 4x4 grid column-wise
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setPrefSize(600, 200);
        this.setPrefSize(400, 300); // Adjust the width and height as needed
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30); // Set vertical spacing between elements
        
        



        VBox textBox = new VBox();
        textBox.setAlignment(Pos.CENTER); 
        Text descriptionText = new Text(
                "Now that you have entered your password, AES-256 will convert the plaintext password into hexadecimal before creating a 4x4 column-major order array composed of 16 bytes. Each byte is represented by two hexadecimal characters. The array is then used (after many transformations) generate the encryption key.");
        descriptionText.setStyle("-fx-padding: 10px;");
        this.getChildren().add(descriptionText);
        javafx.scene.text.TextFlow description = new javafx.scene.text.TextFlow(descriptionText);
        description.setTextAlignment(TextAlignment.CENTER);
        description.prefWidthProperty().bind(descriptionBox.widthProperty());
        descriptionBox.getChildren().add(description);
        this.getChildren().add(descriptionBox);

        
        // Display initial hex sequence as a single line
        Label hexLine = new Label(String.join(" ", hexBytes));
        hexLine.setStyle(
                "-fx-font-size: 14px; -fx-padding: 10px; -fx-border-color: black; -fx-background-color: white; -fx-border-width: 1px; ");
        this.getChildren().add(hexLine);

        // Pause before moving to the grid
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        // Create the grid layout
        HBox gridContainer = new HBox();
        VBox gridWrapper = new VBox();
        gridWrapper.setAlignment(Pos.CENTER);
        gridContainer.setAlignment(Pos.CENTER);
        List<TranslateTransition> animations = new ArrayList<>();

        for (int col = 0; col < GRID_SIZE; col++) {
            VBox colBox = new VBox(); // Create a horizontal row
            colBox.setSpacing(10);
            colBox.setPadding(new Insets(5, 5, 5, 5));

            for (int row = 0; row < GRID_SIZE; row++) {
                Label cell = new Label(grid[row][col]); // Get hex value
                cell.setStyle("-fx-border-color: black; -fx-padding: 5px;"); // Add border
                cell.setOpacity(0); // Initially hidden
                colBox.getChildren().add(cell);

                // Create animation for each cell
                TranslateTransition anim = new TranslateTransition(Duration.seconds(1), cell);
                anim.setFromY(-20); // Start slightly above
                anim.setToY(0);
                anim.setOnFinished(e -> cell.setOpacity(1)); // Make visible after animation
                animations.add(anim);
            }
            gridContainer.getChildren().add(colBox); // Add row to VBox
        }
        this.getChildren().add(gridContainer);

        // Play animations sequentially after pause
        SequentialTransition sequence = new SequentialTransition(pause);
        sequence.getChildren().addAll(animations);
        sequence.play();
    }

    /**
     * Converts the password into a list of hexadecimal byte strings (each byte is 2
     * hex characters).
     * If there are fewer than 16 bytes, it is padded with "00".
     * 
     * @param text The input password string.
     * @return A list of 16 hex byte strings.
     */
    private List<String> convertToHexBytes(String text) {
        List<String> hexBytes = new ArrayList<>(); // List to store hex bytes
        for (char c : text.toCharArray()) {
            hexBytes.add(String.format("%02x", (int) c)); // Convert char to 2-digit hex
        }

        // Pad with "00" if length < 16
        while (hexBytes.size() < BLOCK_SIZE) {
            hexBytes.add("00");
        }
        return hexBytes;
    }

    /**
     * Fills a 4x4 grid column-wise with hex byte strings.
     * AES fills the state matrix column by column, not row by row.
     * 
     * @param hexBytes A list of 16 hex byte strings.
     * @return A 4x4 grid of hex values.
     */
    private String[][] fillGridColumnWise(List<String> hexBytes) {
        String[][] grid = new String[GRID_SIZE][GRID_SIZE];

        int index = 0;
        for (int col = 0; col < GRID_SIZE; col++) { // Column-wise filling
            for (int row = 0; row < GRID_SIZE; row++) {
                grid[row][col] = hexBytes.get(index++); // Fill row first in each column
            }
        }
        return grid;
    }
}
