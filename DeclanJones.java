import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class DeclanJones {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("What height would you like?");
        int width = Integer.parseInt(in.nextLine());
        System.out.println("What width would you like?");
        int height = Integer.parseInt(in.nextLine());
        System.out.println("What difficulty would you like?");
        double difficulty = Double.parseDouble(in.nextLine());
        in.close();

        boolean[][] wallGrid = new boolean[width][height];

        while (true) {

            boolean[][] pathGrid = null;

            int attempts = 0;
            while (pathGrid == null) {
                for (int i = 0; i < wallGrid.length; i++) {
                    for (int j = 0; j < wallGrid[0].length; j++) {
                        if ((int) (Math.random() * difficulty) != 0 && (i != 0 || j != 0)
                                && (i != wallGrid.length - 1 || j != wallGrid[0].length - 1)) {
                            wallGrid[i][j] = true;
                        } else {
                            wallGrid[i][j] = false;
                        }
                    }
                }
                pathGrid = pathFind(wallGrid, false, 0);
                attempts++;
            }

            printGrid(wallGrid, wallGrid);
            pathFind(wallGrid, true, attempts);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            printGrid(wallGrid, pathGrid);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void printGrid(boolean[][] wallGrid, boolean[][] pathGrid) {
        if (pathGrid != null && wallGrid.length == pathGrid.length && wallGrid[0].length == pathGrid[0].length) {
            String toPrint = "";
            for (int i = 0; i < wallGrid.length; i++) {
                for (int j = 0; j < wallGrid[0].length; j++) {
                    if (i + j == 0 || i == wallGrid.length - 1 && j == wallGrid[0].length - 1) {
                        toPrint += "▓▓";
                    } else if (wallGrid[i][j]) {
                        toPrint += "▒▒";
                    } else if (pathGrid[i][j]) {
                        toPrint += "██";
                    } else {
                        toPrint += "  ";
                    }
                }
                toPrint += "\n";
            }
            System.out.print("\033c");
            System.out.flush();
            System.out.print(toPrint);
        }
    }

    public static boolean[][] pathFind(boolean[][] wallGrid, boolean debug, int attempts) {

        boolean[][] checked = new boolean[wallGrid.length][wallGrid[0].length];
        int[][][] cameFrom = new int[wallGrid.length][wallGrid[0].length][2];
        for (int i = 0; i < checked.length; i++) {
            for (int j = 0; j < checked[i].length; j++) {
                checked[i][j] = false;
                for (int k = 0; k < 2; k++) {
                    cameFrom[i][j][k] = 0;
                }
            }
        }

        int[][] distanceGrid = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGrid.length; i++) {
            for (int j = 0; j < distanceGrid[0].length; j++) {
                distanceGrid[i][j] = 0;
            }
        }

        int[][] distanceGridGoal = new int[wallGrid.length][wallGrid[0].length];

        for (int i = 0; i < distanceGridGoal.length; i++) {
            for (int j = 0; j < distanceGridGoal[0].length; j++) {
                distanceGridGoal[distanceGridGoal.length - 1 - i][distanceGridGoal[0].length - 1 - j] = 14
                        * Math.min(i, j) + 10 * (Math.max(i, j) - Math.min(i, j));
            }
        }

        int[][][] cameFromGrid;

        cameFromGrid = checkNeighbours(new int[][] { { 0, 0 } }, wallGrid, distanceGrid, distanceGridGoal,
                checked, cameFrom, debug, attempts);
        boolean[][] pathGrid = findPathGrid(cameFromGrid);
        return pathGrid;
    }

    private static boolean[][] findPathGrid(int[][][] cameFromGrid) {
        if(cameFromGrid == null){
            return null;
        }
        boolean[][] foundPathGrid = new boolean[cameFromGrid.length][cameFromGrid[0].length];
        for (int i = 0; i < foundPathGrid.length; i++) {
            for (int j = 0; j < foundPathGrid[0].length; j++) {
                foundPathGrid[i][j] = false;
            }
        }

        int[] find = new int[] { foundPathGrid.length - 1, foundPathGrid[0].length - 1 };

        while (!foundPathGrid[0][0]) {
            foundPathGrid[find[0]][find[1]] = true;
            find = cameFromGrid[find[0]][find[1]];
        }

        return foundPathGrid;
    }

    private static int[][][] checkNeighbours(int[][] coords, boolean[][] wallGrid, int[][] distanceGrid,
            int[][] distanceGridGoal, boolean[][] checked, int[][][] cameFrom, boolean debug, int attempts) {

        while (true) {
            if (debug) {
                printGrid(wallGrid, checked);
                System.out.print("\n" + attempts);
            }

            if (coords.length == 0) {
                return null;
            }

            checked[coords[0][0]][coords[0][1]] = true;

            if (checked[checked.length - 1][checked[0].length - 1]) {
                return cameFrom;
            }

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
        }
    }
}