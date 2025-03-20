import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DeclanJones {
    // Keep track of whether there is a possible path
    static boolean pathFindable;

    // The grid of walls
    static boolean[][] wallGrid;

    // The path grid to be found later
    static boolean[][] pathGrid;

    // Whether a spot has been checked yet
    static boolean[][] checked;

    // Grids containing the distance from the start and goal, to be populated later
    static int[][] distanceGridStart;
    static int[][] distanceGridGoal;

    // The array of coordinates to check
    static ArrayList<int[]> coordsToCheck;

    // Coordinates of the square that has the shortest path to the start
    static int[][][] cameFrom;

    // Scaling factor, assigned when run
    static int scale;

    static Thread pathfindingThread;

    static Timeline timeline;

    public static void run(int height, int width, int speed, int inScale) {

        try {
            pathfindingThread.interrupt();
        } catch (Exception e) {
        }

        try {
            timeline.stop();
        } catch (Exception e) {
        }

        scale = inScale;

        pathGrid = null;

        wallGrid = new boolean[height / scale][width / scale];

        populateGrids();

        // Create a grid and make sure it is solveable
        while (!pathFindable) {
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
            populateGrids();
            while (!pathFindable && (coordsToCheck.size() >= 1)) {
                checkCoords();
            }
        }

        populateGrids();

        KeyFrame drawEvent = new KeyFrame(Duration.millis(150), event -> {
            printGrid(wallGrid, checked, DeclanJonesLayout.writer);
            if (pathGrid != null) {
                synchronized (pathGrid) {
                    printGrid(wallGrid, pathGrid, DeclanJonesLayout.writer);
                    pathGrid.notifyAll();
                }
            }
        });

        timeline = new Timeline(drawEvent);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        pathfindingThread = new Thread(() -> {
            while (!pathFindable) {
                checkCoords();
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
            timeline.stop();
            return;
        });

        pathfindingThread.start();
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
        for (int i = 0; i < scale; i++) {
            for (int j = 0; j < scale; j++) {
                writer.setColor(x * scale + j, y * scale + i, color);
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
    public static void populateGrids() {

        checked = new boolean[wallGrid.length][wallGrid[0].length];

        cameFrom = new int[wallGrid.length][wallGrid[0].length][2];

        for (int i = 0; i < checked.length; i++) {
            for (int j = 0; j < checked[i].length; j++) {
                checked[i][j] = false;
                for (int k = 0; k < 2; k++) {
                    cameFrom[i][j][k] = 0;
                }
            }
        }

        distanceGridStart = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGridStart.length; i++) {
            for (int j = 0; j < distanceGridStart[0].length; j++) {
                distanceGridStart[i][j] = 0;
            }
        }

        distanceGridGoal = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGridGoal.length; i++) {
            for (int j = 0; j < distanceGridGoal[0].length; j++) {
                distanceGridGoal[distanceGridGoal.length - 1 - i][distanceGridGoal[0].length - 1 - j] = 14
                        * Math.min(i, j) + 10 * (Math.max(i, j) - Math.min(i, j));
            }
        }

        coordsToCheck = new ArrayList<int[]>();
        coordsToCheck.add(new int[] { 0, 0 });

        pathFindable = false;
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
        if (cameFrom == null || cameFrom[cameFrom.length - 1][cameFrom[0].length - 1][0] == 0) {
            return null;
        }

        boolean[][] foundPathGrid = new boolean[cameFrom.length][cameFrom[0].length];
        for (int i = 0; i < foundPathGrid.length; i++) {
            for (int j = 0; j < foundPathGrid[0].length; j++) {
                foundPathGrid[i][j] = false;
            }
        }

        int[] find = new int[] { foundPathGrid.length - 1, foundPathGrid[0].length - 1 };

        while (!foundPathGrid[0][0]) {
            foundPathGrid[find[0]][find[1]] = true;
            find = cameFrom[find[0]][find[1]];
        }

        return foundPathGrid;
    }

    /**
     * Explores the grid around a given coordinate to find the shortest path from a
     * start point.
     * It checks adjacent cells, updates their distances from the start point, and
     * records the path.
     * The method uses a heuristic approach, prioritizing cells closer to the goal.
     * It also maintains a list of coordinates to check, sorting them based on their
     * estimated distance to the goal.
     */
    private static void checkCoords() {
        checked[coordsToCheck.get(0)[0]][coordsToCheck.get(0)[1]] = true;

        for (int i = 0; i < 9; i++) {

            int distance = distanceGridStart[coordsToCheck.get(0)[0]][coordsToCheck.get(0)[1]] + 10 + 4 * ((i + 1) % 2);
            int XToCheck = coordsToCheck.get(0)[0] - 1 + (i % 3);
            int YToCheck = coordsToCheck.get(0)[1] - 1 + (i / 3);

            if (XToCheck >= 0 && YToCheck >= 0 && XToCheck < wallGrid.length && YToCheck < wallGrid[0].length
                    && !checked[XToCheck][YToCheck] && !wallGrid[XToCheck][YToCheck]
                    && (distanceGridStart[XToCheck][YToCheck] > distance || distanceGridStart[XToCheck][YToCheck] == 0)
                    && !coordsToCheck.contains(new int[] { XToCheck, YToCheck })) {

                coordsToCheck.add(new int[] { XToCheck, YToCheck });
                distanceGridStart[XToCheck][YToCheck] = distance;
                cameFrom[XToCheck][YToCheck] = new int[] { coordsToCheck.get(0)[0], coordsToCheck.get(0)[1] };

            }
        }

        coordsToCheck.remove(0);

        coordsToCheck.sort((a, b) -> distanceGridGoal[a[0]][a[1]] - distanceGridGoal[b[0]][b[1]]
                + distanceGridStart[a[0]][a[1]] - distanceGridStart[b[0]][b[1]]);

        if (cameFrom[cameFrom.length - 1][cameFrom[0].length - 1][0] != 0) {
            pathFindable = true;
        }
    }
}