/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.text;

import com.elf.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    private TreeMap<String, Integer> map = new TreeMap<>();
    private int totalWords = 0;
    
    private void add(String word) {
        ++totalWords;
        Integer i = map.get(word);
        
        if(i != null)
            ++i;
        else
            i = 1;
        map.put(word, i);
    }

    private void dump() {
        Set<Entry<String, Integer>> set = map.entrySet();
        
        int numWord = 0;
        int numLines = 0;
        for(Entry<String, Integer> entry : set) {
            System.out.printf("%6d %s  ", entry.getValue(), entry.getKey());
            if(++numWord > 6) {
                numWord = 0;
                System.out.println("");
                if(++numLines > 25)
                    break;
            }
        }
        System.out.println("\nTOTAL WORDS: " + totalWords);
        System.out.println("\nTOTAL UNIQUE WORDS: " + map.size());
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

    private static void doWords() throws FileNotFoundException, IOException {
        WordSmith ws = new WordSmith();
        File words = new File(System.getenv("DATA") + "/dickens.txt");
        FileReader fr = new FileReader(words);
        BufferedReader br = new BufferedReader(fr);
        //MaxPQ<String> pq = new MaxPQ<String>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] ww = StringUtils.toWordsLowercase(line);
            for (String s : ww) {
                    ws.add(s);
            }
        }
        
        fr.close();
        ws.dump();
    }

}
