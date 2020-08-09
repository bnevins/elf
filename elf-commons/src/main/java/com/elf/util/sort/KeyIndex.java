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
    public static void main(String[] args) {
        KeyIndex ki = new KeyIndex();
        for (Student s : ki.students) {
            System.out.println(s.name + "\t" + s.section);
        }
        System.out.println("\n\n");
        int num = ki.students.length;
        // prepare count
        for (int i = 0; i < num; i++) {
            Student student = ki.students[i];
            int key = student.getKey();
            //System.out.println("KEY: " + key);
            //System.out.println("number of count in array: " + ki.count.length);
            ki.count[key]++;
            //System.out.println("COUNT  " + ki.count[key]);
            ki.dump(student);

        }

    }

    KeyIndex() {
        students = new Student[]{
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

        count = new int[5];

    }

    void dump(Student s) {
        //for (Student s : students) {
        System.out.print(s.name + "\t");
        for (int i = 1; i < count.length; i++) {
            System.out.print(count[i] + "  ");
        }
        System.out.println("");
        //}
    }
    Student[] students;
    int[] count;
}
