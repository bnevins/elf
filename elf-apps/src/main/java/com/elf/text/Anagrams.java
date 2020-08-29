/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.text;

import com.elf.algorithms.stdlib.In;
import com.elf.algorithms.stdlib.StdIn;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevns
 */
public class Anagrams {

    AnagramWord[] words;

    public Anagrams() {
        FileReader fr = null;
        ArrayList<AnagramWord> wordsList = new ArrayList<AnagramWord>();
        try {
            String path = System.getProperty("DATA");
            if (path == null) {
                path = "C:/dev/elf/data";
            }
            path += "/words.txt";
            File file = new File(path);    //creates a new file instance  
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String index = insertionSort(line);
                AnagramWord word = new AnagramWord(line, index);
                wordsList.add(word);
            }
            fr.close();
            words = new AnagramWord[wordsList.size()];
            words = wordsList.toArray(words);
            MergeSortComparable sorter = new MergeSortComparable(words);
            sorter.sort();
        } catch (Exception ex) {
            Logger.getLogger(Anagrams.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Anagrams.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final List<List<AnagramWord>> getAllAnagrams() {
        List<List<AnagramWord>> listOfLists = new ArrayList<>();

        for (int i = 0; i < words.length - 1; i++) {
            List<AnagramWord> list = getAnagrams(i);
            if (list != null) {
                listOfLists.add(list);
                i += (list.size() - 1); // -1 because for-loop will increment
            }
        }
        return listOfLists;
    }

    public List<AnagramWord> getAnagrams(int start) {
        if (start + 1 >= words.length) {
            return null;
        }
        if (words[start].compareTo(words[start + 1]) != 0) {
            return null;
        }
        // got one!
        List<AnagramWord> list = new ArrayList<>();
        list.add(words[start]);
        list.add(words[start + 1]);

        for (int i = start + 2; i < words.length - 1; i++) {
            if (words[start].compareTo(words[i]) == 0) {
                list.add(words[i]);
            } else {
                break;
            }
        }
        return list;
    }

    private String insertionSort(String word) {
        char[] cword = word.toCharArray();

        int n = cword.length;
        for (int i = 1; i < n; i++) {
            char key = cword[i];
            int j = i - 1;
            while ((j > -1) && (cword[j] > key)) {
                cword[j + 1] = cword[j];
                j--;
            }
            cword[j + 1] = key;
        }
        return new String(cword);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Anagrams anagrams = new Anagrams();
        AnagramWord[] words = anagrams.words;
        String word;
        String query = "Enter word (X to exit): ";
        System.out.print(query);
        List<List<AnagramWord>> all = anagrams.getAllAnagrams();
        System.out.println("Number of anagram words: " + all.size());

        for (List<AnagramWord> list : all) {
            if (list.size() < 0) {
                continue;
            }
            for (AnagramWord w : list) {
                System.out.println(w);
            }
        }
        System.out.flush();
        int max = -1;
        List<AnagramWord> maxList = null;

        for (Iterator<List<AnagramWord>> iter = all.iterator(); iter.hasNext();) {
            List<AnagramWord> list = iter.next();
            if (list.size() > max) {
                max = list.size();
                maxList = list;
            }
        }
        System.out.println("Max anagrams = " + max);
        for (AnagramWord w : maxList) {
            System.out.println(w);
        }

        if (false) {
            while (!(word = StdIn.readString()).equalsIgnoreCase("X")) {
                String index = anagrams.insertionSort(word);
                AnagramWord aw = new AnagramWord(word, index);
                //System.out.println(aw);
                for (AnagramWord a : words) {
                    if (a.compareTo(aw) == 0) {
                        System.out.println(a);
                    }
                }
                System.out.print("\n" + query);
            }
        }
    }
}
