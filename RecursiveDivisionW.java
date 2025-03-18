import javafx.scene.canvas.Canvas;

public class RecursiveDivisionW {
    public static void drawMaze(Canvas canvas, int level) {
        if(level == 0){
            FXUtils.drawLine(canvas, 0, 0, 170, 0);
            FXUtils.drawLine(canvas, 0, 0, 0, 250);
            FXUtils.drawLine(canvas, 0, 250, 170, 250);
            FXUtils.drawLine(canvas, 170, 0, 170, 250);
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