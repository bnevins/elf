/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.prefs.Preferences;

/**
 *
 * @author bnevins note: not multi-thread safe turn class into an enum if it becomes a problem...
 */
public final class UserPreferences {

    public Rectangle windowBounds;
    public File previousOpenFileParent;
    public File previousSaveAsFileParent;
    public boolean fitToWindow;
    private String sortType;
    private boolean sortAscending; // descending == false!
    private boolean debug;
    private int slideshowSeconds;

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

    public void read() {
        windowBounds = readWindowBounds();
        previousOpenFileParent = new File(node.get("previousOpenFileParent", "."));
        previousSaveAsFileParent = new File(node.get("previousSaveAsFileParent", "."));
        fitToWindow = node.getBoolean("stretch", true);
        setSlideshowSeconds(node.getInt("slideshowSeconds", 2));
        setDebug(node.getBoolean("debugMode", false));
        setSortType(node.get("sortType", "Name"));
        setSortAscending(node.getBoolean("sortAscending", isSortAscending()));
        setSortAscending(node.getBoolean("sortAscending", isSortAscending()));
    }

    public void write() {
        writeWindowBounds();
        node.put("previousOpenFileParent", previousOpenFileParent.getAbsolutePath());
        node.put("previousSaveAsFileParent", previousSaveAsFileParent.getAbsolutePath());
        node.putBoolean("stretch", fitToWindow);
        node.putBoolean("debugMode", isDebug());
        node.put("sortType", getSortType());
        node.putBoolean("sortAscending", isSortAscending());
        node.putInt("slideshowSeconds", getSlideshowSeconds());
    }

    private final Preferences node;
    private static UserPreferences INSTANCE;

    // TODO -- seems wasteful to get default values everytime...
    private Rectangle readWindowBounds() {

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        int x = width / 2;
        int y = height / 2;

        x = node.getInt("window_left", x);
        y = node.getInt("window_top", y);
        width = node.getInt("window_width", width);
        height = node.getInt("window_height", height);
        return new Rectangle(x, y, width, height);
    }

    private void writeWindowBounds() {
        node.putInt("window_left", windowBounds.x);
        node.putInt("window_top", windowBounds.y);
        node.putInt("window_width", windowBounds.width);
        node.putInt("window_height", windowBounds.height);
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
