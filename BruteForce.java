import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class BruteForce extends FlowPane{
    void BruteForceLayout() {
        setAlignment(Pos.CENTER);

        Text description = new Text("");

        Button backsBtn = new Button("Back");
        backsBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(description, backsBtn);
    }
}