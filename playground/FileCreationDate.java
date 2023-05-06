/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCreationDate {

	public static void main(String[] argv){

	    	File file = new File( "C:\\Users\\bnevins\\Desktop\\wallpaper\\chubby.jpg" );

		BasicFileAttributes attrs;
		try {
		    attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		    FileTime time = attrs.lastModifiedTime();
		    
		    String pattern = "yyyy-MM-dd HH:mm:ss";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			
		    String formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

		    System.out.println( "The file creation date and time is: " + formatted );
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}