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
public class BubbleSort implements SortAlgorithm
{
    public BubbleSort()
    {
    }

    public void sort(int a[]) throws Exception
    {
        for (int i = a.length; --i>=0 ; )
        {
            boolean flipped = false;
            for (int j = 0; j<i; j++)
            {
                if (a[j] > a[j+1])
                {
                    int T = a[j];
                    a[j] = a[j+1];
                    a[j+1] = T;
                    flipped = true;
                }
            }
            if (!flipped)
            {
                return;
            }
        }
    }
}
