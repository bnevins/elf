/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class KeyHandler implements KeyListener, PreferencesListener {

    private UserPreferences prefs = UserPreferences.get();
    private TreeSet<KeyCommand> keyCommands = new TreeSet<>();
    //private ArrayList<KeyCommand> keyCommands = new ArrayList<>();

    public KeyHandler() {
        prefs.addListener(this);
        readKeyCommands();
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        if (prefs.isDebug())
//            System.out.print(getKeyInfo(e, "KeyHandler KEY_TYPED"));

        char key = e.getKeyChar();
        switch (key) {
            case KeyEvent.VK_ESCAPE ->
                Globals.controller.toggleFullScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (prefs.isDebug())
            System.out.println(getKeyInfo(e, ""));

        int key = e.getKeyCode();

        int mods = e.getModifiersEx();

        switch (key) {
            case VK_SPACE ->
                Globals.view.nextImage();
        }

        handleKeyCommand(key, mods);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void preferencesChanged() {
        readKeyCommands();
    }

    private void readKeyCommands() {
        keyCommands.clear();
        ArrayList<String> keyCommandStrings = prefs.getKeyCommandsAsStrings();

        for (String s : keyCommandStrings) {
            keyCommands.add(new KeyCommand(s));
        }

        if (prefs.isDebug()) {
            Utils.debug("KeyHandler read in these key commands: ***********\n");
            for (KeyCommand kc : keyCommands)
                Utils.debug(kc.toString());

            Utils.debug("KeyHandler end key commands: ***********\n");

        }
    }

    private void handleKeyCommand(int keyCode, int mods) {
        for (KeyCommand keyCommand : keyCommands) {
            if (keyCode == keyCommand.getKeyCode() && mods == keyCommand.getMods()) {
                handleKeyCommand(keyCommand);
                return;
            }
        }
    }

    private void handleKeyCommand(KeyCommand keyCommand) {
        System.out.println("   ********  Handle Key Command -- got a match for " + keyCommand);
        Path root = Path.of(prefs.getKeyCommandsRootDir());
        
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // stolen code ONLY used for debugging
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        String out = String.format("%s    %s    %s    %s    %s", keyStatus, keyString, modString, actionString, locationString);
        return out;
    }
}
