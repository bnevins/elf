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
public class KeyCode {

    private int code;
    private String display;
    private String description;

    public static KeyCode[] getKeyCodes() {
        return allKeys;
    }

    public KeyCode(int theCode, String theDisplay, String theDescription) {
        code = theCode;
        display = theDisplay;
        description = theDescription;
    }

    public KeyCode(int theCode, String theDisplay) {
        code = theCode;
        display = theDisplay;
        description = "";
    }

    @Override
    public String toString() {
        return display;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    private static KeyCode[] allKeys = new KeyCode[]{
        new KeyCode(VK_ENTER, "Enter"),
        new KeyCode(VK_A, "A"),
        new KeyCode(VK_B, "B"),
        new KeyCode(VK_C, "C"),
        new KeyCode(VK_D, "D"),
        new KeyCode(VK_E, "E"),
        new KeyCode(VK_F, "F"),
        new KeyCode(VK_G, "G"),
        new KeyCode(VK_H, "H"),
        new KeyCode(VK_I, "I"),
        new KeyCode(VK_J, "J"),
        new KeyCode(VK_K, "K"),
        new KeyCode(VK_L, "L"),
        new KeyCode(VK_M, "M"),
        new KeyCode(VK_N, "N"),
        new KeyCode(VK_O, "O"),
        new KeyCode(VK_P, "P"),
        new KeyCode(VK_Q, "Q"),
        new KeyCode(VK_R, "R"),
        new KeyCode(VK_S, "S"),
        new KeyCode(VK_T, "T"),
        new KeyCode(VK_U, "U"),
        new KeyCode(VK_V, "V"),
        new KeyCode(VK_W, "W"),
        new KeyCode(VK_X, "X"),
        new KeyCode(VK_Y, "Y"),
        new KeyCode(VK_Z, "Z"),
        new KeyCode(VK_0, "0"),
        new KeyCode(VK_1, "1"),
        new KeyCode(VK_2, "2"),
        new KeyCode(VK_3, "3"),
        new KeyCode(VK_4, "4"),
        new KeyCode(VK_5, "5"),
        new KeyCode(VK_6, "6"),
        new KeyCode(VK_7, "7"),
        new KeyCode(VK_8, "8"),
        new KeyCode(VK_9, "9"),

        new KeyCode(VK_F1, "F1"),
        new KeyCode(VK_F2, "F2"),
        new KeyCode(VK_F3, "F3"),
        new KeyCode(VK_F4, "F4"),
        new KeyCode(VK_F5, "F5"),
        new KeyCode(VK_F6, "F6"),
        new KeyCode(VK_F7, "F7"),
        new KeyCode(VK_F8, "F8"),
        new KeyCode(VK_F9, "F9"),
        new KeyCode(VK_F10, "F10"),

        new KeyCode(VK_PAGE_UP, "Page Up"),
        new KeyCode(VK_PAGE_DOWN, "Page Down"),};

}
