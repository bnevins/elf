/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author bnevins
 */
// SINGLETON class
public class ArtLib {

    public File next() {
        int num = files.size();
        
        if(num <= 0)
            return null;
        
        if(++currentImage >= num) {
            currentImage = 0;
        }
        return files.get(currentImage);
    }
    
    public static synchronized ArtLib get() {
        if (INSTANCE == null) {
            INSTANCE = new ArtLib();
        }
        return INSTANCE;
    }

    public int add(Path path) {
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                stream.forEach(p -> {
                    File f = p.toFile();
                    if(Utils.isArtFile(f))
                        files.add(f);
                });
            } catch (IOException ex) {
                Logger.getLogger(ArtLib.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return files.size();
    }
    public int replace(Path path) {
        clear();
        int numFiles = add(path);
        Globals.view.imagesReplaced();
        return numFiles;
    }

    private ArtLib() {
        files = new ArrayList<File>();
    }
    private void clear() {
        currentImage = -1;
        files.clear();
    }
    private static ArtLib INSTANCE = null;
    private ArrayList<File> files;
    private int currentImage = -1;
}
