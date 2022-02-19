/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class FileRenamerSpace {

    public static void main(String[] args) {
        if (args.length < 1) {
            //TODO Arg Processor
            System.out.println("Usage FileRenamerSpace dir");
            System.out.println("Change all spaces in names to underscores.  Very simple!!");
            System.exit(1);
        }
        FileRenamerSpace frs = new FileRenamerSpace(args[0]);
    }

    public FileRenamerSpace(String dirname) {
        File dir = new File(dirname);
        System.out.println(dir.getAbsolutePath());
        
        
        
        System.out.println(dirname);
    }
    
}
