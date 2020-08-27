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
    @Override
    public boolean isSlow() {
        return true;
    }

    public void sort(int array[]) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while ((j > -1) && (array[j] > key)) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
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
