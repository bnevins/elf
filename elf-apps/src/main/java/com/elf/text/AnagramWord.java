/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.text;

/**
 *
 * @author bnevins
 */
public class AnagramWord implements Comparable<AnagramWord> {
        String index;
        String word;

        AnagramWord(String aWord, String anIndex) {
            word = aWord;
            index = anIndex;
        }

        @Override
        public int compareTo(AnagramWord other) {
            return index.compareTo(other.index);
        }
        @Override
        public String toString() {
            return word + " --> " + index;
        }
    }
