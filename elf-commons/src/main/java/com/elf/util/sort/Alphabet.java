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
public class Alphabet {
    Alphabet(String s)  {
        string = s;
        chars = s.toCharArray();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Alphabet a = new Alphabet("ACGT");
        System.out.println("String: " + a);
        System.out.println("toChar(2): " + a.tochar(2));
        System.out.println("toIndex(G)" + a.toIndex('G'));
    }
    public String toString() {
        return string;
    }
    public char tochar(int index) {
         return chars[index];
    }
    int toIndex(char c) {
        return string.indexOf(c);
        
    }
    boolean contains(char c) {
        return string.indexOf(c) > -1;
    }
    int R() {
        return string.length();
    }
    int lgR() {
        return -1;
    }
    
    private String string;
    private char[] chars;
}
