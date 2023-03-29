/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.JShowart;

/**
 *
 * @author bnevins
 */
public class CodeDump {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
/***
 * public class JShowartApp {

    Frame mainFrame;
    private static final Color[] COLORS = new Color[]{
        Color.red, Color.blue
    };

    public JShowartApp(int numBuffers, GraphicsDevice gd) {

        try {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            gd.setFullScreenWindow(mainFrame);
            //if (gd.isDisplayChangeSupported()) {
            //  chooseBestDisplayMode(gd);
            //}
            Rectangle bounds = mainFrame.getBounds();
            mainFrame.createBufferStrategy(numBuffers);
            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();
            for (float lag = 2000.0f; lag > 0.00000006f; lag = lag / 1.33f) {
                for (int i = 0; i < numBuffers; i++) {

                    Graphics g = bufferStrategy.getDrawGraphics();
                    if (!bufferStrategy.contentsLost()) {
                        g.setColor(COLORS[i]);
                        g.fillRect(0, 0, bounds.width, bounds.height);
                        bufferStrategy.show();
                        g.dispose();
                    }
                    try {
                        Thread.sleep((int) lag);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            gd.setFullScreenWindow(null);
        }

    }

    public static void main(String args[]) {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();
            int numBuffers = 2;
            JShowartApp test = new JShowartApp(numBuffers, device);
        } catch (HeadlessException e) {
        }
        System.exit(0);
    }
 */