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
public class Student {

    Student(String aName, int aSection) {
        name = aName;
        section = aSection;
    }

    int getKey() {
        return section;
    }
    String name;
    int section;
}
