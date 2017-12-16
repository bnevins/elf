/*
 * BootstrapperInfo.java
 *
 * Created on December 24, 2006, 11:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.filefiler;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class BootstrapperInfo
{
    public BootstrapperInfo(File boot, File filed)
    {
        bootstrapperFile = boot;
        this.filed = filed;
        try
        {
            length = boot.length();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    long    length;
    File    bootstrapperFile;
    File    filed;
}
