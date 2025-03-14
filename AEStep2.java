import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

public class AEStep2 extends VBox {
    public AEStep2() {
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(50);

        Label title = new Label("This page is under construction...");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Back button to return to AESPasswordArray
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("nav-button"); // Apply CSS class
        backButton.setOnAction(e -> FXUtils.setSceneRoot(getScene(), new AESPasswordArray(AEStart.getPassword())));

        getChildren().addAll(title, backButton);
    }
}
