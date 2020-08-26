/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sort;

import com.elf.algorithms.stdlib.StdRandom;
import com.elf.algorithms.stdlib.Stopwatch;
import com.elf.util.sort.SortAlgorithm;
import com.elf.util.sort.BubbleSort;
import com.elf.util.sort.HeapSort;
import com.elf.util.sort.InsertionSort;
import com.elf.util.sort.QuickSort;
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
        copyArray();
        sort(new BubbleSort());
        copyArray();
        sort(new QuickSort());
        copyArray();
        sort(new InsertionSort());
    }

    private void sort(SortAlgorithm sorter) {
        Stopwatch timer = new Stopwatch();
        sorter.sort(sorted);
        double time = timer.elapsedTime();
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
        int num = 4 * 100 * 1000;
        if (args.length > 0) {
            num = Integer.parseInt(args[0]);
        }
        new SortTester().test(num);
        /*        
        Stopwatch timer = new Stopwatch();
        new BubbleSort().sort(ints);
        double time = timer.elapsedTime();
        System.out.printf("BubbleSort (%.2f seconds)\n", time);
        timer = new Stopwatch();
        new BubbleSort().sort(ints);
        time = timer.elapsedTime();
        System.out.printf("BubbleSort of sorted array(%.2f seconds)\n", time);
                
         
        Stopwatch timer = new Stopwatch();
        new HeapSort().sort(ints);
        double time = timer.elapsedTime();
        System.out.printf("Heapsort(%.2f seconds)\n", time);

        timer = new Stopwatch();
        new BubbleSort().sort(ints);
        time = timer.elapsedTime();
        System.out.printf("BubbleSort of sorted array(%.2f seconds)\n", time);

        System.arraycopy(original_ints, 0, ints, 0, num);
        timer = new Stopwatch();
        new QuickSort().sort(ints);
        time = timer.elapsedTime();
        System.out.printf("QuickSort of unsorted array(%.2f seconds)\n", time);

        timer = new Stopwatch();
        new QuickSort().sort(ints);
        time = timer.elapsedTime();
        System.out.printf("QuickSort of sorted array(%.2f seconds)\n", time);
         */
    }

    private static String addCommas(long n) {
        NumberFormat myFormat = NumberFormat.getInstance();
        // this will also round numbers, 3 decimal places
        myFormat.setGroupingUsed(true);
        return myFormat.format(n);
    }

}
