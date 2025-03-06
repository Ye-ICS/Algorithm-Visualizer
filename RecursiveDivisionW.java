import javafx.scene.canvas.GraphicsContext;

public class RecursiveDivisionW {
    int level = 0;
    int x = 0;
    int y = 0;
    int size = 0;
    public void drawMaze(GraphicsContext graphicsContext) {
        
        if (level <= 0) {

            graphicsContext.fillRect(x, y, size, size);

            return; // return early (skip rest of this method)
        }
    }
}
