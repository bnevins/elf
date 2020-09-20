/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

/**
 *
 * @author Neon Tetras
 */
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Viewer extends javax.swing.JFrame {

    /**
     * Class constructor: Viewer
     */
    public Viewer() {
        initComponents();
        listFiles(Path);    //Lists all the files in the directory on window opening
        setLabelIcon(Path, filenames[position]); //sets the label to display the first
        //image in the directory on window opening.
        PreviousButton.setEnabled(false);
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        setTitle("Raspberry Image Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());// The layout is BorderLayout
        //setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setBackground(java.awt.Color.GRAY);
        picLabel = new javax.swing.JLabel();        //Create the Label to display the picture
        picLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        picLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PreviousButton = new javax.swing.JButton();
        PreviousButton.setText("Previous");
        PreviousButton.setIconTextGap(10); //Distance between the icon and text is 10
        PreviousButton.addActionListener(new java.awt.event.ActionListener() { //Register an actionListener for the PreviousButton
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousButtonActionPerformed(evt);
            }
        });
        NextButton = new javax.swing.JButton();
        NextButton.setPreferredSize(PreviousButton.getPreferredSize());
        NextButton.setText("Next");
        NextButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        NextButton.setIconTextGap(10); //Distance between the icon and text is 10
        NextButton.addActionListener(new java.awt.event.ActionListener() {  //Registers an actionListener for the NextButton
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });
        javax.swing.Box vbox = javax.swing.Box.createVerticalBox(); //VerticalBox to hold the pictureLabel and the buttons
        vbox.add(javax.swing.Box.createVerticalStrut(30));
        vbox.add(picLabel);
        vbox.add(javax.swing.Box.createVerticalStrut(50));
        javax.swing.JPanel prev_next_pane = new javax.swing.JPanel(); //Panel to hold the Previous and Next buttons.
        java.awt.FlowLayout flow = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER);
        flow.setHgap(30);
        prev_next_pane.setLayout(flow);
        prev_next_pane.add(PreviousButton);
        prev_next_pane.add(NextButton);
        prev_next_pane.setOpaque(false);
        vbox.add(prev_next_pane); //Add the panel to the verticalBox
        add(vbox);
        java.awt.Toolkit theKit = getToolkit();
        java.awt.Dimension size = theKit.getScreenSize();
        setLocation(size.width / 5, size.height / 10);
        setMinimumSize(new java.awt.Dimension(900, 600));
        //setMaximumSize(new Dimension(size.width/4 +  50,size.height/4));
        //pack();
    }//END:initComponents

    /**
     * Handler for previous button
     */
    private void PreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {
        position--;  //Decrement the index position of the array of filenames  by one on buttonPressed
        if (!NextButton.isEnabled()) {      //if NextButton is
            NextButton.setEnabled(true);  //disabled, enable it
        }
        if (position == 0) {                    //If we are viewing the first Picture in 
            PreviousButton.setEnabled(false); //the directory, disable previous button
        }
        setLabelIcon(Path, filenames[position]);
    }//End of PreviousButton handler

    /**
     * Handler for NextButton
     */
    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        position++; //Increment the index position of array of filenames  by one on buttonPressed
        if (!PreviousButton.isEnabled()) {
            listFiles(Path);
            PreviousButton.setEnabled(true);
        }
        if (position == filenames.length) {
            NextButton.setEnabled(false);
            position--;
            return;
        }
        setLabelIcon(Path, filenames[position]);
    }//End of NextButton handler

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
             * Set the Nimbus look and feel
         */
 /*
             * If Nimbus (introduced in Java SE 6) is not available, stay with the
             * default look and feel. For details see
             * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /*
             * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Viewer().setVisible(true);
            }
        });
    }

    /**
     * Method to list all the files in the directory And store their names in an
     * array
     */
    private void listFiles(String Path) {
        File file = new File(Path);
        filenames = file.list(); //store the file names in the array of strings.
        for (String names : filenames) {
            System.out.println(names); //Print the filenames to the console just so you can see 
        }
    }
    //Method to set the picture on the label

    private void setLabelIcon(String Path, String name) {
        File file = new File(Path + "\\" + name);
        java.awt.image.BufferedImage image = null;
        try {
            image = ImageIO.read(file); //Reas the image from the file.
        } catch (IOException ie) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error reading image file", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        ImageIcon icon = new ImageIcon(image);
        int width = 600;
        int height = 400;
        // WBN
        //Image img = icon.getImage(); //Images produced will remain a fixed size, 600 * 400
        //Image img = icon.getImage().getScaledInstance(width,height,java.awt.Image.SCALE_SMOOTH); //Images produced will remain a fixed size, 600 * 400
        //Image img = icon.getImage().getScaledInstance(width,height,java.awt.Image.SCALE_DEFAULT); //Images produced will remain a fixed size, 600 * 400
        Image img = scaleImage(icon, width, height); //Images produced will remain a fixed size, 600 * 400
        ImageIcon newIcon = new ImageIcon(img); //Create a new imageicon from an image object.
        //Now we want to create a caption for the pictures using their file names
        String pictureName = file.getName();
        int pos = pictureName.lastIndexOf(".");        //This removes the extensions 
        String caption = pictureName.substring(0, pos); //from the file names. e.g .gif, .jpg, .png
        picLabel.setIcon(newIcon);                   //Set the imageIcon on the Label
        picLabel.setText(caption);                   //Set the caption
        picLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); //Caption appears below the image
    }
    //Variables declaration
    int position = 0; //Initial position is 0
    String[] filenames; //Array to hold the file names
    //final String Path = System.getProperty("user.home") +"\\User_Name\\pictures\\"; //path to your images
    final String Path = "c:/tmp/pix"; //path to your images
    private javax.swing.JButton NextButton;
    private javax.swing.JButton PreviousButton;
    private javax.swing.JLabel picLabel;
    // End of variables declaration//GEN-END:variables

    private Image scaleImage(ImageIcon icon, int screenWidth, int screenHeight) {
        Image image = icon.getImage();
        int y = image.getHeight(null);
        int x = image.getWidth(null);
        double doubleX = x;
        double doubleY = y;

        if (x <= 0 || y <= 0) {
            throw new RuntimeException("add Lazy calculation of image dimensions!");
        }
        double xRatio = (double) screenWidth / doubleX;
        double yRatio = (double) screenHeight / doubleY;
        int scaledWidth;
        int scaledHeight;
        // whichever is LESS is the "limiting" dimension
        if (xRatio >= 1.0 && yRatio > 1.0) {
            scaledWidth = x;
            scaledHeight = y;
        } else if (xRatio <= yRatio) {
            scaledWidth = (int) (doubleX * xRatio);
            scaledHeight = (int) (doubleY * xRatio);
        } else { // yRatio is smaller
            scaledWidth = (int) (doubleX * yRatio);
            scaledHeight = (int) (doubleY * yRatio);
        }
        return image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
    }
}//End of class
