/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util.sort;

/**
 *
 * @author bnevns
 */
public class ShellSort implements SortAlgorithm {

    
    @Override
    public void sort(int[] a) {
        // Start with a big gap, then reduce the gap 
        final int n = a.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // Do a gapped insertion sort for this gap size. 
            // The first gap elements a[0..gap-1] are already in gapped order 
            // keep adding one more element until the entire array is 
            // gap sorted  
            for (int i = gap; i < n; i += 1) {
                // add a[i] to the elements that have been gap sorted 
                // save a[i] in temp and make a hole at position i 
                int temp = a[i];
                // shift earlier gap-sorted elements up until the correct  
                // location for a[i] is found 
                int j = i;
                for (j = i; j >= gap && a[j - gap] > temp; j -= gap) {
                    a[j] = a[j - gap];
                }

                //  put temp (the original a[i]) in its correct location 
                a[j] = temp;
                //dump(i,j,a);
            }
        }
    }

    public void sortxx(int[] a) {
        // Start with a big gap, then reduce the gap 
        final int n = a.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            System.out.println("Gap: " + gap);
            // Do a gapped insertion sort for this gap size. 
            // The first gap elements a[0..gap-1] are already in gapped order 
            // keep adding one more element until the entire array is 
            // gap sorted  
            for (int i = gap; i < n; i += 1) {
                // add a[i] to the elements that have been gap sorted 
                // save a[i] in temp and make a hole at position i 
                System.out.println("Outer Loop i: " + i);
                int temp = a[i];

                // shift earlier gap-sorted elements up until the correct  
                // location for a[i] is found 
                int j = i;

                while (true) {
                    boolean dobreak = false;
                    System.out.printf("j=%d, gap=%d, a[j-gap]=%d, temp=%d\n", j, gap, a[j - gap], temp);
                    if (j >= gap && a[j - gap] > temp) {
                        a[j] = a[j - gap];
                        a[j] = temp;
                    } else {
                        a[j] = temp;
                        dobreak = true;
                    }
                    if(dobreak)
                        break;
                }

                //  put temp (the original a[i]) in its correct location 
            }
        }
    }

    @Override
    public String getName() {
        return "Shell Sort";
    }

    @Override
    public boolean isSlow() {
        return false;
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

    public static void main(String[] args) {
        int[] i1 = {110, 50, 14, 2, 27,3, 11, 19};
        dump(0, 0, i1);
        new ShellSort().sort(i1);
        dump(0, 0, i1);
    }
    private static boolean debug = true;
}
