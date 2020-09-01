/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.text;

import com.elf.algorithms.stdlib.Stopwatch;
import com.elf.util.StringUtils;
import com.elf.util.sort.PQ;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevns
 */
public class WordSmith {

    private static double elapsedTimeWordSort;
    private static double elapsedTimeFrequencySort;
    private PQ<Map.Entry<String, Integer>> pq;
    private TreeMap<String, Integer> map = new TreeMap<>();
    private int totalWords = 0;

    private void add(String word) {
        ++totalWords;
        Integer i = map.get(word);

        if (i != null) {
            ++i;
        } else {
            i = 1;
        }
        map.put(word, i);
    }

    private static void doWords() throws FileNotFoundException, IOException {
        WordSmith ws = new WordSmith();
        File words = new File(System.getenv("DATA") + "/dickens.txt");
        FileReader fr = new FileReader(words);
        BufferedReader br = new BufferedReader(fr);
        //MaxPQ<String> pq = new PQ<String>();
        String line;
        Stopwatch timer = new Stopwatch();
        while ((line = br.readLine()) != null) {
            String[] ww = StringUtils.toWordsLowercase(line);
            for (String s : ww) {
                ws.add(s);
            }
        }
        elapsedTimeWordSort = timer.elapsedTime();
        fr.close();
        ws.sortByCount();
        ws.dump();
    }

    public void sortByCount() {
        Stopwatch timer = new Stopwatch();
        pq = new PQ<>(totalWords, new IntegerComp());
        //q
        pq.setMinimum();
        Set<Entry<String, Integer>> set = map.entrySet();

        for (Entry<String, Integer> entry : set) {
            pq.insert(entry);
        }
        elapsedTimeFrequencySort = timer.elapsedTime();
    }

    private void dump() {
        Set<Entry<String, Integer>> set = map.entrySet();

        int numWord = 0;
        int numLines = 0;
        for (Entry<String, Integer> entry : set) {
            System.out.printf("%6d %s  ", entry.getValue(), entry.getKey());
            if (++numWord > 6) {
                numWord = 0;
                System.out.println("");
                if (++numLines > 25) {
                    break;
                }
            }
        }
        for (int i = 0; i < 100; i++) {
            Entry<String, Integer> entry = pq.delMax();
            System.out.println(entry.getKey() + "==>" + entry.getValue());
        }
        System.out.println("\nElapsed Time Word Sort: " + elapsedTimeWordSort + " seconds");
        System.out.println("\nElapsed Time Frequency Sort: " + elapsedTimeFrequencySort + " seconds");
        System.out.println("TOTAL WORDS: " + totalWords);
        System.out.println("TOTAL UNIQUE WORDS: " + map.size());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            doWords();
        } catch (IOException ex) {
            Logger.getLogger(WordSmith.class.getName()).log(Level.SEVERE, null, ex);
        }
//        String s = "It. was.the  ?!, can't";
//        String[] ss = s.split("\\W+");
//        for (String str : ss) {
//            System.out.println((str == null) + ":" + str.length() + ":" + str);
//        }

    }

    class IntegerComp implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            if (o1.getValue() < o2.getValue()) {
                return -1;
            }
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            return 0;
        }
    }
}
