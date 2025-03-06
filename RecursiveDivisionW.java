import javafx.scene.canvas.GraphicsContext;

public class RecursiveDivisionW {
    public static void drawMaze(GraphicsContext graphicsContext, int x, int y, int size, int level) {
        if (level <= 0) {
            graphicsContext.fillRect(x, y, size, size);
            return; //skip rest of method
        }
        
    }
}
