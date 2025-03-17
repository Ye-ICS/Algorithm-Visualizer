import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MazeSortLayout extends StackPane{
    MazeSortLayout() {
        setAlignment(Pos.CENTER);

        WritableImage gridImage = new WritableImage(40, 40);

        ImageView gridView = new ImageView(gridImage);

        PixelWriter writer = gridImage.getPixelWriter();

        getChildren().add(gridView);

        DeclanJones.run(gridImage, writer, 2);
    }
}
