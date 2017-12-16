/*
 * SortTester.java
 *
 * Created on November 8, 2007, 11:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.sort;

import java.util.*;

/**
 *
 * @author bnevins
 */
public class SortTester
{
    
    /** Creates a new instance of SortTester */
    public SortTester()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int NUM = 1000000;
        BubbleSort bsort = new BubbleSort();
        QuickSort qsort = new QuickSort();
        
        //runAlgorithm(bsort, NUM);
        runAlgorithm(qsort, NUM);
    }

    private static void runAlgorithm(SortAlgorithm sorter, int num)
    {
        long start, end;
        int[] a;
        try
        {
            a = getRandomArray(num);
            start = System.currentTimeMillis();
            sorter.sort(a);
            end = System.currentTimeMillis();
            System.out.println(sorter.getClass().getName() + " -- Random array of " + num + " ints: " + (end-start) + " msec");

            start = System.currentTimeMillis();
            sorter.sort(a);
            end = System.currentTimeMillis();
            System.out.println(sorter.getClass().getName() + " -- Pre-Sorted array of " + num + " ints: " + (end-start) + " msec");

            a = getWorstCaseArray(num);
            //a = getRandomArray(num);
            start = System.currentTimeMillis();
            sorter.sort(a);
            end = System.currentTimeMillis();
            System.out.println(sorter.getClass().getName() + " -- Worst Case array of " + num + " ints: " + (end-start) + " msec");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    private static int[] getRandomArray(int num)
    {
        int[] a = new int[num];
        Random random = new Random();
        for(int i = 0; i < num; i++)
        {
            a[i] = random.nextInt(num);
        }
        
        //int mid = num /2;
        //a[mid-1] = a[mid] = a[mid+1] = 1;
        return a;
    }

    private static int[] getWorstCaseArray(int num)
    {
        int[] a = new int[num];
        int x = 0;
        for(int i = num - 1; i >= 0; i--)
        {
            a[i] = x++;
        }
        
        return a;
    }
    
}
