/*
 * Main.java
 *
 * Created on February 1, 2006, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;


import java.io.*;
import java.net.*;

public class Main
{
	public Main()
	{
		
	}
	public static void main(String[] args)
	{
		try
		{
			Project p = new Project("C:/dev/elf/elf-commons/src/java/com/elf/util/web/remodel.xml");
			System.out.println(p);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}