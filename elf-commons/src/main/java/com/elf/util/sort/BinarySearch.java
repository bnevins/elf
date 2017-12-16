/*
 * BinarySearch.java
 *
 * Created on November 9, 2007, 10:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class BinarySearch
{

    public BinarySearch(int[] a, int elem)
    {
        this.a = a;
        this.elem = elem;
    }

    public int search()
    {
        search(0, a.length - 1, elem);
        return numSearches;
    }
    

    private static int[] getSortedArray(int num)
    {
        int[] a = new int[num];
        for(int i = 0; i < num; i++)
        {
            a[i] = i;
        }
        
        return a;
    }

    private boolean search(int lo, int hi, int elem)
    {
        ++numSearches;
        System.out.println("Lo:Hi " + lo + " : " + hi);
        if(lo > hi)
        {
            System.out.println("ERROR");
        }
        
        if(lo == hi)
        {
            return a[lo] == elem;
        }
        
        int mid = (lo + hi) >>> 1;
        
        if(elem > a[mid])
            return search(mid + 1, hi, elem);
        if(elem < a[mid])
            return search(lo, mid - 1, elem);
        else
            return true;
    }
    public static void main(String[] args)
    {
        int num = 200000000;
        System.out.println("NUM " + num);
        int elem = 73;
        
        BinarySearch search = new BinarySearch(getSortedArray(num), elem);
        System.out.println("search returned: " + search.search());
    }
    private int[] a;
    private int elem;
    private int numSearches = 0;
}
