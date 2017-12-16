/*
 * ThumbnailMakerInfo.java
 *
 * Created on June 4, 2006, 10:48 AM
 */

package com.elf.graphics;

import java.beans.*;
import java.io.Serializable;

/**
 * @author bnevins
 */
public class ThumbnailMakerInfo implements Serializable
{
    
    public ThumbnailMakerInfo()
    {
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("rootdir: " + getRootDir());
        sb.append("\n");
        sb.append("prefix: " + getPrefix());
        sb.append("\n");
        sb.append("overwrite: " + getOverwrite());
        sb.append("\n");
        sb.append("recursive: " + getRecursive());
        sb.append("\n");
        sb.append("scaleType: " + getScaleType());
        sb.append("\n");
        sb.append("number1: " + getNumber1());
        sb.append("\n");
        sb.append("number2: " + getNumber2());
        sb.append("\n");
        return sb.toString();
    }

    public void validate()
    {
        
    }
    public String getRootDir()
    {
        return rootDir;
    }
    
    public void setRootDir(String value)
    {
        rootDir = value;
    }

    public String getPrefix()
    {
        return prefix;
    }
    
    public void setPrefix(String value)
    {
        prefix = value;
    }

    public boolean getOverwrite()
    {
        return overwrite;
    }
    
    public void setOverwrite(boolean value)
    {
        overwrite = value;
    }
    public boolean getRecursive()
    {
        return recursive;
    }
    
    public void setRecursive(boolean value)
    {
        recursive = value;
    }
    
    public Scales getScaleType()
    {
        return scaleType;
    }
    
    public void setScaleType(Scales value)
    {
        scaleType = value;
    }
    
    public String getNumber1()
    {
        return number1;
    }
    
    public void setNumber1(String value)
    {
        number1 = value;
    }
    public String getNumber2()
    {
        return number2;
    }
    
    public void setNumber2(String value)
    {
        number2 = value;
    }

    public int getNumber1AsInt()
    {
        try
        {
            return Integer.parseInt(getNumber1());
        }
        catch(NumberFormatException e)
        {
            return -1;
        }
    }
    public int getNumber2AsInt()
    {
        try
        {
            return Integer.parseInt(getNumber2());
        }
        catch(NumberFormatException e)
        {
            return -1;
        }
    }

    public double getNumber1AsDouble()
    {
        try
        {
            return Double.parseDouble(getNumber1());
        }
        catch(NumberFormatException e)
        {
            return -1.0;
        }
    }
    private String rootDir;
    private String prefix;
    private boolean overwrite;
    private boolean recursive;
    private Scales scaleType;
    private String number1;
    private String number2;
    public enum Scales{ SCALE, BOUNDING_BOX, MAX_DIM, WIDTH_HEIGHT}
}
