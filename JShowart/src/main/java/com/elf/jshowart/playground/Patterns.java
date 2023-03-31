/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart.playground;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bnevins
 */
public class Patterns {

    /**
     * @param args the command line arguments
     */
    
    static String testnames[] = {
    
        "  foo.jpg",
        "foo.jpg",
        "foo.bmp",
        "foo.gif",
        "foo.tif",
        "foo.png",
        "foo.jpg ",
        "foo.jpeg",
        "foo.jpgx",
        "foo bar.jpg",
        ".jpg",
        "foo.JPG",
        
        
    
    
    };
    public static void main(String[] args) {
        //String regex = "*\\.{bmp,gif,tif,png,jpg,jpeg}";
        //String regex = "([^\\s]+(\\.(?i)(bmp|gif|tif|png|jpg|jpeg))$)";
        String regex = "(.+\\.(bmp|gif|tif|png|jpg|jpeg)$)";
        Pattern artFilePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        
        for(String s : testnames) {
            Matcher matcher = artFilePattern.matcher(s);
            System.out.println("\"" + s + "\"" + "  ->  " + matcher.matches());
        }
    }

}
