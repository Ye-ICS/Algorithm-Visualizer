import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;


public class Maze extends FlowPane{
    Maze() {
        setAlignment(Pos.CENTER);

        Text description = new Text("This page is under construction...");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        getChildren().addAll(description, backBtn);
    }
}
