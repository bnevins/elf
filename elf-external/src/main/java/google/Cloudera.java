package google;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Byron Nevins
 */
public class Cloudera {
    private static boolean verbose = true;

    public static void main(String[] args) {

        int[] i1 = new int[]{2, 4, 5, 8, 1, 3, 5, 7};
        int[] i2 = new int[]{1, 3, 5, 7, 9, 11, 14};
        int[] i3 = new int[]{1, 3, 5, 7, 6, 4, 14};
        int[] i4 = new int[]{1, 3, 5, 7, 9, 5, 5, 5, 5, 4, 6, 4, 14};
        /*
        new Cloudera(i1).doOneIterative();
        new Cloudera(i2).doOneIterative();
        new Cloudera(i3).doOneIterative();
         */
        new Cloudera(i1).doOneRecursive();
        new Cloudera(i2).doOneRecursive();
        new Cloudera(i3).doOneRecursive();
        new Cloudera(i4).doOneRecursive();
        verbose = false;
        new Cloudera(i5).doOneRecursive();
    }
    final static int[] i5;
    final static int num = 10000;

    static {
        i5 = new int[num];
        Random r = new Random();

        for (int i = 0; i < num; i++) {
            i5[i] = r.nextInt(10) + 1;
        }
    }
    private final int[] array;
    private int spins;

    public Cloudera(int[] array) {
        this.array = array;

    }

    void doOneIterative() {
        String start = Arrays.toString(array) + " ==> ";
        iterate();
        System.out.println(start + Arrays.toString(array));
    }

    void doOneRecursive() {
        String start = Arrays.toString(array) + " ==> ";
        recurse(0, array.length - 1);
        if (verbose)
            System.out.println(start + Arrays.toString(array));
        System.out.println("Num Spins = " + spins);
    }

    void iterate() {
        int len = array.length;
        int endp = len - 1;
        int startp = 0;

        if (len <= 1)
            throw new IllegalArgumentException();


// move end to first even
        int loopcount = 0;
        int swapcount = 0;
        while (!done(startp, endp, len)) {
            loopcount++;
            while (isEven(array[startp]) && !done(startp, endp, len)) {
                startp++;
            }
            if (done(startp, endp, len))
                break;

            while (isOdd(array[endp]) && !done(startp, endp, len))
                --endp;

            if (done(startp, endp, len))
                break;

            swap(startp, endp--);
            swapcount++;
        }
        System.out.printf("Total spins through the loop = %d and total swaps = %d\n", loopcount, swapcount);
    }

    private void swap(int index1, int index2) {
        int tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }

    private boolean isEven(int n) {
        return (n % 2) == 0;
    }

    private boolean isOdd(int n) {
        return !isEven(n);
    }

    private boolean done(int start, int end, int len) {
        if (start >= len - 1)
            return true;

        if (end <= 0)
            return true;

        if (start >= end)
            return true;

        return false;

    }

    private void recurse(int startEven, int startOdd) {
        // get pointers pointing to first wrong even and odd
        while (startEven < array.length - 1) {
            if (isOdd(array[startEven]))
                break;
            ++startEven;
        }

        if (startEven >= array.length || startEven >= startOdd)
            return; // all done!!!

        while (startOdd >= 0) {
            if (isEven(array[startOdd]))
                break;
            --startOdd;
        }

        if (startOdd <= 0 || startOdd <= startEven)
            return; // all done!!!

        // startEven < startOdd
        // startEven pointing at odd, startOdd pointing at even
        // so swap them!!!
        swap(startEven, startOdd);
        ++spins;
        if(verbose)
            System.out.println("IN PROCESS # " + (spins) + " ==> " + Arrays.toString(array));
        recurse(startEven, startOdd);
    }
}
