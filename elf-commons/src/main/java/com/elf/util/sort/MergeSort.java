package com.elf.util.sort;

import com.elf.algorithms.stdlib.StdRandom;
import com.elf.algorithms.stdlib.Stopwatch;
import com.elf.util.Assertion;

/**
 *
 * @author bnevns
 */
public class MergeSort implements SortAlgorithm {

    private static boolean debug = true;
    private int[] tmp;
    private boolean bottomUp = true;
    private int[] arrayToSort;
    private int arrayLength;

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    public void setBottomUp() {
        bottomUp = true;
    }

    public void setTopDown() {
        bottomUp = false;
    }

    @Override
    public void sort(int[] a) {
        arrayToSort = a;
        arrayLength = arrayToSort.length;

        if (arrayLength == 1) // already sorted!!
        {
            return;
        }
        tmp = new int[arrayLength];

        if (bottomUp) {
            sortBottomUp();

        } else {
            sortTopDown(0, arrayLength);
        }

    }

    private void sortBottomUp() {
        for (int width = 1; width < arrayLength; width *= 2) {
            for (int i = 0; i < arrayLength; i += (width * 2)) {
                int low = i;
                int mid = Math.min(i + width, arrayLength);
                int high = Math.min(i + (2 * width), arrayLength);
                debug("width: " + width + "  ");
                merge(low, mid, high);
            }
        }
    }

    // top-down (recursive)
    private void sortTopDown(int low, int high) {
        if (high <= low + 1) {
            return;
        }
        int mid = low + (high - low) / 2;
        debug("SORT TOP DOWN: ", low, mid, high);
        sortTopDown(low, mid);
        sortTopDown(mid, high);
        merge(low, mid, high);
    }

    /* ======================================================           
      Merge does this:

      1. Merge 2 SORTED adjacent pieces of array a[ ] into
         a SORTED array tmp[ ]:

            left array (sorted)         right array (sorted)
           a[iLeft ... iMiddle-1]      a[iMiddle... iRight - 1]
                   \                         /
                    \                       /  sort
                     \                     /
                    tmp[ iLeft ... iRight ]
                                |
                                | Copy back !
                                V
                      a[ iLeft... iRight ]
`
      2. Copy the merged result in tmp[ ] back to a[ ]

      The SAME portion of the array tmp[ ] is used !!!
      ====================================================== 
     */
    private void merge(int left, int middle, int right) {
        debug("MERGE", left, middle, right);
        int i = left;
        int j = middle;
        int k = left;
        while (i < middle || j < right) {
            // elements in both sides - sort!
            if (i < middle && j < right) {
                if (arrayToSort[i] < arrayToSort[j]) {
                    tmp[k++] = arrayToSort[i++];
                } else {
                    tmp[k++] = arrayToSort[j++];
                }
            } else if (i == middle) {
                tmp[k++] = arrayToSort[j++];
            } else if (j == right) {
                tmp[k++] = arrayToSort[i++];
            }
        }
        System.arraycopy(tmp, left, arrayToSort, left, right - left);
    }

    private boolean isSorted() {
        for (int i = 0; i < arrayLength - 1; i++) {
            if (arrayToSort[i] > arrayToSort[i + 1]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        MergeSort sorter = new MergeSort();
        //int[] test = {11,};
        //int[] test = {11, 2, 88, 33, 22, 5, 9, 55, 1};
        final int numInts = 100 * 1000 * 1000;
        int[] toSort = new int[numInts];

        for (int i = 0; i < numInts; i++) {
            // random 0-1 billion
            toSort[i] = StdRandom.uniform(1000 * 1000 * 1000);
        }
        //int[] test = {88, 33, 24, 11, 90, 60, 70, 80, 100, 88,88,88,88};
        Stopwatch timer = new Stopwatch();
        sorter.setTopDown();
        sorter.sort(toSort);
        double time = timer.elapsedTime();
        Assertion.check(sorter.isSorted());
        System.out.printf("%s (%.2f seconds)\n", sorter.getName(), time);
    }

    private void dump(int[] test) {
        System.out.print("{ ");
        for (int i : test) {
            System.out.print(i + " ");
        }
        System.out.println("}");
    }

    private void debug(String s) {
        if (arrayLength < 50) {
            System.out.print(s);
        }
    }

    private void debug(String s, int low, int mid, int high) {
        if (arrayLength < 50) {
            System.out.printf("%s: low=%d, mid=%d, high=%d\n", s, low, mid, high);
        }
    }
//    private static void debug(String s, int low, int high) {
//        System.out.printf("%s: low=%d, high=%d\n", s, low, high);
//    }
}
