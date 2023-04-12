/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import static java.awt.event.KeyEvent.*;

/**
 *
 * @author bnevins
 */
public class KeyHandler implements KeyListener {

    private UserPreferences prefs = UserPreferences.get();

    @Override
    public void keyTyped(KeyEvent e) {
        if (prefs.isDebug())
            System.out.print(getKeyInfo(e, "KeyHandler KEY_TYPED"));

        char key = e.getKeyChar();
        switch (key) {
            case KeyEvent.VK_ESCAPE ->
                Globals.frame.toggleFullScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (prefs.isDebug())
            System.out.print(getKeyInfo(e, "KeyHandler KEY_PRESSED"));
        int key = e.getKeyCode();
        //System.out.println("KEY PRESSED == " + key);
        switch (key) {
            case VK_SPACE, VK_RIGHT ->
                Globals.view.nextImage();
            case VK_LEFT ->
                Globals.view.prevImage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private String getKeyInfo(KeyEvent e, String keyStatus) {

        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }

        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }

        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }

        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }

        String out = String.format("%s\n    %s\n    %s\n    %s\n    %s\n", keyStatus, keyString, modString, actionString, locationString);
        return out;
    }
}