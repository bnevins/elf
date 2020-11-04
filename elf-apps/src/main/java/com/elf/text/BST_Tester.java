package com.elf.text;

import static com.elf.stdlib.StdRandom.uniform;
import com.elf.util.sort.BST;
import com.elf.util.sort.PQ;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class BST_Tester {

    public BST_Tester() {
        tree = new BST<>();
    }
    private static final String wordcountName = System.getenv("DATA").replace('\\', '/')
            + "/dickens_words_count.txt";
    BST<String, Integer> tree;

    private class StringInt implements Comparable<StringInt>{

        private final String s;
        private final Integer i;

        StringInt(String s, Integer i) {
            this.s = s;
            this.i = i;
        }

        @Override
        public int compareTo(StringInt that) {
            return s.compareTo(that.s);
        }
    }

    class IntegerComp implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            if (o1.getValue() < o2.getValue()) {
                return -1;
            }
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            return 0;
        }
    }

    class StringComp implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }

    private void fillBT() {
        try {
            System.out.println(wordcountName);
            BufferedReader reader = new BufferedReader(new FileReader(wordcountName));
            ArrayList<StringInt> entries = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                StringInt entry = parse(line);
                entries.add(entry);
            }
            shuffle(entries);

            for (StringInt e : entries) {
                tree.put(e.s, e.i);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringInt parse(String line) {
        String[] ss = line.split(":");
        return new StringInt(ss[0], Integer.parseInt(ss[1]));
    }

    private static void shuffle(ArrayList<StringInt> list) {
        int n = list.size();
        for (int i = 0; i < n; i++) {
            int r = i + uniform(n - i);     // between i and n-1
            StringInt temp = list.get(i);
            list.set(i, list.get(r));
            list.set(r, temp);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world.");
        BST_Tester btt = new BST_Tester();
        btt.fillBT();
        System.out.println("SIZE: " + btt.tree.size());
        System.out.println("Height partially sorted input: " + btt.tree.height());
        if (btt.tree.get("narrower") == 14) {
            System.out.println("correct!!!");
        } else {
            System.out.println("WRONG!!");
        }
    }

}
