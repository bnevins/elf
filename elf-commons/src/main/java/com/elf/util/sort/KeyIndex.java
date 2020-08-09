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

    public void sort() {
        System.out.println("\n\n");
        int num = students.length;

        for (int i = 0; i < num; i++) {
            Student student = students[i];
            int key = student.getKey();
            count[key + 1]++;
            dump(student);
        }
    }

    private void makeCount(Student[] ss) {
        // assume sections 1->???
        int high = 0;
        for (Student s : ss) {
            int key = s.getKey();
            if (key > high) {
                high = key;
            }
        }
        if (high <= 0) {
            throw new RuntimeException("No Keys!!");
        }

        count = new int[high + 2];
        debug("Created count with " + count.length + " slots.");
    }

    private void dump(Student s) {
        System.out.print(s.getName() + "\t");
        for (int i = 1; i < count.length; i++) {
            System.out.print(count[i] + "  ");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        Student[] students = new Student[]{
            new Student("AAAA", 1),
            new Student("BBBB", 1),
            new Student("C", 2),
            new Student("D", 2),
            new Student("E", 2),
            new Student("F", 3),
            new Student("G", 3),
            new Student("H", 4),
            new Student("I", 4),
            new Student("J", 4),};

        KeyIndex ki = new KeyIndex(students);

        if (debug) {
            debug("XXXXXXXX STUDENTS XXXXXXXXXXX");
            
            for (Student s : ki.students) {
                debug(s.getName() + "\t" + s.getKey());
            }
            debug("XXXXXXXXXXXXXXXXX");
        }
        
        ki.sort();
    }

    private static void debug(String s) {
        if (debug) {
            System.out.println(s);
        }
    }
    Student[] students;
    int[] count;
    private static boolean debug = true;
}
