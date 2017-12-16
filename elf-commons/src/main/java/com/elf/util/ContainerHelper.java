/*
 * ContainerHelper.java
 *
 * Created on March 20, 2005, 9:34 PM
 */

package com.elf.util;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class ContainerHelper
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static String[] toStringArray(AbstractCollection<String> coll) throws ArrayStoreException
    {
	String ss[] = new String[0];
	
	if(coll.size() > 0)
	    ss = (String[]) coll.toArray(ss);
	
	return ss;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static ArrayList<String> toArrayList(String[] ss)
    {
	if(ss == null)
	    return new ArrayList<String>();
	
	ArrayList<String> list = new ArrayList<String>(ss.length);
	
	for(int i = 0; i < ss.length; i++)
	    list.add(ss[i]);
	
	return list;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static String toOneString(String[] ss)
    {
	String s = new String();
	
	for(int i = 0; ss != null && i < ss.length; i++)
	{
	    s += ss[i] + "\n";//NOI18N
	}
	return s;
    }
}

