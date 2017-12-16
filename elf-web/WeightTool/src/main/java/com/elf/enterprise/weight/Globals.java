/*
 * Globals.java
 *
 * Created on October 27, 2007, 12:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.enterprise.weight;

/**
 *
 * @author bnevins
 */
public class Globals {

    private Globals() {
    }
    public static final String WEIGHT_TABLE_NAME = "WEIGHT_ITEMS";
    private static final String[] COLORS = new String[]{
        // from good to bad
        "#00FF00",
        "#00DD00",
        "#00BB00",
        "#008800",
        "#FFFF00",
        "#FFCC00",
        "#FF8800",
        "#FF4400",
        "#FF0000",
        "#BB0000",
        "#770000",
        "#330000",};

    public static String getColor(double dwt, double dstartWt, double dincrement) {
        int wt = (int) dwt;
        int startWt = (int) dstartWt;
        int inc = (int) dincrement;

        if (wt <= startWt)
            return COLORS[0];

        if (wt >= startWt + inc * COLORS.length)
            return COLORS[COLORS.length - 1];

        return COLORS[(wt - startWt) / inc];
    }
    public static void main(String[] notUsed) {
        for(double wt = 169.0; wt < 250.0; wt += 1.0) {
            System.out.printf("%f ==> %s\n", wt, getColor(wt, 170.0, 5.0));
        }
    }

    public static final String JDBC_RESOURCE_NAME = "jdbc/bnevins";
}
