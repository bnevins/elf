/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.io;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author bnevins
 */
public class FileCopier {


    public static void main(String[] args) throws IOException {
        // Get the source and destination directories
        Path sourceDirectory = Paths.get(args[0]);
        Path destinationDirectory = Paths.get(args[1]);

        // Check if the source directory exists
        if (!Files.exists(sourceDirectory)) {
            throw new IllegalArgumentException("The source directory does not exist");
        }

        // Check if the destination directory exists
        if (Files.exists(destinationDirectory)) {
            throw new IllegalArgumentException("The destination directory already exists");
        }

        // Recursively copy the files
        Files.walk(sourceDirectory)
                .forEach(source -> {
                    //var foo = 
                    Path p = source;
                    Path q = destinationDirectory.resolve(source.getFileName());
                    System.out.printf("Source: %s\tDest:  %s\n", p, q);
//                    try {
//                        Files.copy(source, destinationDirectory.resolve(source.getFileName()));
//                        System.out.println(source);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                });
    }
}