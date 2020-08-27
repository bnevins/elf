/*
 * SortAlgorithm.java
 *
 * Created on November 9, 2007, 12:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public interface SortAlgorithm
{
    void sort(int[] a);
    String getName();
    boolean isSlow();
}
