import javafx.scene.Parent;
import javafx.scene.Scene;

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
}
