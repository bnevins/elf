/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.practice;

import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author bnevins
 */
public class WordMutator {

    LinkedList<WordAndParent> queue;

    public static void main(String[] args) {
        WordMutator wm;

        if (args.length == 2) {
            wm = new WordMutator(args[0], args[1]);
        } else {
            wm = new WordMutator("above", "below");
        }
        
        wm.mutate();
    }
    private final String targetWord;
    private final String startingWord;
    private final int wordLength;
    private final static int CHAR_A_AS_INT = (int) "a".charAt(0);
    private final static int CHAR_Z_AS_INT = CHAR_A_AS_INT + 26;
    private static boolean verbose = false;
    private boolean success = false;

    public WordMutator(String from, String to) {
        this.startingWord = from;
        this.targetWord = to;
        queue = new LinkedList<>();
        wordLength = from.length();
        verify();
        queue.add(new WordAndParent(startingWord, null));
    }

    public void setVerbose() {
        verbose = true;
    }

    public void mutate() {
        while (!queue.isEmpty()) {
            mutateOne();
        }

        String out = dumpPath();

        if (out == null) {
            if (verbose) {
                System.out.println("Impossible to transform " + startingWord + " to " + targetWord);
            }
        } else {
            System.out.println(out);
            success = true;
        }
    }

    private void verify() {
        if (wordLength <= 0) {
            throw new RuntimeException("empty string input!");
        }
        if (wordLength != targetWord.length()) {
            throw new RuntimeException("lengths don't match!");
        }
        if (!Words.isWord(targetWord)) {
            throw new RuntimeException("ZZZ " + targetWord + " is not a word!");
        }

        // set the parent to null !!!
        if (!Words.isNewWord(startingWord, null)) {
            throw new RuntimeException(targetWord + " is not a word! ==> the parent is " + Words.getParent(startingWord));
        }
    }

    private void mutateOne() {
        WordAndParent pair = queue.remove();

        for (int characterPosition = 0; characterPosition < wordLength; characterPosition++) {
            char[] chars = pair.word.toCharArray();
            int curr = (int) chars[characterPosition];

            for (int j = CHAR_A_AS_INT; j < CHAR_Z_AS_INT; j++) {
                if (j == curr) {
                    continue;
                }

                chars[characterPosition] = (char) j;
                String newWord = new String(chars);

                if (newWord.equals(targetWord)) {
                    // force the parent to get set!!
                    Words.isNewWord(newWord, pair.word);
                    queue.clear();
                } else if (Words.isNewWord(newWord, pair.word)) {
                    WordAndParent wap = new WordAndParent(newWord, pair.word);
                    queue.add(wap);
                }
            }
        }
    }

    private String dumpPath() {
        String parent = Words.getParent(targetWord);

        if (parent == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new Stack<>();
        stack.push(targetWord);
        stack.push(parent);

        while ((parent = Words.getParent(parent)) != null) {
            stack.push(parent);

            if (parent.equals(startingWord)) {
                break;
            }
        }

        sb.append(stack.pop());
        while (!stack.isEmpty()) {
            sb.append(" -> ");
            sb.append(stack.pop());
        }

        return sb.toString();
    }

    boolean wasSuccessful() {
        return success;
    }

    private static class WordAndParent {

        String word;
        String parent;

        public WordAndParent(String word, String parent) {
            this.word = word;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "Word: " + word + "  Parent: " + parent;
        }
    }
}
