package com.elf.text;

/**
 *
 * @author bnevins
 */
public class MergeSortComparable {

    private final Comparable[] tmp;
    private final Comparable[] arrayToSort;
    private final int arrayLength;

    public MergeSortComparable(Comparable[] a) {
        arrayToSort = a;
        arrayLength = arrayToSort.length;
        tmp = new Comparable[arrayLength];
    }

    public void sort() {
        for (int width = 1; width < arrayLength; width *= 2) {
            for (int i = 0; i < arrayLength; i += (width * 2)) {
                int low = i;
                int mid = Math.min(i + width, arrayLength);
                int high = Math.min(i + (2 * width), arrayLength);
                merge(low, mid, high);
            }
        }
    }

    private void merge(int left, int middle, int right) {
        int i = left;
        int j = middle;
        int k = left;
        while (i < middle || j < right) {
            // elements in both sides - sort!
            if (i < middle && j < right) {
                if (arrayToSort[i].compareTo(arrayToSort[j]) < 0) {
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
    public static void main(String[] args) {
        Integer[] arr = new Integer[] { 100, 20, 30, 5, 1, 99, 77 };
        for(Integer i : arr)
            System.out.print(i + " ");
        System.out.println("");
        MergeSortComparable sorter = new MergeSortComparable(arr);
        sorter.sort();
        for(Integer i : arr)
            System.out.print(i + " ");
        System.out.println("");
    }
}
