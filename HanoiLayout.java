import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HanoiLayout extends FlowPane {
    private Canvas canvas;
    private GraphicsContext gc;

    HanoiLayout() {
        setAlignment(Pos.CENTER);

        Text description = new Text("Tower of Hanoi");

        canvas = new Canvas(600, 200);
        gc = canvas.getGraphicsContext2D();

        // Base of tower thingy
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 150, 100, 10); 
        gc.fillRect(250, 150, 100, 10);
        gc.fillRect(450, 150, 100, 10);

        // Middle of tower thingy
        gc.setFill(Color.GRAY);
        gc.fillRect(95, 50, 10, 100);
        gc.fillRect(295, 50, 10, 100);
        gc.fillRect(495, 50, 10, 100);

        Spinner<Integer> levelSpinner = new Spinner<>(1, 8, 3, 1);
        levelSpinner.setEditable(true);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        Button solveBtn = new Button("Solve");
        solveBtn.setOnAction(event -> {
            gc.clearRect(0, 0, 600, 200);
            int numDisks = levelSpinner.getValue();
            drawDisks(numDisks);
        });

        getChildren().addAll(description, canvas, backBtn, solveBtn, levelSpinner);
    }

    private void drawDisks(int numDisks) {
        gc.clearRect(0, 0, 600, 200);
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 150, 100, 10); 
        gc.fillRect(250, 150, 100, 10);
        gc.fillRect(450, 150, 100, 10);

        gc.fillRect(95, 50, 10, 100);
        gc.fillRect(295, 50, 10, 100);
        gc.fillRect(495, 50, 10, 100);

        Color[] diskColors = new Color[] { //COLOURS!?!?!?!??!
            Color.PURPLE, Color.BLUE, Color.CYAN, Color.GREEN, Color.LIGHTGREEN, 
            Color.YELLOW, Color.ORANGE, Color.RED
        };

        double baseYPosition = 150;
        double diskHeight = 10;

        for (int i = 0; i < numDisks; i++) {
            double diskWidth = 60 + (20 * (numDisks - i - 1));
            double xPosition = 100 - diskWidth/2;
            double yPosition = baseYPosition - (i * diskHeight) - diskHeight;

            gc.setFill(diskColors[i % diskColors.length]);

            gc.fillRect(xPosition, yPosition, diskWidth, diskHeight);
        }
    }
}
