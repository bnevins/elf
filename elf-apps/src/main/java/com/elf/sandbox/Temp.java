/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sandbox;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.elf.util.StringUtils;

/**
 *
 * @author bnevns
 */
public class Temp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ds = "-0.1 199.378 2 -5 0.0000000000000001 \n1234";
        //String ds = "-0.1 199.378 2 -5 0.0000000000000001 , , ;,:";
        double[] dd = StringUtils.getDoubles(ds);

        if (dd != null) {
            for (double d : dd) {
                System.out.print(d + "  XXX  ");
            }
            System.out.println("");
        } else {
            System.out.println("Format Error");
        }
    }
    public static void main3(String[] args) {
    }

    public static void main2(String[] args) {

        try {
            InputStream is = System.in;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (String s = br.readLine(); s != null; s = br.readLine()) {
                System.out.println("XXX: " + s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Temp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param line a string with doubles seprated by spaces. e.g. "12.3 5.8
     * 0.00001"
     * @return a double array or null if line has anything other than numbers
     * (comment)
     */
    public static double[] getDoubles(String line) {
        try {
            String[] ss = line.split(" ");
            double[] dd = new double[ss.length];
            int index = 0;
            for (String s : ss) {
                dd[index++] = Double.parseDouble(s);
            }
            return dd;
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
            return null;
        }
    }
}
