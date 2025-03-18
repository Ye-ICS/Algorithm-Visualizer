import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MazeSortLayout extends VBox {
    MazeSortLayout() {
        WritableImage gridImage = new WritableImage(600, 600);

        ImageView gridView = new ImageView(gridImage);

        PixelWriter writer = gridImage.getPixelWriter();

        Text speedText = new Text("Speed: ");

        Text scaleText = new Text("Scale:");

        Spinner<Integer> scaleSpinner = new Spinner<Integer>(4, 600, 20, 2);
        scaleSpinner.setEditable(true);

        Spinner<Integer> speedSpinner = new Spinner<Integer>(1, 1001, 10, 1);
        speedSpinner.setEditable(true);

        Button resetBtn = new Button("Run");
        resetBtn.setOnAction(event -> DeclanJones.run((int) gridImage.getHeight(), (int) gridImage.getWidth(),
                1000 / speedSpinner.getValue(),
                (int) gridImage.getHeight() / scaleSpinner.getValue()));

        HBox bottomPanel = new HBox(speedText, speedSpinner, scaleText, scaleSpinner, resetBtn);
        bottomPanel.setSpacing(10);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));

        getChildren().addAll(gridView, bottomPanel);

        DeclanJones.run((int) gridImage.getHeight(), (int) gridImage.getWidth(), 0,
                (int) gridImage.getHeight() / scaleSpinner.getValue());

        Thread thread = new Thread(() -> {
            while (true) {
                if (DeclanJones.pathFindable) {
                    DeclanJones.printGrid(DeclanJones.wallGrid, DeclanJones.pathGrid, writer);
                } else {
                    DeclanJones.printGrid(DeclanJones.wallGrid, DeclanJones.checked, writer);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
