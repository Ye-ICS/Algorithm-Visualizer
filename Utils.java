
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Utils {

    static int binarySearch(long[] array, long target) {
        int left = 0, right = array.length - 1;
        boolean found = false;
        int middle = -1;

        while (!found && left <= right) {
            middle = (right + left) / 2;
            if (array[middle] == target) {
                found = true;
            } else if (array[middle] > target) {
                right = middle - 1;
            } else if (array[middle] < target) {
                left = middle + 1;
            }
        }
        if (found) {
            return middle;
        } else {
            return -1; // -1 to represent not found
        }

    }

    static String[] readStrings(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        int length = Integer.parseInt(in.nextLine());
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            strings[i] = in.nextLine();
        }
        return strings;
    }
    static char[] readChars(String filename) throws FileNotFoundException{
        Scanner in = new Scanner(new File(filename));
        int length = Integer.parseInt(in.nextLine());
        char[] chars = new char[length];
        for(int i = 0; i < length; i++){
            chars[i] = in.nextLine().charAt(0);
        }
        return chars;
    }

    static long[] readLongs(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        int length = Integer.parseInt(in.nextLine());

        //in.nextLine();  // Skip the first line. We already know how many entries.
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = Long.parseLong(in.nextLine());

        }
        return longs;

    }

    /**
     * Prompts user for an int between specified low (inclusive) and high
     * (inclusive)
     *
     * @param sc Scanner for terminal input
     * @param low Lowest allowed value
     * @param high Highest allowed value
     * @return Valid user input
     */
    static int getInt(Scanner sc, int low, int high) {
        int input;

        do {
            System.out.print("Please enter value between " + low + " and " + high + ": ");
            String line = sc.nextLine();
            input = Integer.parseInt(line);
        } while (input < low || input > high);

        return input;
    }

    /*
     * Wrapper for System.out.println;
     */
    static <T> void prn(T thingToPrint) {
        System.out.println(thingToPrint);
    }


    /*
     * Wrapper for System.out.print;
     */
    static <T> void prt(T thingToPrint) {
        System.out.print(thingToPrint);
    }
}
