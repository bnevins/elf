/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import javax.swing.Timer;
/**
 *
 * @author bnevins
 */
public class SlideShow implements ActionListener{
    private Timer timer;

    public SlideShow() {
        // todo speed from utils?
        timer = new Timer(UserPreferences.get().getSlideshowSeconds() * 1000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Globals.view.nextImage();
    }

    void stop() {
        timer.stop();
    }
}
