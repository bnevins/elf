/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

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
                    if(Utils.isArtFile(p))
                        files.add(p);
                });
            } catch (IOException ex) {
                Logger.getLogger(ArtLib.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return files.size();
    }
    public int replace(Path path) {
        files.clear();
        return add(path);
    }

    private ArtLib() {
        files = new ArrayList<Path>();
    }

    private static ArtLib INSTANCE = null;
    private ArrayList<Path> files;
}
