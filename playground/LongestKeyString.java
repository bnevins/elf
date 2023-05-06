/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart.playground;

import java.awt.event.*;

/**
 *
 * @author bnevins
 */
public class LongestKeyString {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String longest = "";
        
        for(int i = 0; i < 256; i++) {
            String s = KeyEvent.getKeyText(i);
            String ss = s.toUpperCase();
            
            if(ss.startsWith("UNKNOWN"))
                continue;
            if(ss.startsWith("DEAD"))
                continue;
            if(ss.startsWith("ALPHA"))
                continue;
            if(ss.startsWith("ROMAN"))
                continue;
            
            if(s.length() > longest.length())
                longest = s;
            
            System.out.println(s);
        }
        System.out.printf("\n\nLONGEST Key String is %d chars,  %s\n", longest.length(), longest);
    }
    
}
