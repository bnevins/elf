/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.prefs.Preferences;

/**
 *
 * @author bnevins note: not multi-thread safe turn class into an enum if it becomes a problem...
 */
public final class UserPreferences {

    private Rectangle windowBounds;
    private File previousOpenFileParent;
    private File previousSaveAsFileParent;
    private boolean fitToWindow;
    private String sortType;
    private boolean sortAscending; // descending == false!
    private boolean debug;
    private int slideshowSeconds;
    private boolean maximized;
    private final static String KEY_COMMAND_PREPEND = "KEY_COMMAND_ROW";
    private ArrayList<String> keyCommands = new ArrayList<>();
    private String keyCommandsRootDir;
    private final Preferences mainNode;
    private static UserPreferences INSTANCE;
    private ArrayList<PreferencesListener> listeners = new ArrayList<>();
    private final static String ROOTDIR = "RootDir";

    private UserPreferences() {
        mainNode = Preferences.userNodeForPackage(this.getClass());
        read();
    }

    public static synchronized UserPreferences get() {
        if (INSTANCE == null) {
            INSTANCE = new UserPreferences();
        }
        return INSTANCE;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public ArrayList<String> getKeyCommandsAsStrings() {
        return keyCommands;
    }

    public void read() {
        setDebug(mainNode.getBoolean("debugMode", false));
        setMaximized(mainNode.getBoolean("maximized", false));
        setWindowBounds(readWindowBounds());
        setPreviousOpenFileParent(new File(mainNode.get("previousOpenFileParent", ".")));
        setPreviousSaveAsFileParent(new File(mainNode.get("previousSaveAsFileParent", ".")));
        setFitToWindow(mainNode.getBoolean("stretch", true));
        setSlideshowSeconds(mainNode.getInt("slideshowSeconds", 2));
        setSortType(mainNode.get("sortType", "Name"));
        setSortAscending(mainNode.getBoolean("sortAscending", true));
        readKeyCommands();
        readKeyCommandsRootDir();
    }

    public void write() {
        writeWindowBounds();
        writeKeyCommands();
        writeKeyCommandsRootDir();
        mainNode.put("previousOpenFileParent", getPreviousOpenFileParent().getAbsolutePath());
        mainNode.put("previousSaveAsFileParent", getPreviousSaveAsFileParent().getAbsolutePath());
        mainNode.putBoolean("stretch", isFitToWindow());
        mainNode.putInt("slideshowSeconds", getSlideshowSeconds());
        mainNode.putBoolean("debugMode", isDebug());
        mainNode.put("sortType", getSortType());
        mainNode.putBoolean("sortAscending", isSortAscending());
        mainNode.putBoolean("maximized", isMaximized());
    }

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

        int x = mainNode.getInt("window_left", defaultX);
        int y = mainNode.getInt("window_top", defaultY);
        int width = mainNode.getInt("window_width", defaultWidth);
        int height = mainNode.getInt("window_height", defaultHeight);

        if (debug)
            System.out.println("DEBUG:  Read in saved window position: " + new Rectangle(x, y, width, height));

        if (Utils.isVisible(x, y))
            return new Rectangle(x, y, width, height);
        else
            return new Rectangle(defaultX, defaultY, defaultWidth, defaultHeight);
    }

    private void readKeyCommandsRootDir() {
        keyCommandsRootDir = mainNode.get(ROOTDIR, getDefaultKeyCommandsRootDirPath());
    }

    private void writeWindowBounds() {
        if (debug)
            System.out.println("DEBUG:  Writing window position: " + getWindowBounds());

        mainNode.putInt("window_left", getWindowBounds().x);
        mainNode.putInt("window_top", getWindowBounds().y);
        mainNode.putInt("window_width", getWindowBounds().width);
        mainNode.putInt("window_height", getWindowBounds().height);
    }

    // TODO - KLUDGY!!
    private void readKeyCommands() {
        keyCommands.clear();
        for (int i = 0; i < 1000; i++) {
            String name = String.format("%s_%03d", KEY_COMMAND_PREPEND, i);
            String keycommand = mainNode.get(name, "none");

            if (keycommand.equals("none")) {
                // get rid of the key we just added!
                mainNode.remove(name);
                break;
            }

            keyCommands.add(keycommand);
        }
        if (debug)
            System.out.printf("DEBUG:  Read in %d Key Commands\n", keyCommands.size());
    }

    // TODO - KLUDGY!!
    private void writeKeyCommands() {
        if (debug)
            System.out.printf("DEBUG:  Writing %d Key Commands\n", keyCommands.size());

        clearKeyCommands();
        for (int i = 0; i < keyCommands.size(); i++) {
            String name = String.format("%s_%03d", KEY_COMMAND_PREPEND, i);
            mainNode.put(name, keyCommands.get(i));
        }
    }

    private String getDefaultKeyCommandsRootDirPath() {
        File f = new File(".");
        // getAbsolute appends "\." -- so use Canonical...
        try {
            return f.getCanonicalPath();
        } catch (IOException ioe) {
            return f.getAbsolutePath();
        }
    }

    private void writeKeyCommandsRootDir() {
        if (keyCommandsRootDir == null || keyCommandsRootDir.isBlank()) {
            keyCommandsRootDir = getDefaultKeyCommandsRootDirPath();
        } else {
            keyCommandsRootDir = Path.of(keyCommandsRootDir).toAbsolutePath().toString();
        }

        if (debug)
            System.out.printf("DEBUG:  Writing Root Dir for KeyCommands: %s\n", keyCommandsRootDir);

        mainNode.put(ROOTDIR, keyCommandsRootDir);
    }

    // TODO - KLUDGY!!
    private void clearKeyCommands() {
        for (int i = 0; i < 1000; i++) {
            String name = String.format("%s_%03d", KEY_COMMAND_PREPEND, i);
            String keycommand = mainNode.get(name, "none");
            mainNode.remove(name);

            if (keycommand.equals("none"))
                break;
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

    public void addListener(PreferencesListener listener) {
        listeners.add(listener);
    }

    public void fireNotify() {
        for (PreferencesListener listener : listeners)
            listener.preferencesChanged();
    }

    public Rectangle getWindowBounds() {
        return windowBounds;
    }

    public void setWindowBounds(Rectangle windowBounds) {
        this.windowBounds = windowBounds;
    }

    /**
     * @return the previousOpenFileParent
     */
    public File getPreviousOpenFileParent() {
        return previousOpenFileParent;
    }

    /**
     * @param previousOpenFileParent the previousOpenFileParent to set
     */
    public void setPreviousOpenFileParent(File previousOpenFileParent) {
        this.previousOpenFileParent = previousOpenFileParent;
    }

    /**
     * @return the previousSaveAsFileParent
     */
    public File getPreviousSaveAsFileParent() {
        return previousSaveAsFileParent;
    }

    /**
     * @param previousSaveAsFileParent the previousSaveAsFileParent to set
     */
    public void setPreviousSaveAsFileParent(File previousSaveAsFileParent) {
        this.previousSaveAsFileParent = previousSaveAsFileParent;
    }

    /**
     * @return the fitToWindow
     */
    public boolean isFitToWindow() {
        return fitToWindow;
    }

    /**
     * @param fitToWindow the fitToWindow to set
     */
    public void setFitToWindow(boolean fitToWindow) {
        this.fitToWindow = fitToWindow;
    }

    /**
     * @return the keyCommandsRootDir
     */
    public String getKeyCommandsRootDir() {
        return keyCommandsRootDir;
    }

    /**
     * @param keyCommandsRootDir the keyCommandsRootDir to set
     */
    public void setKeyCommandsRootDir(String keyCommandsRootDir) {
        this.keyCommandsRootDir = keyCommandsRootDir;
    }

}
