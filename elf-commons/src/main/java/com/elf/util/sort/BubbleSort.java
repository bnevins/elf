/*
 * BubbleSort.java
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
public class BubbleSort implements SortAlgorithm {

    public BubbleSort() {
    }
    public String getName() {
        return "Bubble Sort";
    }
    @Override
    public boolean isSlow() {
        return true;
    }


    public void sort(int a[]) {
        for (int i = a.length; --i >= 0;) {
            boolean flipped = false;
            dump(0, 0, a);
            for (int j = 0; j < i; j++) {
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    flipped = true;
                    dump(i, j, a);
                }
            }
            if (!flipped) {
                return;
            }
        }
    }

    private void dump(int i, int j, int[] a) {
        if(a.length < 50) {
            System.out.print("i=" +i + " j=" + j + "  ");
            for(int ii : a) {
                System.out.printf("%-4d", ii);
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        int[] i1 = {1, 5, 4, 2, 3, 19, 8, 14, 11};
        new BubbleSort().sort(i1);
    }
}
