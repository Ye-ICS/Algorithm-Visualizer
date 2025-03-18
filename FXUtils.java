import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Utility methods for JavaFX things.
 */
public class FXUtils {
    /**
     * Sets new root node for scene, auto-resizing to fit
     * @param scene Scene to set root for
     * @param rootNode New root node
     */
    static void setSceneRoot(Scene scene, Parent rootNode) {
        scene.setRoot(rootNode);
        scene.getWindow().sizeToScene();    // Auto-resize
    }
    
    // TODO: Make a method for creating layout switching buttons

    static void drawLine(Canvas canvas, int x1, int y1, int x2, int y2) {
        // draw a line
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }
}
