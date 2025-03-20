public class RecursiveDivisionW {

    private static final Integer WIDTH = 500;
    private static final Integer HEIGHT = 800;

    public static String[][] drawGrid(String[][] array) {
        // draw a grid

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                array[i][j] = ".";
            }
        }
        System.out.println(array);
        return (array);
    }

    public static void drawWalls(String[][] array) {
        // draw the walls
        int randWidth = 0 + (int) (Math.random() * (500 - 0 + 1));
        int randHeight = 0 + (int) (Math.random() * (800 - 0 + 1));
        boolean full = false;
        do {
            for (int i = 0; i < randHeight; i++) {
                array[randWidth][i] = "|";
            }
            for (int i = 0; i < randWidth; i++) {
                array[i][randHeight] = "-";
            }
        } while (full == false);

    }

    public static void main(String[] args) {
        String[][] grid = new String[WIDTH][HEIGHT];
        RecursiveDivisionW.drawGrid(grid);
        RecursiveDivisionW.drawWalls(grid);
    }
}

// needs to make interior walls
// needs to make the two openings, one per wall
// need to go down into 1 of the 4 new quadrants
// need to repeat the process
// need to stop when when its like 3 or 4 levels in
// needs to go back then do same for any spaces in the original subchamber
// then do same for rest of the 4 original subchambers