import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

public class AESPasswordArray extends VBox { // A custom JavaFX component that extends VBox

    private static final int BLOCK_SIZE = 16; // AES block size (16 bytes)

    /**
     * Constructor: Accepts a password, converts it into hex blocks, and displays them.
     * @param password The input string to be converted into hex blocks.
     */
    public AESPasswordArray(String password) { 
        List<String> hexBlocks = convertToHexBlocks(password); // Convert the password into hex blocks

        // Display each hex block as a label inside the VBox
        for (String block : hexBlocks) {
            Label label = new Label(block); // Create a label with hex block text
            this.getChildren().add(label); // Add label to VBox (this component)
        }
    }

    /**
     * Converts a given string into a list of hexadecimal blocks, each of size 16 bytes.
     * If a block is smaller than 16 bytes, it is padded with "00".
     * 
     * @param text The input string to be converted.
     * @return List of hex-encoded 16-byte blocks.
     */
    private List<String> convertToHexBlocks(String text) {
        List<String> blocks = splitIntoBlocks(text, BLOCK_SIZE); // Split text into 16-byte blocks
        List<String> hexBlocks = new ArrayList<>(); // List to store hex representations

        for (String block : blocks) {
            StringBuilder hexString = new StringBuilder();

            // Convert each character to its hexadecimal representation
            for (char c : block.toCharArray()) {
                String hex = String.format("%02x", (int) c); // Convert char to 2-digit hex
                hexString.append(hex);
            }

            // Pad the block with "00" if it's smaller than 16 bytes
            while (hexString.length() < BLOCK_SIZE * 2) { // *2 because each byte is 2 hex chars
                hexString.append("00");
            }

            hexBlocks.add(hexString.toString()); // Add hex block to the list
        }
        return hexBlocks;
    }

    /**
     * Splits the input string into blocks of a specified size.
     * 
     * @param text The input string.
     * @param blockSize The desired block size.
     * @return List of string blocks, each up to blockSize characters long.
     */
    private List<String> splitIntoBlocks(String text, int blockSize) {
        List<String> blocks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += blockSize) {
            int endIndex = Math.min(i + blockSize, text.length()); // Ensure we don't exceed text length
            blocks.add(text.substring(i, endIndex)); // Extract a block of text
        }
        return blocks;
    }
}
