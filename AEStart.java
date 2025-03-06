import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

/**
 * Placeholder. May delete later.
 */
public class AEStart extends VBox {
    AEStart() {
        setAlignment(Pos.CENTER);
        // javafx.stage.Stage stage = (javafx.stage.Stage) getScene().getWindow();
        // stage.setFullScreen(true);
        // stage.setMaximized(true);
        setPrefSize(400, 600);
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setSpacing(150); // Set vertical spacing between elements

        Text description = new Text("Enter your password here:");
        description.setStyle("-fx-padding: 10px;");

        TextField passwordBox = new TextField();
        passwordBox.setMaxWidth(200);
        passwordBox.setStyle("-fx-padding: 10px;");

        Button startBtn = new Button("Start Animation !");
        startBtn.backgroundProperty().set(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        startBtn.setStyle("-fx-padding: 10px;");

        startBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(description, passwordBox, startBtn);
    }
}