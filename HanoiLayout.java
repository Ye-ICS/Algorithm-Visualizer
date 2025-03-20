import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HanoiLayout extends FlowPane {
    HanoiLayout() {
        setAlignment(Pos.CENTER);

        Text description = new Text("Tower of Hanoi");

        Canvas canvas = new Canvas(600, 200); 
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //base of tower things
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 150, 100, 10); 
        gc.fillRect(250, 150, 100, 10);
        gc.fillRect(450, 150, 100, 10);

        //middle sticks of tower things
        gc.setFill(Color.GRAY);
        gc.fillRect(95, 50, 10, 100);
        gc.fillRect(295, 50, 10, 100);
        gc.fillRect(495, 50, 10, 100);

        Spinner<Integer> levelSpinner = new Spinner<Integer>(0, 100000, 0, 1);
        levelSpinner.setEditable(true);


        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        Button solveBtn = new Button("solve");
        //solveBtn.setOnAction(event -> method i make in future :D);


        getChildren().addAll(description, canvas, backBtn, solveBtn, levelSpinner);
    }

}
