import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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
    @SuppressWarnings("unused")
    public AESPasswordArray(String password) {
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        List<String> hexBytes = convertToHexBytes(password); // Convert password to hex byte array
        String[][] grid = fillGridColumnWise(hexBytes); // Fill the 4x4 grid column-wise
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setPrefSize(600, 200);
        this.setPrefSize(400, 300); // Adjust the width and height as needed
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30); // Set vertical spacing between elements
        
        Text descriptionText = new Text(
                "Now that you have entered your password, AES-256 will convert the plaintext password into hexadecimal before creating a 4x4 column-major order array composed of 16 bytes. Each byte is represented by two hexadecimal characters. The array is then used (after many transformations) to generate the encryption key.");
        descriptionText.setStyle("-fx-padding: 10px; -fx-font-size: 19px;");
        this.getChildren().add(descriptionText);
        javafx.scene.text.TextFlow description = new javafx.scene.text.TextFlow(descriptionText);
        description.setTextAlignment(TextAlignment.CENTER);
        description.prefWidthProperty().bind(descriptionBox.widthProperty());
        descriptionBox.getChildren().add(description);
        this.getChildren().add(descriptionBox);

        // Display initial hex sequence as a single line
        Label hexLine = new Label(String.join(" ", hexBytes));
        hexLine.setStyle("-fx-font-size: 17px; -fx-padding: 10px; -fx-border-color: black; -fx-background-color: white; -fx-border-width: 1px;");
        this.getChildren().add(hexLine);

        // Pause before moving to the grid
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        // Create the grid layout
        HBox gridContainer = new HBox();
        gridContainer.setStyle("-fx-border-color:rgb(30, 70, 170); -fx-border-width: 2px;");
        gridContainer.setMinSize(190, 190);
        gridContainer.setPrefSize(190, 190);
        gridContainer.setMaxSize(190, 190);
        VBox gridWrapper = new VBox();
        gridWrapper.setMinSize(196, 196);
        gridWrapper.setPrefSize(196, 196);
        gridWrapper.setMaxSize(196, 196);
        gridWrapper.setAlignment(Pos.CENTER);
        gridContainer.setAlignment(Pos.CENTER);
        List<TranslateTransition> animations = new ArrayList<>();

        for (int col = 0; col < GRID_SIZE; col++) {
            VBox colBox = new VBox();
            colBox.setSpacing(5);
            colBox.setPadding(new Insets(5, 5, 5, 5));

            for (int row = 0; row < GRID_SIZE; row++) {
                Label cell = new Label(grid[row][col]);
                cell.setStyle("-fx-border-color: black; -fx-padding: 5px; -fx-background-color: white; -fx-font-size : 17px");
                cell.setOpacity(0);
                colBox.getChildren().add(cell);

                TranslateTransition anim = new TranslateTransition(Duration.seconds(1), cell);
                anim.setFromY(-20);
                anim.setToY(0);
                anim.setOnFinished(e -> cell.setOpacity(1));
                animations.add(anim);
            }
            gridContainer.getChildren().add(colBox);
        }
        gridWrapper.getChildren().add(gridContainer);
        this.getChildren().addAll(gridWrapper);

        SequentialTransition sequence = new SequentialTransition(pause);
        sequence.getChildren().addAll(animations);
        sequence.play();

    // Next Button to go to AEStep2
    Button nextButton = new Button("Next");
    nextButton.getStyleClass().add("nav-button"); // Apply CSS class
    nextButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AEStep2()));

    Button backButton = new Button("Back");
    backButton.getStyleClass().add("nav-button");
    backButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AEStart()));

    HBox buttonBox = new HBox(20, backButton, nextButton);
    buttonBox.setAlignment(Pos.CENTER);
    this.getChildren().add(buttonBox);
    getStylesheets().add(getClass().getResource("css/NavButtons.css").toExternalForm());

    }

    private List<String> convertToHexBytes(String text) {
        List<String> hexBytes = new ArrayList<>();
        for (char c : text.toCharArray()) {
            hexBytes.add(String.format("%02x", (int) c));
        }
        while (hexBytes.size() < BLOCK_SIZE) {
            hexBytes.add("00");
        }
        System.out.println(hexBytes);
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