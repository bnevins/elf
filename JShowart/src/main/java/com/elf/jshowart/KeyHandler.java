/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

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
        File currFile = Model.get().curr();
        
        if(currFile == null)
            return;
        
        System.out.println("   ********  Handle Key Command -- got a match for " + keyCommand);
        Path root = Path.of(prefs.getKeyCommandsRootDir());
        //keyCommand.getRelativeTo();
        File finalTargetDir = getFinalTarget(keyCommand, currFile);
        Utils.debug("Final Target == %s", finalTargetDir);

        if (!finalTargetDir.exists())
            finalTargetDir.mkdir();
        File finalFile = new File(finalTargetDir, currFile.getName());

        switch (keyCommand.getType()) {
            case COPY -> {
                try {
                    Files.copy(currFile.toPath(), finalFile.toPath());
                } catch (IOException ex) {
                    Utils.debug(ex.toString());
                }
            }
            case MOVE -> {
                try {
                    Files.move(currFile.toPath(), finalFile.toPath());
                    Model.get().delete();
                    Globals.view.refresh();
                } catch (IOException ex) {
                    Utils.debug(ex.toString());
                }
            }
            case INDEX -> {
                throw new UnsupportedOperationException("Not finished yet!");
            }
            case LIST -> {
                throw new UnsupportedOperationException("Not finished yet!");
            }
        }
    }

    private File getFinalTarget(KeyCommand keyCommand, File currFile) {
        String target = keyCommand.getTarget();
        if (target == null || target.isBlank())
            throw new IllegalArgumentException("no target set");

        switch (keyCommand.getRelativeTo()) {
            case PARENT -> {
                File parent = currFile.getParentFile();
                return new File(parent, target);
            }
            case ABSOLUTE -> {
            }
            case ROOT -> {
            }
        }
        return null;
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
