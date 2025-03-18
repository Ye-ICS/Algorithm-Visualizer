import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MazeSortLayout extends VBox {
    MazeSortLayout() {
        WritableImage gridImage = new WritableImage(600, 600);

        ImageView gridView = new ImageView(gridImage);

        PixelWriter writer = gridImage.getPixelWriter();

        Text cbText = new Text("Display Progress: ");

        CheckBox cb = new CheckBox();
        cb.setIndeterminate(false);
        cb.setSelected(true);

        Text scaleText = new Text("Scale:");

        Spinner<Integer> scaleSpinner = new Spinner<Integer>(3, 200, 20, 1);
        scaleSpinner.setEditable(true);

        Button resetBtn = new Button("Run");
        resetBtn.setOnAction(event -> DeclanJones.run(cb.selectedProperty().get(), gridImage, writer,
                (int) gridImage.getHeight() / scaleSpinner.getValue()));

        HBox bottomPanel = new HBox(cbText, cb, scaleText, scaleSpinner, resetBtn);
        bottomPanel.setSpacing(10);
        bottomPanel.setPadding(new Insets(10, 10, 10, 10));

        getChildren().addAll(gridView, bottomPanel);
    }
}
