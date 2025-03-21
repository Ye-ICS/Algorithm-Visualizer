import java.util.Arrays;

public class RecursiveDivisionW {

    private static final Integer WIDTH = 10;
    private static final Integer HEIGHT = 10;

    public static String[][] drawGrid(String[][] array) {
        // draw a grid

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                array[i][j] = ".";
            }
        }
        return (array);
    }

    public static String[][] drawWalls(String[][] array, Integer horiz, Integer vert) {
        // draw the walls
        int randNum;
        boolean full = false;
        do {
            randNum = 1 + (int) (Math.random() * (10 - 2 + 1));
            for (int i = 0; i < WIDTH; i++) {
                array[vert][i] = "-";
            }
            randNum = 1 + (int) (Math.random() * (10 - 2 + 1));
            array[vert][randNum] = " ";

            for (int j = 0; j < HEIGHT; j++) {
                array[j][horiz] = "|";
            }
            randNum = 1 + (int) (Math.random() * (10 - 2 + 1));
            array[randNum][horiz] = " ";

            array[vert][horiz] = "+";
            full = true;
        } while (full == false);
        return (array);
    }

    public static String[][] drawMaze(String[][] array, Integer horiz, Integer vert) {
        // draw the maze
        int randWidth = 1 + (int) (Math.random() * (horiz - 2 + 1));
        int randHeight = 1 + (int) (Math.random() * (vert - 2 + 1));
        int randNum;
        boolean full = false;
        do {
            for (int i = 0; i < randWidth; i++) {
                array[randHeight][i] = "-";
            }
            randNum = 1 + (int) (Math.random() * (randWidth - 2 + 1));
            array[randHeight][randNum] = " ";

            for (int j = 0; j < randHeight; j++) {
                array[j][randWidth] = "|";
            }
            randNum = 1 + (int) (Math.random() * (randHeight - 2 + 1));
            array[randNum][randWidth] = " ";

            array[randHeight][randWidth] = "+";
            full = true;
        } while (full == false);
        return (array);
    }

    public static void main(String[] args) {
        String[][] grid = new String[WIDTH][HEIGHT];
        int randHoriz = 1 + (int) (Math.random() * (WIDTH - 2 + 1));
        int randVert = 1 + (int) (Math.random() * (HEIGHT - 2 + 1));

        RecursiveDivisionW.drawGrid(grid);
        System.out.println(Arrays.deepToString(grid));

        RecursiveDivisionW.drawWalls(grid, randHoriz, randVert);
        System.out.println(Arrays.deepToString(grid));

        RecursiveDivisionW.drawMaze(grid, randHoriz, randVert);
        System.out.println(Arrays.deepToString(grid));
    }
}

// need to go down into 1 of the 4 new quadrants
// need to repeat the process
// need to stop when when its like 3 or 4 levels in
// needs to go back then do same for any spaces in the original subchamber
// then do same for rest of the 4 original subchambers