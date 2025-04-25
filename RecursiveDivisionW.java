import java.util.Arrays;

public class RecursiveDivisionW {

    private static final Integer WIDTH = 10;
    private static final Integer HEIGHT = 10;

    /**
     * creates the initial grid for the Maze to be drawn on
     * 
     * @param array The array containing the location of the grid elements
     * @return
     */

    public static String[][] drawGrid(String[][] array) {
        // draw a grid

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                array[i][j] = ".";
            }
        }
        return (array);
    }

    /**
     * Method to draw the initial walls of the maze
     * 
     * @param array The array containing the location of the grid elements
     * @param horiz The horizontal location of the wall
     * @param vert  The vertical location of the wall
     * @return
     */
    public static String[][] drawWalls(String[][] array, Integer horiz, Integer vert) {
        // draw the walls
        int randNum;
        do {
            randNum = 1 + (int) (Math.random() * (10 - 3 + 1));
            for (int i = 0; i < WIDTH; i++) {
                array[vert][i] = "-";
            }
            randNum = 1 + (int) (Math.random() * (10 - 3 + 1));
            array[vert][randNum] = " ";

            for (int j = 0; j < HEIGHT; j++) {
                array[j][horiz] = "|";
            }
            randNum = 1 + (int) (Math.random() * (10 - 3 + 1));
            array[randNum][horiz] = " ";

            array[vert][horiz] = "+";
            return (array);
        } while (true);
    }

    /**
     * the recursie method the draw the rest of the maze
     * 
     * @param array The array containing the location of the grid elements
     * @param bottom The horizontal location of the original wall
     * @param right  The vertical location of the original wall
     * @return
     */
    public static String[][] drawMaze(String[][] array, Integer bottom, Integer right, Integer top, Integer left) {
        // draw the maze

        // TODO: modify this statement
        int vertLine = 2 + (int) (Math.random() * (bottom - 3 + 1)); // vertical line
        int horizLine = 2 + (int) (Math.random() * (right - 3 + 1)); // horizontal line
        int randNum;
        int remainingWidth = bottom - vertLine;
        int remainingHeight = right - horizLine;

        for (int i = 0; i < vertLine; i++) {
            array[horizLine][i] = "-";
        }
        randNum = 1 + (int) (Math.random() * (vertLine - 3 + 1));
        array[horizLine][randNum] = " ";

        for (int j = 0; j < horizLine; j++) {
            array[j][vertLine] = "|";
        }
        randNum = 1 + (int) (Math.random() * (horizLine - 3 + 1));
        array[randNum][vertLine] = " ";
        array[horizLine][vertLine] = "+";

        // if (remainingHeight <= 2 || remainingWidth <= 2) {
        //     drawMaze(array, HEIGHT, WIDTH);
        // } else {
        //     drawMaze(array, vertLine, horizLine);
        // } 
         // NEED to call the method recursively 4 times, one 4 each quad 
        return (array);
    }

    /**
     * Main method to run the program in the terminal
     */
    public void main(String[] args) {
        String[][] grid = new String[WIDTH][HEIGHT];
        int randHoriz = 2 + (int) (Math.random() * ((WIDTH - 2) - 2 + 1));
        int randVert = 2 + (int) (Math.random() * ((HEIGHT - 2) - 2 + 1));

        RecursiveDivisionW.drawGrid(grid); // method to draw the grid

        RecursiveDivisionW.drawWalls(grid, randHoriz, randVert); // method to draw the intial walls

        RecursiveDivisionW.drawMaze(grid, randHoriz, randVert, 0, 2);
        System.out.println(Arrays.deepToString(grid));
    }
}