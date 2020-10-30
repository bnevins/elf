package com.elf.raspberry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLister {
    public static List<Path> getFiles(String dirName) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            return paths
                .filter(f -> f.toString().toLowerCase().endsWith(".jpg"))
                .collect(Collectors.toList());
        }
    }
    public static void main(String[] args) {
try {
        List<Path> list = getFiles(args[0]);
        System.out.println(list.size() + " entries found");
        System.out.println(list.get(200));
}
catch(IOException e) {
System.out.println(e);}
}
}
