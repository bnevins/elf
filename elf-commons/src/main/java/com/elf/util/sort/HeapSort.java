/*
 * HeapSort.java
 *
 * Created on November 21, 2007, 12:07 PM
 *
 */
package com.elf.util.sort;

import java.util.*;

/**
 *
 * @author bnevins
 */
public class HeapSort implements SortAlgorithm {

    public HeapSort() {

    }

    public String getName() {
        return "Heapsort";
    }

    public static void main(String[] args) {
        int[] i1 = {1, 5, 4, 2, 3, 19, 8, 14, 11};

        int len = 1800000;
        int[] i2 = new int[len];
        Random ran = new Random(System.nanoTime());

        for (int i = 0; i < len; i++) {
            i2[i] = ran.nextInt();
        }

        int[] a = i2;

        long start = System.nanoTime();
        new HeapSort().sort(a);
        long end = System.nanoTime();
        System.out.println("Time for " + a.length + " elements: " + ((end - start) / 1000000) + " msec");
        System.out.println("nsec per element: " + ((end - start) / len));

    }

    public void sort(int a[]) {
        makeHeap(a);
        if (a.length < 100) {
            System.out.println("Make Heap: " + Arrays.toString(a));
        }
        sortHeap(a);
        if (a.length < 100) {
            System.out.println("Sorted: " + Arrays.toString(a));
        }
    }

    private void makeHeap(int[] a) {
        int len = a.length;

        makeHeap(a, len);
    }

    private void makeHeap(int[] a, int len) {
        // start at the last parent...
        int start = (len >>> 1) - 1;
        for (int i = start; i >= 0; i--) {
            sift(a, len, i);
        }
    }

    private void sift(int[] a, int len, int i) {
        int half = len >>> 1;

        while (i < half) {
            // j is guaranteed to be < len
            int j = (i * 2) + 1;
            int child = a[j];

            if (j + 1 < len) {
                if (a[j + 1] > child) {
                    j = j + 1;
                    child = a[j];
                }
            }

            if (a[i] < child) {
                swap(a, i, j);
                i = j;
            } else {
                break;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private void sortHeap(int[] a) {
        // the highest element is at index=0
        // so swap it with the last element and then re-sift and repeat

        for (int i = a.length - 1; i > 0; i--) {
            swap(a, 0, i);
            sift(a, i, 0);
        }
    }
}
