/*
 * InsertionSort.java
 *
 */
package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class InsertionSort implements SortAlgorithm {

    public InsertionSort() {
    }

    public String getName() {
        return "Insertion Sort";
    }

    public void sort(int array[]) {
        int n = array.length;
        for (int j = 1; j < n; j++) {
            int key = array[j];
            int i = j - 1;
            while ((i > -1) && (array[i] > key)) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = key;
        }
    }

    public static void main(String[] args) {
        int[] i1 = {1, 5, 4, 2, 3, 19, 8, 14, 11};
        System.out.println("BEFORE: ");
        for(int i : i1)
            System.out.print(i + " ");
        System.out.println("");

        new InsertionSort().sort(i1);

        System.out.println("AFTER: ");
        for(int i : i1)
            System.out.print(i + " ");
        System.out.println("");
    }
}
