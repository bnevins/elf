/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author bnevins
 */
public class JShowartFrame extends javax.swing.JFrame{

    private final UserPreferences prefs;
    private JShowartView view;
    private final FileNameExtensionFilter filter;

    /**
     * Creates new form JShowartFrame
     */
    public JShowartFrame() {
        prefs = UserPreferences.get();
        filter = new FileNameExtensionFilter(
            "JPG, BMP, PNG, GIF and TIFF Images",
            "jpg", "jpeg", "bmp", "png", "gif", "tif");
        initComponents();
        MenuFitToWindow.setSelected(prefs.fitToWindow);
        setBounds(prefs.windowBounds);
        addKeyListener(Globals.view);
        Globals.frame = this;
        //LayoutManager lm = getContentPane().getLayout();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
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
        MenuOpenFolders = new javax.swing.JMenuItem();
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
        MenuOpenFiles.setText("Open File(s)...");
        MenuOpenFiles.setToolTipText("Open Image(s)");
        MenuOpenFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFilesActionPerformed(evt);
            }
        });
        MenuFile.add(MenuOpenFiles);

        MenuOpenFolders.setText("Open Folder(s)...");
        MenuOpenFolders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFoldersActionPerformed(evt);
            }
        });
        MenuFile.add(MenuOpenFolders);

        MenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuSave.setText("Save");
        MenuSave.setToolTipText("Save Image");
        MenuFile.add(MenuSave);

        MenuSaveAs.setText("Save As...");
        MenuSaveAs.setToolTipText("Save Image As...");
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
        JFileChooser chooser = new JFileChooser(prefs.previousOpenFileParent);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            System.out.println("You chose to open this file: " + f.getName());
            //view.setImage(f);
            prefs.previousOpenFileParent = f.getParentFile();
            System.out.println("DEBUG XYZ:  " + f.getPath());
            System.out.println("DEBUG XYZZZZ:  " + f.getAbsolutePath());
        }
    }//GEN-LAST:event_MenuOpenFilesActionPerformed

    private void MenuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveAsActionPerformed

        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to save this file: "
                    + chooser.getSelectedFile().getName());
        }
    }//GEN-LAST:event_MenuSaveAsActionPerformed

    private void onWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowClosing
        prefs.windowBounds = getBounds();
        prefs.write();
    }//GEN-LAST:event_onWindowClosing

    private void MenuOpenFoldersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFoldersActionPerformed
        JFileChooser chooser = new JFileChooser(prefs.previousOpenFolder);
        //chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            int numFilesAdded = ArtLib.get().replace(f.toPath());
            System.out.println("" + numFilesAdded + " files added");
            prefs.previousOpenFolder = f;
        }

    }//GEN-LAST:event_MenuOpenFoldersActionPerformed

    private void MenuFitToWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFitToWindowActionPerformed
        System.out.println("FitToWindow: isSelected: " + MenuFitToWindow.isSelected() + "   " + evt);
        prefs.fitToWindow = MenuFitToWindow.isSelected();
    }//GEN-LAST:event_MenuFitToWindowActionPerformed

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
    private javax.swing.JMenuItem MenuOpenFolders;
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
}
