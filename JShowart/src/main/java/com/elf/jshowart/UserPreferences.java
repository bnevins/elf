/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

/**
 *
 * @author bnevins note: not multi-thread safe turn class into an enum if it becomes a problem...
 */
public final class UserPreferences {

    /**
     * @return the maximized
     */
    public boolean isMaximized() {
        return maximized;
    }

    /**
     * @param maximized the maximized to set
     */
    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public Rectangle windowBounds;
    public File previousOpenFileParent;
    public File previousSaveAsFileParent;
    public boolean fitToWindow;
    private String sortType;
    private boolean sortAscending; // descending == false!
    private boolean debug;
    private int slideshowSeconds;
    private boolean maximized;
    private final static String KEY_COMMAND_PREPEND = "KEY_COMMAND_ROW";
    private ArrayList<String> keyCommands = new ArrayList<>();

    private UserPreferences() {
        node = Preferences.userNodeForPackage(this.getClass());
        read();
        System.out.println("USER PREFS CTOR HERE!!!!");
    }

    public static synchronized UserPreferences get() {
        if (INSTANCE == null) {
            INSTANCE = new UserPreferences();
        }
        return INSTANCE;
    }

    public ArrayList<String> getKeyCommands() {
        return keyCommands;
    }
    
    public void read() {
        setDebug(node.getBoolean("debugMode", false));
        setMaximized(node.getBoolean("maximized", false));
        windowBounds = readWindowBounds();
        previousOpenFileParent = new File(node.get("previousOpenFileParent", "."));
        previousSaveAsFileParent = new File(node.get("previousSaveAsFileParent", "."));
        fitToWindow = node.getBoolean("stretch", true);
        setSlideshowSeconds(node.getInt("slideshowSeconds", 2));
        setSortType(node.get("sortType", "Name"));
        setSortAscending(node.getBoolean("sortAscending", true));
        readKeyCommands();
    }

    public void write() {
        writeWindowBounds();
        writeKeyCommands();
        node.put("previousOpenFileParent", previousOpenFileParent.getAbsolutePath());
        node.put("previousSaveAsFileParent", previousSaveAsFileParent.getAbsolutePath());
        node.putBoolean("stretch", fitToWindow);
        node.putInt("slideshowSeconds", getSlideshowSeconds());
        node.putBoolean("debugMode", isDebug());
        node.put("sortType", getSortType());
        node.putBoolean("sortAscending", isSortAscending());
        node.putBoolean("maximized", isMaximized());
    }

    private final Preferences node;
    private static UserPreferences INSTANCE;

    /**
     * Let's be scrupulous to make sure the window doesn't disappear way off-screen somewhere. A maxed window may give a point slightly off screen. We're going
     * to use the default window bounds to be safe. Tough!
     *
     * @return the bounds of the frame to create
     */
    private Rectangle readWindowBounds() {

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int defaultWidth = screenSize.width / 2;
        int defaultHeight = screenSize.height / 2;
        int defaultX = defaultWidth / 2;
        int defaultY = defaultHeight / 2;

        int x = node.getInt("window_left", defaultX);
        int y = node.getInt("window_top", defaultY);
        int width = node.getInt("window_width", defaultWidth);
        int height = node.getInt("window_height", defaultHeight);

        if (debug)
            System.out.println("Read in saved window position: " + new Rectangle(x, y, width, height));

        if (Utils.isVisible(x, y))
            return new Rectangle(x, y, width, height);
        else
            return new Rectangle(defaultX, defaultY, defaultWidth, defaultHeight);
    }

    private void writeWindowBounds() {
        if (debug)
            System.out.println("Writing window position: " + windowBounds);

        node.putInt("window_left", windowBounds.x);
        node.putInt("window_top", windowBounds.y);
        node.putInt("window_width", windowBounds.width);
        node.putInt("window_height", windowBounds.height);
    }

    private void readKeyCommands() {
        for (int i = 0; i < 1000; i++) {
            String name = String.format("%s_%03d", KEY_COMMAND_PREPEND, i);
            String keycommand = node.get(name, "none");

            if (keycommand.equals("none"))
                break;
            
            keyCommands.add(keycommand);
        }
    }

    private void writeKeyCommands() {
        for(int i = 0; i < keyCommands.size(); i++) {
            String name = String.format("%s_%03d", KEY_COMMAND_PREPEND, i);
            node.put(name, keyCommands.get(i));
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    //   Getters and Setters
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the sortType
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    /**
     * @return the sortAscending
     */
    public boolean isSortAscending() {
        return sortAscending;
    }

    /**
     * @param sortAscending the sortAscending to set
     */
    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the slideshowSeconds
     */
    public int getSlideshowSeconds() {
        return slideshowSeconds;
    }

    /**
     * @param slideshowSeconds the slideshowSeconds to set
     */
    public void setSlideshowSeconds(int slideshowSeconds) {
        this.slideshowSeconds = slideshowSeconds;
    }
}
