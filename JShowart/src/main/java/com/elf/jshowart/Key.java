/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import static java.awt.event.KeyEvent.*;

/**
 *
 * @author bnevins
 */
public class Key {

    private int code;
    private String display;

    public static int getKeyCode(String keyString) {
        // e.g. VK_END key is persisted as a plain string -- "End"
        // VK_COMMA is " , "
        // small array, do a brainless search
        for(Key key : allKeys) 
            if(key.display.equals(keyString))
                return key.code;
        
        throw new IllegalArgumentException("Invalid keystring: " + keyString);
    }
    
    public static Key[] getKeys() {
        return allKeys;
    }

    public Key(int theCode, String theDisplay) {
        code = theCode;
        display = theDisplay;
    }

    @Override
    public String toString() {
        return display;
    }

    public int getCode() {
        return code;
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    
    private static Key[] allKeys = new Key[]{
        new Key(VK_A, "A"),
        new Key(VK_B, "B"),
        new Key(VK_C, "C"),
        new Key(VK_D, "D"),
        new Key(VK_E, "E"),
        new Key(VK_F, "F"),
        new Key(VK_G, "G"),
        new Key(VK_H, "H"),
        new Key(VK_I, "I"),
        new Key(VK_J, "J"),
        new Key(VK_K, "K"),
        new Key(VK_L, "L"),
        new Key(VK_M, "M"),
        new Key(VK_N, "N"),
        new Key(VK_O, "O"),
        new Key(VK_P, "P"),
        new Key(VK_Q, "Q"),
        new Key(VK_R, "R"),
        new Key(VK_S, "S"),
        new Key(VK_T, "T"),
        new Key(VK_U, "U"),
        new Key(VK_V, "V"),
        new Key(VK_W, "W"),
        new Key(VK_X, "X"),
        new Key(VK_Y, "Y"),
        new Key(VK_Z, "Z"),
        new Key(VK_0, "0"),
        new Key(VK_1, "1"),
        new Key(VK_2, "2"),
        new Key(VK_3, "3"),
        new Key(VK_4, "4"),
        new Key(VK_5, "5"),
        new Key(VK_6, "6"),
        new Key(VK_7, "7"),
        new Key(VK_8, "8"),
        new Key(VK_9, "9"),
        new Key(VK_F1, "F1"),
        new Key(VK_F2, "F2"),
        new Key(VK_F3, "F3"),
        new Key(VK_F4, "F4"),
        new Key(VK_F5, "F5"),
        new Key(VK_F6, "F6"),
        new Key(VK_F7, "F7"),
        new Key(VK_F8, "F8"),
        new Key(VK_F9, "F9"),
        new Key(VK_F10, "F10"),
        new Key(VK_PAGE_UP, "Page Up"),
        new Key(VK_PAGE_DOWN, "Page Down"),
        new Key(VK_SLASH, " / "),
        new Key(VK_COMMA, " , "),
        new Key(VK_PERIOD, " . "),
        new Key(VK_SEMICOLON, " ; "),
        new Key(VK_EQUALS, " = "),
        new Key(VK_OPEN_BRACKET, " [ "),
        new Key(VK_CLOSE_BRACKET, " ] "),
        new Key(VK_MINUS, " - "),
        new Key(VK_BACK_SLASH, " \\ "),
        new Key(VK_HOME, "Home"),
        new Key(VK_END, "End"),
        new Key(VK_DELETE, "Delete"),};
}
