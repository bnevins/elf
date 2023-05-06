/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart.playground;

import javax.imageio.*;

/**
 *
 * @author bnevins
 */
public class ImageFileExtensions {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] extensions = ImageIO.getReaderFileSuffixes();
        for (String ext : extensions) {
            System.out.println(ext);
        }
    }       // TODO code application logic here
}
