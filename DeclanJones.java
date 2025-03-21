import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DeclanJones {

    // The grid of walls
    static boolean[][] wallGrid;

    // The path grid to be found later
    static boolean[][] pathGrid;

    // Whether a spot has been checked for its neighbours yet
    static boolean[][] neighboursChecked;

    // Grids containing the distance from the start and goal, to be populated later
    static int[][] startDistance;
    static int[][] goalDistance;

    // The array of coordinates to check
    static ArrayList<int[]> nodesToCheck;

    // Coordinates of the square that has the shortest path to the start
    static int[][][] previousNode;

    // Scaling factor, assigned when run
    static int renderScale;

    static Thread pathfindingThread;

    static Timeline renderTimeline;

    public static void run(int height, int width, int speed, int inputScale) {

        stopThreads();

        renderScale = inputScale;

        pathGrid = null;

        wallGrid = new boolean[height / renderScale][width / renderScale];

        resetPathFinding();

        // Create a grid and make sure it is solveable
        while (!checkCoords()) {
            for (int i = 0; i < wallGrid.length; i++) {
                for (int j = 0; j < wallGrid[0].length; j++) {
                    if ((int) (Math.random() * 2) != 0 && (i != 0 || j != 0)
                            && (i != wallGrid.length - 1 || j != wallGrid[0].length - 1)) {
                        wallGrid[i][j] = true;
                    } else {
                        wallGrid[i][j] = false;
                    }
                }
            }
            resetPathFinding();
            while (!checkCoords() && (nodesToCheck.size() >= 1)){

            }
        }

        resetPathFinding();

        KeyFrame drawEvent = new KeyFrame(Duration.millis(150), event -> {
            printGrid(wallGrid, neighboursChecked, DeclanJonesLayout.writer);
            if (pathGrid != null) {
                synchronized (pathGrid) {
                    printGrid(wallGrid, pathGrid, DeclanJonesLayout.writer);
                    pathGrid.notifyAll();
                }
            }
        });

        renderTimeline = new Timeline(drawEvent);
        renderTimeline.setCycleCount(Timeline.INDEFINITE);
        renderTimeline.play();

        pathfindingThread = new Thread(() -> {
            while (!checkCoords()) {
                if (speed > 0) {
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                    }
                }
            }
            pathGrid = findPathGrid();
            synchronized (pathGrid) {
                try {
                    pathGrid.wait();
                } catch (InterruptedException e) {
                }
            }
            renderTimeline.stop();
            return;
        });

        pathfindingThread.start();
    }

    public static void stopThreads() {
        try {
            pathfindingThread.interrupt();
        } catch (Exception e) {
        }

        try {
            renderTimeline.stop();
        } catch (Exception e) {
        }
    }

    /**
     * Prints two boolean grids onto a PixelWriter, coloring pixels based on their
     * values in the grids.
     * The method iterates through each cell of the grids, applying different colors
     * based on the following conditions:
     * - If the cell is at the top-left (0, 0) or bottom-right corner (grid1.length
     * - 1, grid1[0].length - 1), it's colored red.
     * - If the cell is true in grid1, it's colored gray.
     * - If the cell is true in grid2, it's colored white.
     * - Otherwise, the cell is colored black.
     * The method only executes if grid2 is not null and both grids have the same
     * dimensions.
     *
     * @param wallGrid    The grid of walls to display.
     * @param displayGrid The second grid to display. Can be null.
     * @param writer      The PixelWriter to draw the grids onto.
     */
    public static void printGrid(boolean[][] wallGrid, boolean[][] displayGrid, PixelWriter writer) {
        if (displayGrid == null || wallGrid.length != displayGrid.length
                || wallGrid[0].length != displayGrid[0].length) {
            return;
        }

        // synchronized (displayGrid) {
        for (int i = 0; i < wallGrid.length; i++) {
            for (int j = 0; j < wallGrid[0].length; j++) {
                if (i + j == 0 || i == wallGrid.length - 1 && j == wallGrid[0].length - 1) {
                    writerToScale(i, j, writer, Color.rgb(0, 255, 0));
                } else if (wallGrid[i][j]) {
                    writerToScale(i, j, writer, Color.rgb(124, 0, 0));
                } else if (displayGrid[i][j]) {
                    writerToScale(i, j, writer, Color.rgb(255, 255, 255));
                } else {
                    writerToScale(i, j, writer, Color.rgb(0, 0, 0));
                }
            }
        }
        // }

    }

    /**
     * Writes a scaled pixel to the given PixelWriter with the specified color.
     * The method iterates through a square region defined by the 'scale' factor,
     * setting each pixel within that region to the provided color. This effectively
     * creates a larger, scaled pixel at the given (x, y) coordinates.
     *
     * @param x      The x-coordinate of the pixel to scale.
     * @param y      The y-coordinate of the pixel to scale.
     * @param writer The PixelWriter object used to write the color to the image.
     * @param color  The color to set for the scaled pixel.
     */
    public static void writerToScale(int x, int y, PixelWriter writer, Color color) {
        for (int i = 0; i < renderScale; i++) {
            for (int j = 0; j < renderScale; j++) {
                writer.setColor(x * renderScale + j, y * renderScale + i, color);
            }
        }
    }

    /**
     * populates the grids required for pathfinding.
     * This method resets the 'checked' boolean grid, the 'cameFrom' grid,
     * the 'distanceGridStart', and the 'distanceGridGoal' grids.
     * The 'checked' grid is initialized to all false values, indicating that no
     * cells have been visited.
     * The 'cameFrom' grid is initialized to all zero values, representing that no
     * path has been established.
     * The 'distanceGridStart' is initialized to all zero values.
     * The 'distanceGridGoal' is populated with heuristic distance values,
     * calculated based on the Manhattan distance from the goal.
     * Finally, 'coordsToCheck' is initialized with the starting coordinate {0, 0},
     * and 'pathFindable' is set to false.
     */
    public static void resetPathFinding() {

        neighboursChecked = new boolean[wallGrid.length][wallGrid[0].length];

        previousNode = new int[wallGrid.length][wallGrid[0].length][2];

        for (int i = 0; i < neighboursChecked.length; i++) {
            for (int j = 0; j < neighboursChecked[i].length; j++) {
                neighboursChecked[i][j] = false;
                for (int k = 0; k < 2; k++) {
                    previousNode[i][j][k] = 0;
                }
            }
        }

        startDistance = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < startDistance.length; i++) {
            for (int j = 0; j < startDistance[0].length; j++) {
                startDistance[i][j] = 0;
            }
        }

        goalDistance = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < goalDistance.length; i++) {
            for (int j = 0; j < goalDistance[0].length; j++) {
                goalDistance[goalDistance.length - 1 - i][goalDistance[0].length - 1 - j] = 14
                        * Math.min(i, j) + 10 * (Math.max(i, j) - Math.min(i, j));
            }
        }

        nodesToCheck = new ArrayList<int[]>();
        nodesToCheck.add(new int[] { 0, 0 });
    }

    /**
     * Reconstructs the path found by the A* search algorithm, using the 'cameFrom'
     * array to trace back from the end node to the start node.
     *
     * The method iterates backwards from the end node, marking each cell in the
     * 'foundPathGrid' as part of the path.
     * It stops when it reaches the start node (0, 0).
     *
     * @return A 2D boolean array representing the grid, where 'true' indicates a
     *         cell is part of the path, and 'false' indicates it is not.
     *         Returns 'null' if no path was found (i.e., 'cameFrom' is null or the
     *         end node in 'cameFrom' points to null).
     */
    private static boolean[][] findPathGrid() {
        if (previousNode == null || previousNode[previousNode.length - 1][previousNode[0].length - 1][0] == 0) {
            return null;
        }

        boolean[][] foundPathGrid = new boolean[previousNode.length][previousNode[0].length];
        for (int i = 0; i < foundPathGrid.length; i++) {
            for (int j = 0; j < foundPathGrid[0].length; j++) {
                foundPathGrid[i][j] = false;
            }
        }

        int[] find = new int[] { foundPathGrid.length - 1, foundPathGrid[0].length - 1 };

        while (!foundPathGrid[0][0]) {
            foundPathGrid[find[0]][find[1]] = true;
            find = previousNode[find[0]][find[1]];
        }

        return foundPathGrid;
    }

    /**
     * Explores the grid around a given coordinate to find the shortest path from a
     * start point to the end.
     * It checks adjacent cells, updates their distances from the start point, and
     * records the path in previousNode.
     * The method uses a heuristic approach, prioritizing cells closer to the goal.
     * It also maintains a list of coordinates to check, sorting them based on their
     * estimated distance to the goal, and distance from the start.
     *
     * @return True if the end has been reached, false otherwise.
     */
    private static boolean checkCoords() {
        if(nodesToCheck.size() == 0){
            return false;
        }
        neighboursChecked[nodesToCheck.get(0)[0]][nodesToCheck.get(0)[1]] = true;

        for (int i = 0; i < 9; i++) {

            int distance = startDistance[nodesToCheck.get(0)[0]][nodesToCheck.get(0)[1]] + 10 + 4 * ((i + 1) % 2);
            int XToCheck = nodesToCheck.get(0)[0] - 1 + (i % 3);
            int YToCheck = nodesToCheck.get(0)[1] - 1 + (i / 3);

            if (XToCheck >= 0 && YToCheck >= 0 && XToCheck < wallGrid.length && YToCheck < wallGrid[0].length
                    && !neighboursChecked[XToCheck][YToCheck] && !wallGrid[XToCheck][YToCheck]
                    && (startDistance[XToCheck][YToCheck] > distance || startDistance[XToCheck][YToCheck] == 0)
                    && !nodesToCheck.contains(new int[] { XToCheck, YToCheck })) {

                nodesToCheck.add(new int[] { XToCheck, YToCheck });
                startDistance[XToCheck][YToCheck] = distance;
                previousNode[XToCheck][YToCheck] = new int[] { nodesToCheck.get(0)[0], nodesToCheck.get(0)[1] };

            }
        }

        nodesToCheck.remove(0);

        nodesToCheck.sort((a, b) -> goalDistance[a[0]][a[1]] - goalDistance[b[0]][b[1]]
                + startDistance[a[0]][a[1]] - startDistance[b[0]][b[1]]);

        if (previousNode[previousNode.length - 1][previousNode[0].length - 1][0] != 0) {
            return true;
        }

        return false;
    }
}