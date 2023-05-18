/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
// SINGLETON class
public class Model {

    private Model() {
        prefs = UserPreferences.get();
        files = new ArrayList<File>();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public File next() {
        return next(1);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public File next(int numForward) {
        if (isEmpty())
            return null;

        int numFiles = files.size();

        while (--numForward >= 0)
            if (++currentImageNum >= numFiles) {
                currentImageNum = 0;
            }
        return files.get(currentImageNum);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public File prev(int numBack) {
        if (isEmpty())
            return null;

        int numFiles = files.size();

        while (--numBack >= 0)
            if (--currentImageNum < 0) {
                currentImageNum = numFiles - 1;
            }
        return files.get(currentImageNum);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public File curr() {
        if (currentImageNum < 0)
            return null;
        

        return files.get(currentImageNum);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void delete() {
        if (currentImageNum >= 0)
            files.remove(currentImageNum);
        
        // Important Edge Case:
        // say you have 3 pics p0, p1, p2.  If you remove p0 or p1 no problem.  currentImageNum will still be valid.
        // but if you delete p2 -- currentImageNum will be 2 -- which is invalid!
        
        if(currentImageNum >= files.size())
            currentImageNum = 0;
        
        // next edge case.  You just deleted the last picture
        if(files.size() <= 0)
            currentImageNum = -1;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static synchronized Model get() {
        if (INSTANCE == null) {
            INSTANCE = new Model();
        }
        return INSTANCE;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public int replace(List<File> files, boolean recursive) {

        if (recursive)
            Utils.debug("Recursive Add Files");

        clear();
        int numFiles = 0;

        for (File f : files) {
            numFiles += add(f.toPath(), recursive);
        }
        sort();
        Globals.view.imagesReplaced();
        Globals.controller.enableNavigationKeys();
        return numFiles;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //public int replace(File[] files) {
    //  return replace(Arrays.asList(files));
    //}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //  private below
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private int add(Path path, boolean recursive) {
        int numFilesAdded = 0;
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.walk(path, (recursive ? Integer.MAX_VALUE : 1))) {
                stream.forEach(p -> {
                    File f = p.toFile();
                    if (Utils.isArtFile(f))
                        files.add(f);
                });
            } catch (IOException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
            numFilesAdded = files.size();
        } else if (Files.isRegularFile(path)) {
            if (Utils.isArtFile(path)) {
                files.add(path.toFile());
                numFilesAdded = 1;
            }
        }
        return numFilesAdded;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void clear() {
        currentImageNum = -1;
        files.clear();
        prevSortType = null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isEmpty() {
        return files.size() <= 0;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    void sort() {
        Utils.debug("BEFORE SORT:");

        if (prevSortType != null)
            Utils.debug("PREFS OLD SORT TYPE: " + prevSortType + "   PREFS DIRECTION: " + (prevSortAscending ? "ascending" : "descending"));

        Utils.debug("PREFS NEW SORT TYPE: " + prefs.getSortType() + "   PREFS DIRECTION: " + (prefs.isSortAscending() ? "ascending" : "descending"));

        String sortType = prefs.getSortType();
        boolean sortAscending = prefs.isSortAscending();

        if (prevSortType != null && sortType.equals(prevSortType) && prevSortAscending == sortAscending) {
            Utils.debug("SORT TYPES DID NOT ACTUALLY CHANGE!");
            return;
        }

        // optimization -- I don't care about the sorting time.  It's the resetting to image #0 I want to avoid
        prevSortType = sortType;
        prevSortAscending = sortAscending;

        Utils.debug("BEFORE SORT");
        files.forEach(file -> {
            Utils.debug("      " + file.getName() + "   Date: " + file.lastModified() + "    SIZE: " + file.length());
        });

        switch (sortType) {
            case "Name" -> {
                if (sortAscending)
                    Collections.sort(files, (f1, f2) -> {
                        return f1.getName().compareToIgnoreCase(f2.getName());
                    });
                else
                    Collections.sort(files, (f1, f2) -> {
                        return f2.getName().compareToIgnoreCase(f1.getName());
                    });
            }
            case "Date" -> {
                if (sortAscending)
                    Collections.sort(files, (f1, f2) -> {
                        return Utils.compare(f1.lastModified(), f2.lastModified());
                    });
                else
                    Collections.sort(files, (f1, f2) -> {
                        return Utils.compare(f2.lastModified(), f1.lastModified());
                    });
            }
            case "Size" -> {
                if (sortAscending)
                    Collections.sort(files, (f1, f2) -> {
                        return Utils.compare(f1.length(), f2.length());
                    });
                else
                    Collections.sort(files, (f1, f2) -> {
                        return Utils.compare(f2.length(), f1.length());
                    });
            }
            case "Random" ->
                Collections.shuffle(files);
        }

        Utils.debug("AFTER SORT:");
        files.forEach(file -> {
            Utils.debug("      " + file.getName() + "   Date: " + file.lastModified() + "    SIZE: " + file.length());
        });

        // TODO resets to first image.  Caller needs to call view.next() -- or just leave it and the next call to next() or prev() will start at begin or end
        currentImageNum = -1;
        firstSort = false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Model INSTANCE = null;
    private ArrayList<File> files;
    private int currentImageNum = -1;
    private UserPreferences prefs;
    private boolean firstSort = true;
    private String prevSortType;
    private boolean prevSortAscending;

    int numImages() {
        return files.size();
    }
}
