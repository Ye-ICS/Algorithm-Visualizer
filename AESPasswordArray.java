import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class AESPasswordArray extends VBox {
    private static final int BLOCK_SIZE = 16; // AES block size (16 bytes)
    private static final int GRID_SIZE = 4; // 4x4 grid

    /**
     * Constructor: Accepts a password and plaintext, converts both into 4x4 hex grids
     * (column-wise), and displays them side by side.
     *
     * @param password  The input password to be converted into a hex grid.
     * @param plaintext The input plaintext to be converted into a hex grid.
     */
    public AESPasswordArray(String password, String plaintext) {
        setBackground(new Background(new BackgroundFill(Color.web("#34495e"), CornerRadii.EMPTY, Insets.EMPTY))); // Modern dark color

        List<String> passwordHex = convertToHexBytes(password);
        List<String> plaintextHex = convertToHexBytes(plaintext);

        String[][] passwordGrid = fillGridColumnWise(passwordHex);
        String[][] plaintextGrid = fillGridColumnWise(plaintextHex);

        setPrefSize(600, 600);
        setAlignment(Pos.CENTER);
        setSpacing(30);

        // Description
        Text descriptionText = new Text(
                "The password and plaintext have been converted to hexadecimal and organized in 4x4 matrices.");
        descriptionText.setStyle("-fx-padding: 10px; -fx-font-size: 18px; -fx-font-family: 'Arial'; -fx-fill: white;");
        this.getChildren().add(descriptionText);

        // Display both hex sequences
        Label passwordHexLine = new Label("Password Hex: " + String.join(" ", passwordHex));
        Label plaintextHexLine = new Label("Plaintext Hex: " + String.join(" ", plaintextHex));

        passwordHexLine.setStyle("-fx-font-size: 17px; -fx-padding: 10px; -fx-border-color: black; -fx-background-color: white; -fx-border-width: 1px; -fx-text-fill: #2c3e50;");
        plaintextHexLine.setStyle("-fx-font-size: 17px; -fx-padding: 10px; -fx-border-color: black; -fx-background-color: white; -fx-border-width: 1px; -fx-text-fill: #2c3e50;");

        this.getChildren().addAll(passwordHexLine, plaintextHexLine);

        // Pause before grid animation
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Display both grids side by side
        HBox gridsContainer = new HBox(50);
        gridsContainer.setAlignment(Pos.CENTER);

        VBox passwordGridContainer = createGridContainer(passwordGrid, "Password Matrix");
        VBox plaintextGridContainer = createGridContainer(plaintextGrid, "Plaintext Matrix");

        gridsContainer.getChildren().addAll(passwordGridContainer, plaintextGridContainer);
        this.getChildren().add(gridsContainer);

        pause.play();

        // Navigation Buttons
        Button nextButton = new Button("Next");
        nextButton.getStyleClass().add("nav-button");
        nextButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AESKeyExpansion(password, plaintext)));

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("nav-button");
        backButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AEStart()));

        HBox buttonBox = new HBox(20, backButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);
        this.getChildren().add(buttonBox);

        getStylesheets().add(getClass().getResource("css/NavButtons.css").toExternalForm());
    }

    private VBox createGridContainer(String[][] grid, String labelText) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        container.getChildren().add(label);

        HBox gridContainer = new HBox();
        gridContainer.setStyle("-fx-border-color: rgb(30, 70, 170); -fx-border-width: 2px;");
        gridContainer.setMinSize(190, 190);
        gridContainer.setPrefSize(190, 190);
        gridContainer.setAlignment(Pos.CENTER);

        for (int col = 0; col < GRID_SIZE; col++) {
            VBox colBox = new VBox();
            colBox.setSpacing(5);
            for (int row = 0; row < GRID_SIZE; row++) {
                Label cell = new Label(grid[row][col]);
                cell.setStyle("-fx-border-color: black; -fx-padding: 5px; -fx-background-color: white; -fx-font-size: 17px; -fx-text-fill: #2c3e50;");
                colBox.getChildren().add(cell);

                // Add a translate animation to each grid element
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), cell);
                transition.setFromY(50);
                transition.setToY(0);
                transition.setDelay(Duration.seconds(0.2 * (row + col))); // Stagger the animation
                transition.play();
            }
            gridContainer.getChildren().add(colBox);
        }

        container.getChildren().add(gridContainer);
        return container;
    }

    private List<String> convertToHexBytes(String text) {
        List<String> hexBytes = new ArrayList<>();
        for (char c : text.toCharArray()) {
            hexBytes.add(String.format("%02x", (int) c));
        }
        while (hexBytes.size() < BLOCK_SIZE) {
            hexBytes.add("00"); // Padding with 0x00 if needed
        }
        return hexBytes;
    }

    private String[][] fillGridColumnWise(List<String> hexBytes) {
        String[][] grid = new String[GRID_SIZE][GRID_SIZE];
        int index = 0;
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                grid[row][col] = hexBytes.get(index++);
            }
        }
        return grid;
    }
}
