
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AStar extends BorderPane {
    AStar() {
        Text description = new Text("AStar");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        GridPane grid = new GridPane();

        setCenter(grid);
        setTop(description);
        setBottom(backBtn);
    }
}
