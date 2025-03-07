import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

public class AESPasswordArray extends VBox {

    private static final int BLOCK_SIZE = 16;

    public AESPasswordArray(String password) { // Accept password as a parameter
        List<String> hexBlocks = convertToHexBlocks(password); // Convert to hex blocks

        // Add labels to the VBox to display the hex blocks
        for (String block : hexBlocks) {
            Label label = new Label(block);
            this.getChildren().add(label); // Add label to the VBox
        }
    }

    private List<String> convertToHexBlocks(String text) {
        List<String> blocks = splitIntoBlocks(text, BLOCK_SIZE);
        List<String> hexBlocks = new ArrayList<>();

        for (String block : blocks) {
            StringBuilder hexString = new StringBuilder();
            for (char c : block.toCharArray()) {
                String hex = String.format("%02x", (int) c); // Ensure two-digit hex
                hexString.append(hex);
            }

            // Pad with "00" if the block is less than 16 bytes
            while (hexString.length() < BLOCK_SIZE) { // *2 because each byte is 2 hex chars
                hexString.append("00");
            }

            hexBlocks.add(hexString.toString());
        }
        return hexBlocks;
    }

    private List<String> splitIntoBlocks(String text, int blockSize) {
        List<String> blocks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += blockSize) {
            int endIndex = Math.min(i + blockSize, text.length());
            blocks.add(text.substring(i, endIndex));
        }
        return blocks;
    }
}
