/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sandbox;

import java.awt.*;

public class ShowConfigurations {

    public static void main(String[] args) {
        GraphicsEnvironment ge
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        GraphicsConfiguration[] configurations = defaultScreen.
                getConfigurations();
        System.out.println("Default screen device: "
                + defaultScreen.getIDstring());
        for (int i = 0; i < configurations.length; i++) {
            System.out.println(" Configuration " + (i + 1));
            System.out.println(" " + configurations[i].getColorModel());
        }
        System.exit(0);
    }
}
