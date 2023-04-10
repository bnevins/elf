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
        
        for(File f : files) {
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
        System.out.println("PREFS SORT TYPE: " + prefs.getSortType() + "   PREFS DIRECTION: " + (prefs.isSortAscending() ? "ascending" : "descending"));
       
        files.forEach(file -> {
            System.out.println(file.getName());
            });
        
        Collections.sort(files, (f1, f2) -> {
            String fname1 = f1.getName();
            String fname2 = f2.getName();
            return fname1.compareToIgnoreCase(fname2);});
        
        System.out.println("AFTER SORT:");
        files.forEach(file -> {
            System.out.println(file.getName());
            });
        
       // TODO resets to first image.  Caller needs to call view.next() -- or just leave it and the next call to next() or prev() will start at begin or end
        currentImageNum = -1;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Model INSTANCE = null;
    private ArrayList<File> files;
    private int currentImageNum = -1;
    private UserPreferences prefs;
    void setSortAscending(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
