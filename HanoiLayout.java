import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

/**
 * Placeholder. May delete later.
 */
public class HanoiLayout extends FlowPane{
    HanoiLayout() {
        setAlignment(Pos.CENTER);

        Text description = new Text("HIIII!I!I!I!I!");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(description, backBtn);
    }
}
