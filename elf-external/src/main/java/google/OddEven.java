package google;

import java.lang.RuntimeException;
import java.util.*;

/**
 * Do NOT want a recursive solution! One pass will separate properly
 *
 */
public class OddEven {
    private final int[] ints;
    private final int len;

    public static void main(String... args) {
        int[] a1 = {11, 52, 566, 32, 63, 6567, -2, 4, 0, 2, 66, 773};
        int[] a2 = {2, 3, 5, 4};
        int[] a3 = { 2, 2, 2, 2, 2, 2, 2, 4};
        int[] a4 = { 1, 3, 5, 111, -99 };
        int[] a5= { 0, 1, 3, 5, 111, -99 };
        int[] a6= { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // bad
        int[] a7= { 2, 4, 4, 6, 8, 10, 12, 3, 5, 7, 9,11, 13, 15 }; // worst
        
        
        OddEven oddeven = new OddEven(a1);
        oddeven = new OddEven(a2);
        oddeven = new OddEven(a3);
        oddeven = new OddEven(a4);
        oddeven = new OddEven(a5);
        oddeven = new OddEven(a6);
        oddeven = new OddEven(a7);
    }

    public OddEven(int[] ints) {
        this.ints = ints;
        len = ints.length;
        partition();
    }

    private void partition() {
        // odds on the left, evens on the right
        int firstEven = 0;
        int firstOdd = len - 1;
        int count = 0;

        dump("Starting Array ==> ");

        while (true) {
            firstEven = getFirstEven(firstEven);
            firstOdd = getFirstOdd(firstOdd);

            // if any of these 3 things are true -- then we're finished!
            if (firstEven >= len) {
                break;
            }
            if (firstOdd < 0) {
                break;
            }

            if (firstEven >= firstOdd) {
                break;
            }

            swap(firstEven++, firstOdd--);
            dump("Swap #" + (++count) + " ==> ");
        }
        dump("Ending Array ==> ");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }

    private boolean isOdd(int i) {
        return i % 2 != 0;
    }

    private boolean isEven(int i) {
        return !isOdd(i);
    }

    private void swap(int i1, int i2) {
        if (i1 == i2) {
            throw new RuntimeException("Algorithm Error!!!");
        }

        int tmp = ints[i1];
        ints[i1] = ints[i2];
        ints[i2] = tmp;
    }

    private void dump(String msg) {
        System.out.printf("%s%s\n", msg, Arrays.toString(ints));
    }

    private int getFirstOdd(int right) {
        for (int i = right; i >= 0; i--) {
            if (isOdd(ints[i])) {
                return i;
            }
        }
        return -1;
    }

    private int getFirstEven(int left) {
        for (int i = left; i < len; i++) {
            if (isEven(ints[i])) {
                return i;
            }
        }
        return len;
    }
}
 