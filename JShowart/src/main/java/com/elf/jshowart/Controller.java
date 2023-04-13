/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class Controller extends JFrame {

    private final UserPreferences prefs;
    private View view;
    private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private boolean currentFullScreen = false;
    private KeyHandler keyHandler;
    private SlideShow slideshow = null;
    // keep it alive because it can be S-L-O-W to start
    /**
     * Creates new form JShowartFrame
     */
                                                    
   public Controller() {
        prefs = UserPreferences.get();
        initComponents();
        clearAllSortButtons();
        clearAllShrinkButtons();
        MenuSlideshow.setSelected(false);
        
        switch (prefs.getSortType()) {
            case "Name" ->
                MenuSortName.setSelected(true);
            case "Date" ->
                MenuSortDate.setSelected(true);
            case "Size" ->
                MenuSortSize.setSelected(true);
            case "Random" ->
                MenuSortRandom.setSelected(true);
            default -> { // can't happen
                MenuSortName.setSelected(true);
                prefs.setSortType("Name");
            }
        }
        if(prefs.isSortAscending()) 
            MenuSortAscending.setSelected(true);
        else
            MenuSortDescending.setSelected(true);
            
        sortTypeGroup.add(MenuSortName);
        sortTypeGroup.add(MenuSortDate);
        sortTypeGroup.add(MenuSortSize);
        sortTypeGroup.add(MenuSortRandom);
        sortDirectionGroup.add(MenuSortAscending);
        sortDirectionGroup.add(MenuSortDescending);
        shrinkGroup.add(MenuNoShrink);
        shrinkGroup.add(MenuShrinkHalfxxx);
        shrinkGroup.add(MenuShrinkFourth);
        shrinkGroup.add(MenuShrinkEighth);
        MenuFitToWindow.setSelected(prefs.fitToWindow);
        setBounds(prefs.windowBounds);
        MenuDebugMode.setSelected(prefs.isDebug());
        
        // note: they get keystrokes in the order that they're added!  View first, then Controller, then KeyHandler
        addKeyListener(keyHandler = new KeyHandler());
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

        sortTypeGroup = new javax.swing.ButtonGroup();
        sortDirectionGroup = new javax.swing.ButtonGroup();
        jMenu1 = new javax.swing.JMenu();
        shrinkGroup = new javax.swing.ButtonGroup();
        MenuBar = new javax.swing.JMenuBar();
        MenuFile = new javax.swing.JMenu();
        MenuOpenFiles = new javax.swing.JMenuItem();
        MenuSave = new javax.swing.JMenuItem();
        MenuSaveAs = new javax.swing.JMenuItem();
        MenuSaveCurrentSize = new javax.swing.JMenuItem();
        MenuEdit = new javax.swing.JMenu();
        MenuView = new javax.swing.JMenu();
        MenuFitToWindow = new javax.swing.JCheckBoxMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        MenuRotate90 = new javax.swing.JMenuItem();
        MenuRotate180 = new javax.swing.JMenuItem();
        MenuRotate270 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        MenuShrinkHalf = new javax.swing.JMenuItem();
        MenuShrink = new javax.swing.JMenu();
        MenuNoShrink = new javax.swing.JRadioButtonMenuItem();
        MenuShrinkHalfxxx = new javax.swing.JRadioButtonMenuItem();
        MenuShrinkFourth = new javax.swing.JRadioButtonMenuItem();
        MenuShrinkEighth = new javax.swing.JRadioButtonMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        MenuSort = new javax.swing.JMenu();
        MenuSortName = new javax.swing.JRadioButtonMenuItem();
        MenuSortSize = new javax.swing.JRadioButtonMenuItem();
        MenuSortDate = new javax.swing.JRadioButtonMenuItem();
        MenuSortRandom = new javax.swing.JRadioButtonMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        MenuSortAscending = new javax.swing.JRadioButtonMenuItem();
        MenuSortDescending = new javax.swing.JRadioButtonMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        MenuUtilities = new javax.swing.JMenu();
        MenuDebugMode = new javax.swing.JCheckBoxMenuItem();
        MenuSlideshow = new javax.swing.JCheckBoxMenuItem();
        MenuHelp = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JShowArt");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWindowClosing(evt);
            }
        });

        MenuFile.setText("File");
        MenuFile.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                MenuFileMenuSelected(evt);
            }
        });

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

        MenuSaveCurrentSize.setText("Save Current Size");
        MenuSaveCurrentSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveCurrentSizeActionPerformed(evt);
            }
        });
        MenuFile.add(MenuSaveCurrentSize);

        MenuBar.add(MenuFile);

        MenuEdit.setText("Edit");
        MenuBar.add(MenuEdit);

        MenuView.setText("View");
        MenuView.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                MenuViewMenuSelected(evt);
            }
        });

        MenuFitToWindow.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuFitToWindow.setSelected(true);
        MenuFitToWindow.setText("Fit To Window");
        MenuFitToWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFitToWindowActionPerformed(evt);
            }
        });
        MenuView.add(MenuFitToWindow);
        MenuView.add(jSeparator7);

        MenuRotate90.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuRotate90.setText("Rotate 90 CW");
        MenuRotate90.setToolTipText("Rotate 90 CW and Save");
        MenuRotate90.setActionCommand("rotate90");
        MenuRotate90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotate90(evt);
            }
        });
        MenuView.add(MenuRotate90);

        MenuRotate180.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuRotate180.setText("Rotate 180 CW");
        MenuRotate180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRotate180ActionPerformed(evt);
            }
        });
        MenuView.add(MenuRotate180);

        MenuRotate270.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuRotate270.setText("Rotate 270 CW");
        MenuRotate270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRotate270ActionPerformed(evt);
            }
        });
        MenuView.add(MenuRotate270);
        MenuView.add(jSeparator2);

        MenuShrinkHalf.setText("Shrink 1/2");
        MenuShrinkHalf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuShrinkHalfActionPerformed(evt);
            }
        });
        MenuView.add(MenuShrinkHalf);

        MenuShrink.setText("Shrink");

        MenuNoShrink.setSelected(true);
        MenuNoShrink.setText("No Shrink");
        MenuNoShrink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuNoShrinkActionPerformed(evt);
            }
        });
        MenuShrink.add(MenuNoShrink);

        MenuShrinkHalfxxx.setText("Shrink 1/2");
        MenuShrinkHalfxxx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuShrinkHalfxxxActionPerformed(evt);
            }
        });
        MenuShrink.add(MenuShrinkHalfxxx);

        MenuShrinkFourth.setText("Shrink 1/4");
        MenuShrinkFourth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuShrinkFourthActionPerformed(evt);
            }
        });
        MenuShrink.add(MenuShrinkFourth);

        MenuShrinkEighth.setText("Shrink 1/8");
        MenuShrinkEighth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuShrinkEighthActionPerformed(evt);
            }
        });
        MenuShrink.add(MenuShrinkEighth);

        MenuView.add(MenuShrink);
        MenuView.add(jSeparator1);

        MenuSort.setText("Sort");

        MenuSortName.setText("Name");
        MenuSortName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortNameActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortName);

        MenuSortSize.setText("Size");
        MenuSortSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortSizeActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortSize);

        MenuSortDate.setText("Date");
        MenuSortDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortDateActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortDate);

        MenuSortRandom.setText("Random");
        MenuSortRandom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortRandomActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortRandom);
        MenuSort.add(jSeparator9);

        MenuSortAscending.setText("Ascending");
        MenuSortAscending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortAscendingActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortAscending);

        MenuSortDescending.setText("Descending");
        MenuSortDescending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSortDescendingActionPerformed(evt);
            }
        });
        MenuSort.add(MenuSortDescending);

        MenuView.add(MenuSort);
        MenuView.add(jSeparator8);

        MenuBar.add(MenuView);

        MenuUtilities.setText("Utilities");
        MenuUtilities.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                MenuUtilitiesMenuSelected(evt);
            }
        });

        MenuDebugMode.setSelected(true);
        MenuDebugMode.setText("Debug Mode");
        MenuDebugMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuDebugModeActionPerformed(evt);
            }
        });
        MenuUtilities.add(MenuDebugMode);

        MenuSlideshow.setSelected(true);
        MenuSlideshow.setText("Slideshow");
        MenuSlideshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSlideshowActionPerformed(evt);
            }
        });
        MenuUtilities.add(MenuSlideshow);

        MenuBar.add(MenuUtilities);

        MenuHelp.setText("Help");

        about.setText("About JShowArt...");
        about.setToolTipText("");
        about.setActionCommand("");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        MenuHelp.add(about);

        MenuBar.add(MenuHelp);

        setJMenuBar(MenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        AboutDialog about = new AboutDialog(this, true);
        about.setVisible(true);
    }//GEN-LAST:event_aboutActionPerformed

    private void MenuOpenFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFilesActionPerformed
        var chooser = Globals.setupAndGetOpenFileChooser();
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            int numFilesAdded = Model.get().replace(files);

            if (numFilesAdded <= 0)
                enableSaveImages(false);
            else {
                enableSaveImages(true);
                prefs.previousOpenFileParent = chooser.getCurrentDirectory();
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
        repaint();
    }//GEN-LAST:event_MenuFitToWindowActionPerformed

    private void MenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveActionPerformed
        // TODO add your handling code here:
        assert (view != null);
        view.save();
    }//GEN-LAST:event_MenuSaveActionPerformed

    private void rotate90(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotate90
        view.rotate(90);
    }//GEN-LAST:event_rotate90

    private void MenuRotate180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRotate180ActionPerformed
        view.rotate(180);
    }//GEN-LAST:event_MenuRotate180ActionPerformed

    private void MenuRotate270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRotate270ActionPerformed
        view.rotate(270);
    }//GEN-LAST:event_MenuRotate270ActionPerformed

    private void MenuSaveCurrentSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveCurrentSizeActionPerformed
        view.saveCurrentSize();
    }//GEN-LAST:event_MenuSaveCurrentSizeActionPerformed

    private void MenuShrinkHalfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuShrinkHalfActionPerformed
        view.showScaled(0.5);
    }//GEN-LAST:event_MenuShrinkHalfActionPerformed

    private void MenuFileMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_MenuFileMenuSelected
        boolean hasImage = view.hasImageLoaded();
        MenuSave.setEnabled(hasImage);
        MenuSaveAs.setEnabled(hasImage);
        MenuSaveCurrentSize.setEnabled(hasImage && prefs.fitToWindow);
    }//GEN-LAST:event_MenuFileMenuSelected

    private void MenuViewMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_MenuViewMenuSelected
        boolean hasImage = view.hasImageLoaded();
        MenuFitToWindow.setEnabled(hasImage);
        MenuRotate90.setEnabled(hasImage);
        MenuRotate180.setEnabled(hasImage);
        MenuRotate270.setEnabled(hasImage);
        MenuShrinkHalf.setEnabled(hasImage);
        MenuShrink.setEnabled(hasImage);
    }//GEN-LAST:event_MenuViewMenuSelected

    private void MenuSortNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortNameActionPerformed
        menuSortHelper(evt);
    }//GEN-LAST:event_MenuSortNameActionPerformed

    private void MenuSortSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortSizeActionPerformed
        menuSortHelper(evt);
    }//GEN-LAST:event_MenuSortSizeActionPerformed

    private void MenuSortDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortDateActionPerformed
        menuSortHelper(evt);
    }//GEN-LAST:event_MenuSortDateActionPerformed

    private void MenuSortRandomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortRandomActionPerformed
        menuSortHelper(evt);
    }//GEN-LAST:event_MenuSortRandomActionPerformed

    private void MenuSortAscendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortAscendingActionPerformed
        menuSortDirectionHelper(true);
    }//GEN-LAST:event_MenuSortAscendingActionPerformed

    private void MenuSortDescendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSortDescendingActionPerformed
        menuSortDirectionHelper(false);
    }//GEN-LAST:event_MenuSortDescendingActionPerformed

    private void MenuDebugModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuDebugModeActionPerformed
        // TODO add your handling code here:
        prefs.setDebug(MenuDebugMode.isSelected());
    }//GEN-LAST:event_MenuDebugModeActionPerformed

    private void MenuSlideshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSlideshowActionPerformed
        if(slideshow == null ) {
            MenuSlideshow.setSelected(true);
            slideshow = new SlideShow();
        }
        else {
            MenuSlideshow.setSelected(false);
            slideshow.stop();
            slideshow = null;
        }
    }//GEN-LAST:event_MenuSlideshowActionPerformed

    private void MenuUtilitiesMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_MenuUtilitiesMenuSelected
        MenuSlideshow.setEnabled(Model.get().numImages() > 1);
    }//GEN-LAST:event_MenuUtilitiesMenuSelected
    private void MenuShrinkHalfxxxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuShrinkHalfxxxActionPerformed
        view.setShrinkFactor(2);
    }//GEN-LAST:event_MenuShrinkHalfxxxActionPerformed

    private void MenuShrinkFourthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuShrinkFourthActionPerformed
        view.setShrinkFactor(4);
    }//GEN-LAST:event_MenuShrinkFourthActionPerformed

    private void MenuShrinkEighthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuShrinkEighthActionPerformed
        view.setShrinkFactor(8);
    }//GEN-LAST:event_MenuShrinkEighthActionPerformed

    private void MenuNoShrinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuNoShrinkActionPerformed
        view.setShrinkFactor(1);
    }//GEN-LAST:event_MenuNoShrinkActionPerformed

    private void menuSortHelper(java.awt.event.ActionEvent evt) {
        String type = evt.getActionCommand();
        prefs.setSortType(type);
        Model.get().sort();
    }
    private void menuSortDirectionHelper(boolean ascending) {
        prefs.setSortAscending(ascending);
        Model.get().sort();
    }
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
            java.util.logging.Logger.getLogger(Controller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Controller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Controller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Controller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Controller().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JCheckBoxMenuItem MenuDebugMode;
    private javax.swing.JMenu MenuEdit;
    private javax.swing.JMenu MenuFile;
    private javax.swing.JCheckBoxMenuItem MenuFitToWindow;
    private javax.swing.JMenu MenuHelp;
    private javax.swing.JRadioButtonMenuItem MenuNoShrink;
    private javax.swing.JMenuItem MenuOpenFiles;
    private javax.swing.JMenuItem MenuRotate180;
    private javax.swing.JMenuItem MenuRotate270;
    private javax.swing.JMenuItem MenuRotate90;
    private javax.swing.JMenuItem MenuSave;
    private javax.swing.JMenuItem MenuSaveAs;
    private javax.swing.JMenuItem MenuSaveCurrentSize;
    private javax.swing.JMenu MenuShrink;
    private javax.swing.JRadioButtonMenuItem MenuShrinkEighth;
    private javax.swing.JRadioButtonMenuItem MenuShrinkFourth;
    private javax.swing.JMenuItem MenuShrinkHalf;
    private javax.swing.JRadioButtonMenuItem MenuShrinkHalfxxx;
    private javax.swing.JCheckBoxMenuItem MenuSlideshow;
    private javax.swing.JMenu MenuSort;
    private javax.swing.JRadioButtonMenuItem MenuSortAscending;
    private javax.swing.JRadioButtonMenuItem MenuSortDate;
    private javax.swing.JRadioButtonMenuItem MenuSortDescending;
    private javax.swing.JRadioButtonMenuItem MenuSortName;
    private javax.swing.JRadioButtonMenuItem MenuSortRandom;
    private javax.swing.JRadioButtonMenuItem MenuSortSize;
    private javax.swing.JMenu MenuUtilities;
    private javax.swing.JMenu MenuView;
    private javax.swing.JMenuItem about;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.ButtonGroup shrinkGroup;
    private javax.swing.ButtonGroup sortDirectionGroup;
    private javax.swing.ButtonGroup sortTypeGroup;
    // End of variables declaration//GEN-END:variables

    void setView(View view) {
        this.view = view;
    }

    private void clearAllSortButtons() {
        // being rigorous.  Setting default in NB Design can foul things up!
        MenuSortName.setSelected(false);
        MenuSortDate.setSelected(false);
        MenuSortSize.setSelected(false);
        MenuSortRandom.setSelected(false);
        MenuSortAscending.setSelected(false);
        MenuSortDescending.setSelected(false);
    }

    private void clearAllShrinkButtons() {
        MenuNoShrink.setSelected(true);
        MenuShrinkHalfxxx.setSelected(false);
        MenuShrinkFourth.setSelected(false);
        MenuShrinkEighth.setSelected(false);
    }
}
