import javafx.scene.canvas.GraphicsContext;

public class RecursiveDivisionW {
    public static void drawMaze(GraphicsContext graphicsContext, int x, int y, int size, int level) {
        if (level <= 0) {
            graphicsContext.fillRect(x, y, size, size);
            return; //skip rest of method
        }

    }
}



// needs to make interior walls
// needs to make the two openings, one per wall
// need to go down into 1 of the 4 new quadrants
// need to repeat the process
// need to stop when when its like 3 or 4 levels in
// needs to go back then do same for any spaces in the original subchamber
// then do same for rest of the 4 original subchambers