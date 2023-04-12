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
        if (isEmpty())
            return null;

        int num = files.size();

        if (++currentImageNum >= num) {
            currentImageNum = 0;
        }
        return files.get(currentImageNum);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public File prev() {
        if (isEmpty())
            return null;

        int num = files.size();

        if (--currentImageNum < 0) {
            currentImageNum = num - 1;
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
    public static synchronized Model get() {
        if (INSTANCE == null) {
            INSTANCE = new Model();
        }
        return INSTANCE;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public int replace(File[] files) {
        clear();
        int numFiles = 0;

        for (File f : files) {
            numFiles += add(f.toPath());
        }
        sort();
        Globals.view.imagesReplaced();
        return numFiles;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //  private below
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private int add(Path path) {
        int numFilesAdded = 0;
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
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
// TODO get sort types from prefs        
// TODO get sort types from prefs        
// TODO get sort types from prefs        
// TODO get sort types from prefs        
// TODO get sort types from prefs        
        System.out.println("BEFORE SORT:");

        if (prevSortType != null)
            System.out.println("PREFS OLD SORT TYPE: " + prevSortType + "   PREFS DIRECTION: " + (prevSortAscending ? "ascending" : "descending"));

        System.out.println("PREFS NEW SORT TYPE: " + prefs.getSortType() + "   PREFS DIRECTION: " + (prefs.isSortAscending() ? "ascending" : "descending"));

        String sortType = prefs.getSortType();
        boolean sortAscending = prefs.isSortAscending();

        if (prevSortType != null && sortType.equals(prevSortType) && prevSortAscending == sortAscending) {
            System.out.println("SORT TYPES DID NOT ACTUALLY CHANGE!");
            return;
        }

        // optimization -- I don't care about the sorting time.  It's the resetting to image #0 I want to avoid
        prevSortType = sortType;
        prevSortAscending = sortAscending;

        System.out.println("BEFORE SORT");
        files.forEach(file -> {
            System.out.println("      " + file.getName() + "   Date: " + file.lastModified() + "    SIZE: " + file.length());
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
                        return Utils.compare(f2.lastModified(), f1.lastModified());
                    });
            }
                case "Random" -> Collections.shuffle(files);
        }

        System.out.println("AFTER SORT:");
        files.forEach(file -> {
            System.out.println("      " + file.getName() + "   Date: " + file.lastModified() + "    SIZE: " + file.length());
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
