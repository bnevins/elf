/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.practice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public final class Words {

    private static Map<String, String> words = new HashMap<>();
    private static String WORDS_FILE_PATH;

    static public List<String> getSubset(int numChars) {

        List<String> subset = new ArrayList<>();

        for(String word : words.keySet()) {
            if(word.length() == numChars)
                subset.add(word);
        }
        return subset;
    }

    public static void clear() {
        for (Map.Entry<String, String> entry : words.entrySet()) {
            entry.setValue(null);
        }
    }

    private Words() {
    }

    public final static boolean isWord(String word) {
        return words.containsKey(word);
    }

    public final static boolean isNewWord(String word, String parent) {

        if (!isWord(word)) {
            return false;
        }

        return words.putIfAbsent(word, parent) == null;
    }

    public static String getParent(String word) {
        return words.get(word);
    }

    private static void read() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(WORDS_FILE_PATH))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                words.put(line, null);
            }
            if (count != 354987) {
                throw new RuntimeException("Word file is supposed to be 354987 lines long!!");
            }
        }
    }

    static {
        String osname = System.getProperty("os.name");

        if (osname.startsWith("Mac")) {
            WORDS_FILE_PATH = "/Users/wnevins/google/words.txt";
        } else {
            WORDS_FILE_PATH = "c:/google/words.txt";
        }
        try {
            long begin = System.currentTimeMillis();
            read();
            long end = System.currentTimeMillis();
            System.out.println("******   " + (end - begin) + " msec to read in 350,000 words");
        } catch (IOException ex) {
            System.out.println("ERROR READING WORDS FILE!!!");
            System.exit(1);
        }
    }
}
