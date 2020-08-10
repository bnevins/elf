/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class TextSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int ret = BruteForce("AAAAB", "AAAAAAAB");
        System.out.println("ANswer: " + ret);
    }
    public static int BruteForce(String pattern, String text) {
        int i = 0;
        int j = 0;
        int pLength = pattern.length();
        int tLength = text.length();
        
        while(i < tLength && j < pLength) {
            if(text.charAt(i) == pattern.charAt(j))
                j++;
            else {
                i -= j;
                j = 0;
            }
            ++i;
        }
        if(j == pLength)
            return i - j;
        else
            return -1;
    }
}
