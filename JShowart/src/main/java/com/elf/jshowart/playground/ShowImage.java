/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.JShowart.playground;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ShowImage extends Panel {

    BufferedImage image;
    //String imageName = "Q:\\P\\_stills\\_sbest\\0007.jpg";
    String imageName = "C:/Users/bnevins/Desktop/wallpaper/HeartB_Jensen_3925.jpg";
    
    public ShowImage() {
        try {
            System.out.println("Enter image name\n");
            //BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            //String imageName = "xyz";
            File input = new File(imageName);
            image = ImageIO.read(input);
        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }
    }
    @Override
    public void paint(Graphics g) {
        int h = image.getHeight();
        int w = image.getWidth();
        
        //g.drawImage(image, 0, 0, null);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, w, h, null);
    }

    static public void main(String args[]) throws
            Exception {
        JFrame frame = new JFrame("Display image");
        Panel panel = new ShowImage();
        frame.getContentPane().add(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
/***
CRect ShowartUtils::FitToWindow(const CSize window, CSize dib, BOOL stretch)
{
	if((dib.cx != window.cx || dib.cy != window.cy)  && stretch)
	{
		double xRatio = ((double)window.cx) / ((double)dib.cx);
		double yRatio = ((double)window.cy) / ((double)dib.cy);

		double ratio = xRatio <= yRatio ? xRatio : yRatio;
		
		dib = CSize((int)(((double)dib.cx) * ratio), (int)(((double)dib.cy) * ratio));
	}

	CPoint			origin(0,0);
	
	if(dib.cx < window.cx)
		origin.x = (window.cx - dib.cx) / 2;

	if(dib.cy < window.cy)
		origin.y = (window.cy - dib.cy) / 2;
	
	return CRect(origin, dib);
}
 * 
 */
