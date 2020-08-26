/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sort;

import com.elf.algorithms.stdlib.StdRandom;
import com.elf.algorithms.stdlib.Stopwatch;
import com.elf.util.sort.BubbleSort;
import com.elf.util.sort.HeapSort;
import com.elf.util.sort.QuickSort;

/**
 *
 * @author bnevns
 */
public class SortTester {

    public static void main(String[] args) {
        final int num = 1 * 100 * 1000;
        /*
        if (args.length > 0) {
            num = Integer.parseInt(args[0]);
        }
        */
        System.out.println(num + " integers to sort");
        final int[] original_ints = new int[num];
        int[] ints = new int[num];
        System.arraycopy(original_ints, 0, ints, 0, num);

        for (int i = 0; i < num; i++) {
            //ints[i] = StdRandom.uniform(1000);
            //System.out.println(ints[i]);
            ints[i] = StdRandom.uniform(1000 * 1000 * 1000);
        }
        /*        
        Stopwatch timer = new Stopwatch();
        new BubbleSort().sort(ints);
        double time = timer.elapsedTime();
        System.out.printf("BubbleSort (%.2f seconds)\n", time);
        timer = new Stopwatch();
        new BubbleSort().sort(ints);
        time = timer.elapsedTime();
        System.out.printf("BubbleSort of sorted array(%.2f seconds)\n", time);
                System.arraycopy(original_ints, 0, ints, 0, num);
         */
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
        
    }
}
