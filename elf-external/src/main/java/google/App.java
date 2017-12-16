package google;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App {
    interface PartitionCondition {
        /**
         * Decides if the condition true for given array.
         *
         * @param ints array of integers
         * @param ci the current index
         * @param pi the index of pivot element
         */
        boolean isTrue(int[] ints, int ci, int pi);
    }

    static class QuicksortPartitioner implements PartitionCondition {
        public boolean isTrue(int[] ints, int fi, int si) {
            return ints[fi] < ints[si];
        }
    }

    static class OddEvenPartitioner implements PartitionCondition {
        public boolean isTrue(int[] ints, int ci, int pi) {
            // defining isTrue to mean "odd"
            
            
            return true;  // Change this line
        }
    }

    static void swap(int[] ints, int fi, int si) {
        int tmp = ints[fi];
        ints[fi] = ints[si];
        ints[si] = tmp;
    }

    static int partition(int[] ints, final int first, int last, PartitionCondition partitionChecker) {
        int pivot = first; /*
         * first element is the pivot
         */
        for (int i = first + 1; i <= last; i++) {
            if (partitionChecker.isTrue(ints, i, first)) {
                pivot++;
                swap(ints, i, pivot);
                System.out.println(Arrays.toString(ints));
            }
        }
        swap(ints, pivot, first);
        return pivot;
    }

    static int partition(int[] a) {
        return partition(a, 0, a.length - 1, new QuicksortPartitioner());
    }

    static int partitionOddEven(int[] ints) {
        return partition(ints, 0, ints.length - 1, new OddEvenPartitioner());
    }

    public static void main(String... args) {
        int[] a = {11, 52, 566, 32, 63, 6567, -2, 4, 0, 2, 66, 773};
        System.out.println("Given Array: " + Arrays.toString(a));
        int pi = partition(a);
        System.out.println(Arrays.toString(a));
        System.out.println("Partition index: " + pi + ", partition element: " + a[pi]);
        pi = partitionOddEven(a);
        System.out.println(Arrays.toString(a));
        System.out.println("Partition index: " + pi + ", partition element: " + a[pi]);
    }
}
//Compile and run the program: javac Partition.java; java Partition. It should partition it the way Quicksort needs.
//The problem is: Change the line #18 in the above program so that it partitions the array as evens/odds correctly.
