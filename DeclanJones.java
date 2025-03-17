import java.util.Arrays;
import java.util.Comparator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DeclanJones {
    static boolean pathFound;

    static boolean[][] wallGrid;

    static boolean[][] pathGrid;

    static boolean[][] checked;

    static int[][] distanceGrid;

    static int[][] distanceGridGoal;

    static int[][] coords;

    static int[][][] cameFrom;

    static int scale = 1;

    public static void run(WritableImage gridImage, PixelWriter writer, int newScale) {
        scale = newScale;

        wallGrid = new boolean[(int) gridImage.getWidth() / scale][(int) gridImage.getHeight() / scale];

        while (!pathFound) {
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
            populate();
            while (!pathFound && (coords.length >= 1)) {
                checkNeighbours();
            }
        }

        populate();

        final Timeline timeline = new Timeline();

        KeyFrame periodicEvent = new KeyFrame(Duration.millis(300), event -> {
            checkNeighbours();
            printGrid(checked, wallGrid, writer);
            if (pathFound) {
                pathGrid = findPathGrid();
                printGrid(wallGrid, pathGrid, writer);
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(periodicEvent);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void printGrid(boolean[][] wallGrid, boolean[][] pathGrid, PixelWriter writer) {
        if (pathGrid != null && wallGrid.length == pathGrid.length && wallGrid[0].length == pathGrid[0].length) {
            for (int i = 0; i < wallGrid.length; i++) {
                for (int j = 0; j < wallGrid[0].length; j++) {
                    if (i + j == 0 || i == wallGrid.length - 1 && j == wallGrid[0].length - 1) {
                        writer.setColor(i, j, Color.rgb(50, 50, 50));
                    } else if (wallGrid[i][j]) {
                        writer.setColor(i, j, Color.rgb(150, 150, 150));
                    } else if (pathGrid[i][j]) {
                        writer.setColor(i, j, Color.rgb(255, 255, 255));
                    } else {
                        writer.setColor(i, j, Color.rgb(0, 0, 0));
                    }
                }
            }
        }
    }

    public static void writerLarge(int x, int y, PixelWriter writer, Color color) {

        writer.setColor(x * scale, y * scale, color);
        writer.setColor(x * scale + 1, y * scale, color);
        writer.setColor(x * scale, y * scale + 1, color);
        writer.setColor(x * scale + 1, y * scale + 1, color);

    }

    public static void populate() {

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

        distanceGrid = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGrid.length; i++) {
            for (int j = 0; j < distanceGrid[0].length; j++) {
                distanceGrid[i][j] = 0;
            }
        }

        distanceGridGoal = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGridGoal.length; i++) {
            for (int j = 0; j < distanceGridGoal[0].length; j++) {
                distanceGridGoal[distanceGridGoal.length - 1 - i][distanceGridGoal[0].length - 1 - j] = 14
                        * Math.min(i, j) + 10 * (Math.max(i, j) - Math.min(i, j));
            }
        }

        coords = new int[][] { { 0, 0 } };

        pathFound = false;
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

    private static void checkNeighbours() {
        checked[coords[0][0]][coords[0][1]] = true;

        for (int i = 0; i < 9; i++) {
            int distance = distanceGrid[coords[0][0]][coords[0][1]] + 10 + 4 * ((i + 1) % 2);
            int checkX = coords[0][0] - 1 + (i % 3);
            int checkY = coords[0][1] - 1 + (i / 3);
            if (checkX >= 0 && checkY >= 0 && checkX < wallGrid.length && checkY < wallGrid[0].length
                    && !checked[checkX][checkY] && !wallGrid[checkX][checkY]
                    && (distanceGrid[checkX][checkY] > distance || distanceGrid[checkX][checkY] == 0)
                    && !Arrays.asList(coords).contains(new int[] { checkX, checkY })) {
                coords = Arrays.copyOf(coords, coords.length + 1);
                coords[coords.length - 1] = new int[] { checkX, checkY };
                distanceGrid[checkX][checkY] = distance;
                cameFrom[checkX][checkY] = new int[] { coords[0][0], coords[0][1] };
            }
        }

        int[][] tempCoords = Arrays.copyOf(coords, coords.length - 1);
        for (int i = 0; i < tempCoords.length; i++) {
            tempCoords[i] = Arrays.copyOf(coords[i + 1], 2);
        }

        coords = new int[tempCoords.length][2];
        for (int i = 0; i < tempCoords.length; i++) {
            coords[i] = Arrays.copyOf(tempCoords[i], 2);
        }

        Arrays.sort(coords, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return distanceGridGoal[a[0]][a[1]] - distanceGridGoal[b[0]][b[1]] + distanceGrid[a[0]][a[1]]
                        - distanceGrid[b[0]][b[1]];
            }
        });

        if (cameFrom[cameFrom.length - 1][cameFrom[0].length - 1][0] != 0) {
            pathFound = true;
        }
    }
}