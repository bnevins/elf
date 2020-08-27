/*
 * SelectionSort.java
 *
 * Created on November 8, 2007, 11:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class SelectionSort implements SortAlgorithm {

    public SelectionSort() {
    }

    public String getName() {
        return "Selection Sort";
    }

    public void sort(int a[]) {
        int num = a.length;
        int numAccess = 0;
        int numExchanges = 0;
        for (int i = 0; i < num - 1; i++) {
            int key = a[i];
            ++numAccess;
            for (int j = i + 1; j < num; j++) {
                ++numAccess;
                if (a[j] < key) {
                    ++numExchanges;
                    exch(a, i, j);
                    key = a[i];
                }
                dump(i, j, a);
            }
        }
        dump(numAccess, numExchanges);
    }

    private void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void dump(int i, int j, int[] a) {
        if (!debug) {
            return;
        }
        if (a.length < 50) {
            System.out.print("i=" + i + " j=" + j + "  ");
            for (int ii : a) {
                System.out.printf("%-4d", ii);
            }
            System.out.println("");
        }
    }

    private static void dump(int acc, int exc) {
        if (!debug) {
            return;
        }
        System.out.printf("Accesses = %d, Exchanges = %d\n", acc, exc);
    }
    @Override
    public boolean isSlow() {
        return true;
    }

    public static void main(String[] args) {
        int[] i1 = {1, 5, 4, 2, 3, 19, 8, 14};
        dump(0, 0, i1);
        new SelectionSort().sort(i1);
        dump(0, 0, i1);
        new SelectionSort().sort(i1);
        dump(0, 0, i1);
    }
    private static final boolean debug = true;
}
