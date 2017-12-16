/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.io;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class FieldFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            
            String s = null;
            while((s = br.readLine()) != null) {
                int i = s.indexOf(',');
                System.out.println(s.substring(0, i));
            }
        }
        catch (IOException ex) {
            Logger.getLogger(FieldFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         
        
    
    
    
    }

}
