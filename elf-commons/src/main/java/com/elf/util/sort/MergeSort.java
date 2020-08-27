/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util.sort;

import com.elf.util.Assertion;

/**
 *
 * @author bnevns
 */
public class MergeSort implements SortAlgorithm {

    private int[] tmp;
    private boolean bottomUp = false;

    @Override
    public void sort(int[] a) {

        int N = a.length;

        tmp = new int[N];

        if (!bottomUp) {
            sort(a, 0, a.length);
            return;
        }

        for (int i = 0; i < N; i += 2) {
            int high = i + 1;
            if (high >= a.length) {
                high = i;
            }

            merge(a, i, i, high);
        }

    }

    // top-down (recursive)
    private void sort(int[] a, int low, int high) {
        if (high <= low + 1) {
            return;
        }
        int mid = low + (high - low) / 2;
        sort(a, low, mid);
        sort(a, mid, high);
        merge(a, low, mid, high);
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
    
    private void merge(int arr[], int left, int middle, int right) {
        int i = left;
        int j = middle;
        int k = left;
        while (i < middle || j < right) {
            // elements in both sides - sort!
            if (i < middle && j < right) {
                if (arr[i] < arr[j]) {
                    tmp[k++] = arr[i++];
                } else {
                    tmp[k++] = arr[j++];
                }
            } else if (i == middle) {
                //Assertion.check(j == (right - 1));
                tmp[k++] = arr[j++];
            } else if (j == right) {
                //Assertion.check(i == (middle - 1));
                tmp[k++] = arr[i++];
            }
        }
        System.arraycopy(tmp, left, arr, left, right - left);
    }

    // Merges two subarrays of arr[].  First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    private void mergex(int arr[], int left, int mid, int right) {
        int i = left;
        int j = mid + 1;

        for (int k = 0; k < right - left + 1; k++) {
            // right-side emptied.  Draw from left
            if (j > right) {
                tmp[k] = arr[i++];
            } // left-side empty
            else if (i > mid) {
                tmp[k] = arr[j++];
            } else {

                if (arr[i] < arr[j]) {
                    tmp[k] = arr[i++];
                } else {
                    tmp[k] = arr[j++];
                }
            }
        }
        System.arraycopy(tmp, 0, arr, left, right - left + 1);
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public boolean isSlow() {
        return false;
    }

    public static void main(String[] args) {
        MergeSort sorter = new MergeSort();
        int[] test = {88, 33};
        //int[] test = {88, 33, 24, 11, 90, 60, 70, 80, 100};
        dump(test);
        sorter.sort(test);
        dump(test);
    }

    private static void dump(int[] test) {
        System.out.print("{ ");
        for (int i : test) {
            System.out.print(i + " ");
        }
        System.out.println("}");
    }
}
