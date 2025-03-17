import java.util.Arrays;
import java.util.Comparator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DeclanJones {
    static boolean pathFindable;

    static boolean[][] wallGrid;

    static boolean[][] pathGrid;

    static boolean[][] checked;

    static int[][] distanceGridStart;

    static int[][] distanceGridGoal;

    static int[][] coordsToCheck;

    static int[][][] cameFrom;

    static int scale;

    public static void run(boolean displayProgress, WritableImage gridImage, PixelWriter writer, int inScale) {
        scale = inScale;

        wallGrid = new boolean[(int) gridImage.getWidth() / scale][(int) gridImage.getHeight() / scale];

        populateGrids();

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
            while (!pathFindable && (coordsToCheck.length >= 1)) {
                checkCoords();
            }
        }

        if (displayProgress) {
            populateGrids();

            final Timeline timeline = new Timeline();

            KeyFrame periodicEvent = new KeyFrame(Duration.millis(150), event -> {
                checkCoords();
                printGrid(wallGrid, checked, writer);
                if (pathFindable) {
                    pathGrid = findPathGrid();
                    printGrid(wallGrid, pathGrid, writer);
                    timeline.stop();
                }
            });

            timeline.getKeyFrames().add(periodicEvent);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

        } else {

            pathGrid = findPathGrid();
            printGrid(wallGrid, pathGrid, writer);

        }
    }

    public static void printGrid(boolean[][] grid1, boolean[][] grid2, PixelWriter writer) {
        if (grid2 != null && grid1.length == grid2.length && grid1[0].length == grid2[0].length) {
            for (int i = 0; i < grid1.length; i++) {
                for (int j = 0; j < grid1[0].length; j++) {
                    if (i + j == 0 || i == grid1.length - 1 && j == grid1[0].length - 1) {
                        writerToScale(i, j, writer, Color.rgb(255, 0, 0));
                    } else if (grid1[i][j]) {
                        writerToScale(i, j, writer, Color.rgb(150, 150, 150));
                    } else if (grid2[i][j]) {
                        writerToScale(i, j, writer, Color.rgb(255, 255, 255));
                    } else {
                        writerToScale(i, j, writer, Color.rgb(0, 0, 0));
                    }
                }
            }
        }
    }

    public static void writerToScale(int x, int y, PixelWriter writer, Color color) {
        for (int i = 0; i < scale; i++) {
            for (int j = 0; j < scale; j++) {
                writer.setColor(x * scale + j, y * scale + i, color);
            }
        }
    }

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

        coordsToCheck = new int[][] { { 0, 0 } };

        pathFindable = false;
    }

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

    private static void checkCoords() {
        checked[coordsToCheck[0][0]][coordsToCheck[0][1]] = true;

        for (int i = 0; i < 9; i++) {

            int distance = distanceGridStart[coordsToCheck[0][0]][coordsToCheck[0][1]] + 10 + 4 * ((i + 1) % 2);
            int XToCheck = coordsToCheck[0][0] - 1 + (i % 3);
            int YToCheck = coordsToCheck[0][1] - 1 + (i / 3);

            if (XToCheck >= 0 && YToCheck >= 0 && XToCheck < wallGrid.length && YToCheck < wallGrid[0].length
                    && !checked[XToCheck][YToCheck] && !wallGrid[XToCheck][YToCheck]
                    && (distanceGridStart[XToCheck][YToCheck] > distance || distanceGridStart[XToCheck][YToCheck] == 0)
                    && !Arrays.asList(coordsToCheck).contains(new int[] { XToCheck, YToCheck })) {

                coordsToCheck = Arrays.copyOf(coordsToCheck, coordsToCheck.length + 1);
                coordsToCheck[coordsToCheck.length - 1] = new int[] { XToCheck, YToCheck };
                distanceGridStart[XToCheck][YToCheck] = distance;
                cameFrom[XToCheck][YToCheck] = new int[] { coordsToCheck[0][0], coordsToCheck[0][1] };
                
            }
        }

        int[][] tempCoords = Arrays.copyOf(coordsToCheck, coordsToCheck.length - 1);
        for (int i = 0; i < tempCoords.length; i++) {
            tempCoords[i] = Arrays.copyOf(coordsToCheck[i + 1], 2);
        }

        coordsToCheck = new int[tempCoords.length][2];
        for (int i = 0; i < tempCoords.length; i++) {
            coordsToCheck[i] = Arrays.copyOf(tempCoords[i], 2);
        }

        Arrays.sort(coordsToCheck, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return distanceGridGoal[a[0]][a[1]] - distanceGridGoal[b[0]][b[1]] + distanceGridStart[a[0]][a[1]]
                        - distanceGridStart[b[0]][b[1]];
            }
        });

        if (cameFrom[cameFrom.length - 1][cameFrom[0].length - 1][0] != 0) {
            pathFindable = true;
        }
    }
}