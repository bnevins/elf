/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.JShowart;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.prefs.Preferences;

/**
 *
 * @author bnevins note: not multi-thread safe turn class into an enum if it
 * becomes a problem...
 */
public final class UserPreferences {

    public Rectangle windowBounds;
    public File previousOpenFolder;
    public boolean stretch;

    private UserPreferences() {
        node = Preferences.userNodeForPackage(this.getClass());
    }

    public static UserPreferences get() {
        if (INSTANCE == null) {
            INSTANCE = new UserPreferences();
        }
        INSTANCE.read();
        return INSTANCE;
    }

    public void read() {
        windowBounds = readWindowBounds();
        previousOpenFolder = new File(node.get("previousOpenFolder", "."));
        stretch = node.getBoolean("stretch", true);
    }

    public void write() {
        writeWindowBounds();
        node.put("previousOpenFolder", previousOpenFolder.getAbsolutePath());
        node.putBoolean("stretch", stretch);
        node.putBoolean("stretch", true); // TEMP TEMP TEMP!!!!
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
}
