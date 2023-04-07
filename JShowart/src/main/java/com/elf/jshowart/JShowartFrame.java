/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author bnevins
 */
public class JShowartFrame extends JFrame implements KeyListener {

    private final UserPreferences prefs;
    private JShowartView view;
    private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private boolean currentFullScreen = false;

    // keep it alive because it can be S-L-O-W to start

    /**
     * Creates new form JShowartFrame
     */
    public JShowartFrame() {
        prefs = UserPreferences.get();
        initComponents();
        MenuFitToWindow.setSelected(prefs.fitToWindow);
        setBounds(prefs.windowBounds);
        addKeyListener(Globals.view);
        addKeyListener(this);
        Globals.frame = this;
        
    }

    public void enableSaveImages(boolean enable) {
        MenuSave.setEnabled(enable);
        MenuSaveAs.setEnabled(enable);
    }

    public void toggleFullScreen() {
        System.out.println("TOGGLE FULL SCREEN = " + currentFullScreen);
        currentFullScreen = !currentFullScreen;

        if (currentFullScreen) {
            // TODO remove menubar
            setVisible(false);
            dispose();
            setUndecorated(true);
            setResizable(false);
            //setExtendedState(JFrame.MAXIMIZED_BOTH); 
            graphicsDevice.setFullScreenWindow(this);
            setAlwaysOnTop(true);
            setVisible(true);
        } else {
            graphicsDevice.setFullScreenWindow(null);
            dispose();
            setUndecorated(false);
            setResizable(true);
            setExtendedState(JFrame.NORMAL);
            setBounds(prefs.windowBounds);
            setAlwaysOnTop(false);
            setVisible(true);
        }
    }

    /**
     * *
     * addWindowListener WindowAdapter
     *
     * @Override protected void processWindowEvent(WindowEvent e) { if (e.getID() == WindowEvent.WINDOW_DEACTIVATED) { // windowState is set in my set full
     * screen code if (windowState == WindowState.FULL_SCREEN) { return; } } * super.processWindowEvent(e); }
     */
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu7 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuFile = new javax.swing.JMenu();
        MenuOpenFiles = new javax.swing.JMenuItem();
        MenuSave = new javax.swing.JMenuItem();
        MenuSaveAs = new javax.swing.JMenuItem();
        MenuEdit = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        MenuFitToWindow = new javax.swing.JCheckBoxMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();

        jMenu7.setText("jMenu7");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JShowArt");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWindowClosing(evt);
            }
        });

        MenuFile.setText("File");

        MenuOpenFiles.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuOpenFiles.setText("Open Files and Folders");
        MenuOpenFiles.setToolTipText("Open Image(s)");
        MenuOpenFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFilesActionPerformed(evt);
            }
        });
        MenuFile.add(MenuOpenFiles);

        MenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuSave.setText("Save");
        MenuSave.setToolTipText("Save Image");
        MenuSave.setEnabled(false);
        MenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveActionPerformed(evt);
            }
        });
        MenuFile.add(MenuSave);

        MenuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuSaveAs.setText("Save As...");
        MenuSaveAs.setToolTipText("Save Image As...");
        MenuSaveAs.setEnabled(false);
        MenuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveAsActionPerformed(evt);
            }
        });
        MenuFile.add(MenuSaveAs);

        jMenuBar1.add(MenuFile);

        MenuEdit.setText("Edit");
        jMenuBar1.add(MenuEdit);

        jMenu3.setText("View");

        MenuFitToWindow.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuFitToWindow.setSelected(true);
        MenuFitToWindow.setText("Fit To Window");
        MenuFitToWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFitToWindowActionPerformed(evt);
            }
        });
        jMenu3.add(MenuFitToWindow);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Utilities");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");

        about.setText("About JShowArt...");
        about.setToolTipText("");
        about.setActionCommand("");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        jMenu5.add(about);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        AboutDialog about = new AboutDialog(this, true);
        about.setVisible(true);
    }//GEN-LAST:event_aboutActionPerformed

    private void MenuOpenFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFilesActionPerformed
        Globals.chooser.setCurrentDirectory(prefs.previousOpenFileParent);
        int returnVal = Globals.chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = Globals.chooser.getSelectedFiles();
            int numFilesAdded = ArtLib.get().replace(files);

            if (numFilesAdded <= 0)
                enableSaveImages(false);
            else {
                enableSaveImages(true);
                prefs.previousOpenFileParent = Globals.chooser.getCurrentDirectory();
            }
        }
    }//GEN-LAST:event_MenuOpenFilesActionPerformed

    private void MenuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveAsActionPerformed
        assert (view != null);
        view.saveAs();

    }//GEN-LAST:event_MenuSaveAsActionPerformed

    private void onWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowClosing
        prefs.windowBounds = getBounds();
        prefs.write();
    }//GEN-LAST:event_onWindowClosing

    private void MenuFitToWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFitToWindowActionPerformed
        System.out.println("FitToWindow: isSelected: " + MenuFitToWindow.isSelected() + "   " + evt);
        prefs.fitToWindow = MenuFitToWindow.isSelected();
        //revalidate();
        //view.invalidate();
        //view.repaint();
        repaint();
    }//GEN-LAST:event_MenuFitToWindowActionPerformed

    private void MenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveActionPerformed
        // TODO add your handling code here:
        assert (view != null);
        view.save();
    }//GEN-LAST:event_MenuSaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JShowartFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenuEdit;
    private javax.swing.JMenu MenuFile;
    private javax.swing.JCheckBoxMenuItem MenuFitToWindow;
    private javax.swing.JMenuItem MenuOpenFiles;
    private javax.swing.JMenuItem MenuSave;
    private javax.swing.JMenuItem MenuSaveAs;
    private javax.swing.JMenuItem about;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables

    void setView(JShowartView view) {
        this.view = view;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
            case KeyEvent.VK_ESCAPE ->
                toggleFullScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
