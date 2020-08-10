/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class KeyIndex {

    /**
     * @param args the command line arguments
     */
    KeyIndex(Student[] theStudents) {
        students = theStudents;
        makeCount(theStudents);
    }

    public void makeKeyIndex() {
        System.out.println("\n\n");
        int num = students.length;

        for (int i = 0; i < num; i++) {
            Student student = students[i];
            int key = student.getKey();
            count[key + 1]++;
            dump(student);
        }
    }

    public void makeIndices() {
        debug("Make Indices");

        for (int i = 0; i < numKeys; i++) {
            count[i + 1] += count[i];
            dump(count);
        }
    }

    public void sort() {
        dump(students);
        Student[] aux = new Student[students.length];
        debug("***** SORT *****");
        for (int i = 0; i < students.length; i++) {
            dump(count);
            int location = count[students[i].getKey()]++;
            aux[location] = students[i];
        }
        for (int i = 0; i < students.length; i++) {
            students[i] = aux[i];
        }

        dump(students);
    }

    private void makeCount(Student[] ss) {
        // assume sections 1->???
        for (Student s : ss) {
            int key = s.getKey();
            if (key > numKeys) {
                numKeys = key;
            }
        }
        if (numKeys <= 0) {
            throw new RuntimeException("No Keys!!");
        }

        count = new int[numKeys + 2];
        debug("Created count with " + count.length + " slots.");
    }

    private void dump(Student[] ss) {
        if (debug) {
            debug("XXXXXXXX STUDENTS XXXXXXXXXXX");

            for (Student s : ss) {
                debug(s.getName() + "\t" + s.getKey());
            }
            debug("XXXXXXXXXXXXXXXXX");
        }

    }

    private void dump(Student s) {
        System.out.print(s.getName() + "\t");
        for (int i = 1; i < count.length; i++) {
            System.out.print(count[i] + "  ");
        }
        System.out.println("");
    }

    private void dump(int[] c) {
        for (int i = 0; i < c.length; i++) {
            System.out.print(c[i] + "  ");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        Student[] students = new Student[]{
            new Student("AAAA", 1),
            new Student("BBBB", 3),
            new Student("C", 2),
            new Student("D", 2),
            new Student("E", 1),
            new Student("F", 3),
            new Student("G", 4),
            new Student("H", 4),
            new Student("I", 2),
            new Student("J", 4),
            new Student("K", 1),
            new Student("L", 2),
            new Student("M", 4),
            new Student("N", 5),
            new Student("O", 5),
            new Student("P", 3),
            new Student("Q", 6),
            new Student("R", 4),
            new Student("S", 4),
            new Student("T", 2),
            new Student("U", 1),
            new Student("V", 1),
            new Student("W", 3),
            new Student("X", 5),
            new Student("Y", 6)};

        KeyIndex ki = new KeyIndex(students);
        
        ki.makeKeyIndex();
        ki.makeIndices();
        ki.sort();
    }

    private static void debug(String s) {
        if (debug) {
            System.out.println(s);
        }
    }
    private Student[] students;
    private int[] count;
    private static boolean debug = true;
    private int numKeys = 0;

}
