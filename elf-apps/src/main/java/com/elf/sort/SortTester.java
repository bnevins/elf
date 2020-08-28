/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sort;

import com.elf.algorithms.stdlib.StdRandom;
import com.elf.algorithms.stdlib.Stopwatch;
import com.elf.util.Assertion;
import com.elf.util.sort.SortAlgorithm;
import com.elf.util.sort.BubbleSort;
import com.elf.util.sort.HeapSort;
import com.elf.util.sort.InsertionSort;
import com.elf.util.sort.MergeSort;
import com.elf.util.sort.QuickSort;
import com.elf.util.sort.SelectionSort;
import com.elf.util.sort.ShellSort;
import java.text.NumberFormat;

/**
 *
 * @author bnevns
 */
public class SortTester {

    private int numInts;
    private int[] unsorted;
    private int[] sorted;
    private String numIntsString;

    void test(int aNumInts) {
        numInts = aNumInts;
        makeRandomArrays();
        numIntsString = addCommas(numInts);
        System.out.println(numIntsString + " integers to sort");
        sort((SortAlgorithm) new HeapSort());

        //it's super slow -- but sorts pre-sorted arrays very fast!
        //copyArray();
        sort(new BubbleSort());
        copyArray();
        sort(new QuickSort());
        copyArray();
        sort(new InsertionSort());
        sort(new InsertionSort());
        copyArray();
        sort(new SelectionSort());
        sort(new SelectionSort());
        copyArray();
        sort(new ShellSort());
        sort(new ShellSort());
        copyArray();
        MergeSort mergeSort = new MergeSort();
        mergeSort.setBottomUp();
        sort(mergeSort);
        copyArray();
        mergeSort.setTopDown();
        sort(mergeSort);

    }

    private void sort(SortAlgorithm sorter) {
        if (sorted.length > 10000 && sorter.isSlow()) {
            System.out.printf("%s (is too slow to run...\n", sorter.getName());
            return;
        }
        Stopwatch timer = new Stopwatch();
        sorter.sort(sorted);
        double time = timer.elapsedTime();
        Assertion.check(isSorted(sorted));
        System.out.printf("%s (%.2f seconds)\n", sorter.getName(), time);
    }

    private void copyArray() {
        System.arraycopy(unsorted, 0, sorted, 0, numInts);
    }

    private void makeRandomArrays() {
        unsorted = new int[numInts];
        sorted = new int[numInts];

        for (int i = 0; i < numInts; i++) {
            // random 0-1 billion
            sorted[i] = StdRandom.uniform(1000 * 1000 * 1000);
            unsorted[i] = sorted[i];
        }
    }

    public static void main(String[] args) {
        int num = 10 * 1000 * 1000;
        if (args.length > 0) {
            num = Integer.parseInt(args[0]);
        }
        new SortTester().test(num);
        //System.arraycopy(num, num, num, num, num);
    }

    private static String addCommas(long n) {
        NumberFormat myFormat = NumberFormat.getInstance();
        // this will also round numbers, 3 decimal places
        myFormat.setGroupingUsed(true);
        return myFormat.format(n);
    }

    private static boolean isSorted(int[] sorted) {
        for(int i = 0; i < sorted.length - 1; i++)
            if(sorted[i] > sorted[i+1])
                return false;
        return true;
    }
}