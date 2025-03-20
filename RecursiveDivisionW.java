public class RecursiveDivisionW {

    private static final Integer WIDTH = 500;
    private static final Integer HEIGHT = 800;

    public static void drawGrid() {
        String[][] grid = new String[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                grid[i][j] = ".";
            }
        }
        System.out.println(grid);
    }

    public static void main(String[] args) {
        RecursiveDivisionW.drawGrid();
    }
}

// needs to make interior walls
// needs to make the two openings, one per wall
// need to go down into 1 of the 4 new quadrants
// need to repeat the process
// need to stop when when its like 3 or 4 levels in
// needs to go back then do same for any spaces in the original subchamber
// then do same for rest of the 4 original subchambers