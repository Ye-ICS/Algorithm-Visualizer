import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class AESPasswordArray extends VBox { // A custom JavaFX component

    private static final int BLOCK_SIZE = 16; // AES block size (16 bytes)
    private static final int GRID_SIZE = 4;  // 4x4 grid

    /**
     * Constructor: Accepts a password, converts it into a 4x4 hex grid (column-wise), and displays it.
     * @param password The input string to be converted into a hex grid.
     */
    public AESPasswordArray(String password) {
        List<String> hexBytes = convertToHexBytes(password); // Convert password to hex byte array
        String[][] grid = fillGridColumnWise(hexBytes); // Fill the 4x4 grid column-wise

        // Display initial hex sequence as a single line
        Label hexLine = new Label(String.join(" ", hexBytes));
        hexLine.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        this.getChildren().add(hexLine);

        // Pause before moving to the grid
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        // Create the grid layout
        VBox gridContainer = new VBox();
        List<TranslateTransition> animations = new ArrayList<>();

        for (int row = 0; row < GRID_SIZE; row++) {
            HBox rowBox = new HBox(); // Create a horizontal row
            rowBox.setSpacing(10);
            rowBox.setPadding(new Insets(5, 5, 5, 5));

            for (int col = 0; col < GRID_SIZE; col++) {
                Label cell = new Label(grid[row][col]); // Get hex value
                cell.setStyle("-fx-border-color: black; -fx-padding: 5px;"); // Add border
                cell.setOpacity(0); // Initially hidden
                rowBox.getChildren().add(cell);

                // Create animation for each cell
                TranslateTransition anim = new TranslateTransition(Duration.seconds(1), cell);
                anim.setFromY(-20); // Start slightly above
                anim.setToY(0);
                anim.setOnFinished(e -> cell.setOpacity(1)); // Make visible after animation
                animations.add(anim);
            }
            gridContainer.getChildren().add(rowBox); // Add row to VBox
        }
        this.getChildren().add(gridContainer);

        // Play animations sequentially after pause
        SequentialTransition sequence = new SequentialTransition(pause);
        sequence.getChildren().addAll(animations);
        sequence.play();
    }

    /**
     * Converts the password into a list of hexadecimal byte strings (each byte is 2 hex characters).
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
