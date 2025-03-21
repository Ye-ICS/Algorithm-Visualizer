import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

// Import cryptographic classes for SHA-256 hashing
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class AESKeyExpansion extends VBox {

    // Constructor to initialize the scene with key expansion and animation
    public AESKeyExpansion(String password, String plaintext) throws RuntimeException {
        // Set up VBox container and style
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
        setSpacing(50);
        setBackground(new Background(new BackgroundFill(Color.web("#f4f4f9"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Create a title label
        Label title = new Label("AES Key Expansion (First Two Keys)");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: black;");

        // Description text for key expansion process
        Text description = new Text("AES-256 generates 15 round keys from the initial 256-bit key.\n" +
                                    "The first two round keys are displayed as 4x4 matrices.");
        description.setStyle("-fx-font-size: 16px; -fx-fill: black;");
        
        // Generate the 256-bit key from password and split it into matrices
        byte[] key = get256BitKey(password);
        int[][][] keys = splitKeyIntoMatrices(key); // Split into two 4x4 matrices

        // Create labels to display the two 4x4 keys (Key 0 and Key 1)
        Label key0Label = createMatrixLabel(keys[0]);
        Label key1Label = createMatrixLabel(keys[1]);

        // Add the title, description, and key labels to the VBox
        getChildren().addAll(title, description, key0Label, key1Label);

        // Add a back button to navigate back
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("nav-button");
        backButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AESPasswordArray(AEStart.getPassword(), AEStart.getPlaintext())));
        getChildren().add(backButton);

        // Add fade animation for a smoother UI transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // Method to generate a 256-bit key from a password using SHA-256 hashing
    public static byte[] get256BitKey(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8)); // Returns 32 bytes
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm Not Found");
        }
    }

    // Method to convert byte array to hex string (optional, if you want to display the raw key)
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Method to split the 256-bit key into two 4x4 matrices
    public static int[][][] splitKeyIntoMatrices(byte[] key) {
        int[][] key0 = new int[4][4];
        int[][] key1 = new int[4][4];

        // Fill the first 16 bytes into Key 0
        for (int i = 0; i < 16; i++) {
            key0[i % 4][i / 4] = key[i] & 0xFF;
        }

        // Fill the next 16 bytes into Key 1
        for (int i = 16; i < 32; i++) {
            key1[i % 4][(i - 16) / 4] = key[i] & 0xFF;
        }

        return new int[][][] { key0, key1 };
    }

        private VBox createGridContainer(String[][] grid, String labelText, int GRID_SIZE) {
                VBox container = new VBox(10);
                container.setAlignment(Pos.CENTER);
                Label label = new Label(labelText);
                label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
                container.getChildren().add(label);
        
                HBox gridContainer = new HBox();
                gridContainer.setStyle("-fx-border-color: rgb(30, 70, 170); -fx-border-width: 3px;");
                gridContainer.setAlignment(Pos.CENTER);
        
                for (int col = 0; col < GRID_SIZE; col++) {
            VBox colBox = new VBox();
            colBox.setSpacing(5);
            for (int row = 0; row < GRID_SIZE; row++) {
                Label cell = new Label(grid[row][col]);
                cell.setMinSize(40, 40);
                cell.setStyle("-fx-border-color: green; -fx-border-width: 1px; -fx-background-color: white; -fx-font-size: 17px; -fx-text-fill: #2c3e50;");
                colBox.getChildren().add(cell);
            }
            gridContainer.getChildren().add(colBox);
        }

        container.getChildren().add(gridContainer);
        return container;
    }

    // AES S-Box for SubWord
    private static final int[] SBOX = {
        0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
        0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
        0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
        0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
    };

    // Rcon table
    private static final int[] RCON = {
        0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36
    };


    // RotWord - Rotate 4-byte word left by 1 byte
    public static int[] rotWord(int[] word) {
        return new int[]{word[1], word[2], word[3], word[0]};
    }

    // SubWord - Apply AES S-Box substitution
    public static int[] subWord(int[] word) {
        int[] newWord = new int[4];
        for (int i = 0; i < 4; i++) {
            newWord[i] = SBOX[(word[i] & 0xFF) % SBOX.length];
        }
        return newWord;
    }

    // Rcon function - Generate round constants
    public static int[] rcon(int round) {
        return new int[]{RCON[round], 0x00, 0x00, 0x00};
    }

    // XOR function
    public static int[] xorWords(int[] word1, int[] word2) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = word1[i] ^ word2[i];
        }
        return result;
    }

    // Key Expansion function - Generates 15 round keys
    public static int[][] generateRoundKeys(int[] key) {
        int[][] expandedKeys = new int[60][4]; // 15 round keys (each 128 bits = 4 words)

        // First 8 words are copied directly from the original key
        for (int i = 0; i < 8; i++) {
            expandedKeys[i] = Arrays.copyOfRange(key, i * 4, (i + 1) * 4);
        }

        // Generate remaining words
        for (int i = 8; i < 60; i++) {
            int[] temp = expandedKeys[i - 1];

            if (i % 8 == 0) {
                // Apply RotWord, SubWord, and Rcon for every 8th word
                temp = xorWords(subWord(rotWord(temp)), rcon(i / 8));
            } else if (i % 8 == 4) {
                // Apply SubWord for every 4th word
                temp = subWord(temp);
            }

            // XOR with the word 8 positions before
            expandedKeys[i] = xorWords(expandedKeys[i - 8], temp);
        }

        return expandedKeys;
    }

    // Print the round keys (Formatted)
    public static void printRoundKeys(int[][] keys) {
        for (int i = 3; i < 15; i++) {
            System.out.printf("Round Key %d:\n", i);
            for (int j = 0; j < 4; j++) {
                System.out.printf("%02X %02X %02X %02X\n", keys[i * 4 + j][0], keys[i * 4 + j][1], keys[i * 4 + j][2], keys[i * 4 + j][3]);
            }
            System.out.println();
        }
    }
    // Helper method to create a matrix label for displaying a 4x4 matrix
    private Label createMatrixLabel(int[][] key) {
        StringBuilder matrixText = new StringBuilder("Key Matrix:\n");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrixText.append(String.format("%02x ", key[j][i])); // Convert to hex format
            }
            matrixText.append("\n");
        }

        Label matrixLabel = new Label(matrixText.toString());
        matrixLabel.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        matrixLabel.setPadding(new Insets(10));

        // Fade animation for each matrix
        FadeTransition fade = new FadeTransition(Duration.seconds(1), matrixLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.seconds(0.5));
        fade.play();

        return matrixLabel;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
    String password = "YamenSucksAtMath"; // Example password

    // Generate and split the key
    byte[] aesKey = get256BitKey(password);
    int[][][] keyMatrices = splitKeyIntoMatrices(aesKey);

    // Print the first two round keys (keyMatrices)
    System.out.println("Round Key 0:");
    printMatrix(keyMatrices[0]);
    System.out.println();
    System.out.println("Round Key 1:");
    printMatrix(keyMatrices[1]);
    System.out.println();

    // Expand the key
    int[] intKey = new int[aesKey.length];
    for (int i = 0; i < aesKey.length; i++) {
        intKey[i] = aesKey[i] & 0xFF;
    }
    int[][] roundKeys = generateRoundKeys(intKey);

    // Print the remaining round keys
    printRoundKeys(roundKeys);
}

// Helper method to print a 4x4 matrix
private static void printMatrix(int[][] matrix) {
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            System.out.printf("%02X ", matrix[i][j]);
        }
        System.out.println();
    }
}
}
