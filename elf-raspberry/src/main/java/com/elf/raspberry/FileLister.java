package com.elf.raspberry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author bnevins
 */
public class FileLister {

    public static void main(String[] args) throws IOException {

        String dirName = "e:/working/baybridge";

        List<String> result;
        List<Path> result2;

        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            result2 = paths.filter(f -> f.toString().toUpperCase().endsWith(".JPG")).collect(Collectors.toList());
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(result2.get(i));
            System.out.println(result2.get(i).getFileName());
        }
//        for(String s : result)
//            System.out.println("xxxx " + s);
    }
}
