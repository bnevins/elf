/*
 * QuickSort.java
 *
 * Created on November 8, 2007, 11:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class QuickSort  implements SortAlgorithm
{
    
    /** Creates a new instance of QuickSort */
    public QuickSort()
    {
    }
    public String getName() {
        return "Quick Sort";
    }

    private void sort(int a[], int lo0, int hi0)
    {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi)
        {
            return;
        }
        else if( lo == hi - 1 )
        {
            /*
             *  sort a two element list by swapping if necessary
             */
            if (a[lo] > a[hi])
            {
                int T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
            return;
        }
        
        
        /*
         *  Pick a pivot and move it out of the way
         */
        int pivot = a[(lo + hi) / 2];
        ++numPivots;
        a[(lo + hi) / 2] = a[hi];
        a[hi] = pivot;
        
        while( lo < hi )
        {
            /*
             *  Search forward from a[lo] until an element is found that
             *  is greater than the pivot or lo >= hi
             */
            while (a[lo] <= pivot && lo < hi)
            {
                lo++;
            }
            
            /*
             *  Search backward from a[hi] until element is found that
             *  is less than the pivot, or lo >= hi
             */
            while (pivot <= a[hi] && lo < hi )
            {
                hi--;
            }
            
            /*
             *  Swap elements a[lo] and a[hi]
             */
            if( lo < hi )
            {
                ++numSwaps;
                int T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
            
        }
        
        /*
         *  Put the median in the "center" of the list
         */
        a[hi0] = a[hi];
        a[hi] = pivot;
        
        /*
         *  Recursive calls, elements a[lo0] to a[lo-1] are less than or
         *  equal to pivot, elements a[hi+1] to a[hi0] are greater than
         *  pivot.
         */
        sort(a, lo0, lo-1);
        sort(a, hi+1, hi0);
    }
    
    public void sort(int a[])
    {
        numPivots = numSwaps = 0;
        sort(a, 0, a.length-1);
        System.out.println("Num Pivots: " + numPivots + ", Num Swaps: " + numSwaps);
    }
    
    static int numPivots = 0;
    static int numSwaps = 0;

    @Override
    public boolean isSlow() {
        return false;
    }
}
