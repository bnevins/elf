/*
 * Strings.java
 *
 * Created on February 14, 2006, 2:17 AM
 *
 */

package com.elf.util;

import java.util.*;
import java.text.MessageFormat;

/**
 *
 * @author bnevins
 */

public class Strings
{
	public Strings(String... fqnPropsList)
	{
		for(String fqnProps : fqnPropsList)
			addBundle(fqnProps);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void addBundle(String fqnProps)
	{
		// format: "com.elf.foo.LogStrings"
        try
        {
            bundles.add(ResourceBundle.getBundle(fqnProps));
        }
        catch(Exception e)
        {
			// should throw ???
        }
	}
	
	///////////////////////////////////////////////////////////////////////////

    public String get(String indexString)
    {
		// grab the first property that matches...
		for(ResourceBundle bundle : bundles)
		{
			try
			{
				return bundle.getString(indexString);
			}
			catch (Exception e)
			{
				// not an error...
			}
		}
		// it is not an error to have no key...
		return indexString;
    }

	///////////////////////////////////////////////////////////////////////////
	
	public String get(String indexString, Object... objects)
    {
        indexString = get(indexString);
        
        try
        {
            MessageFormat mf = new MessageFormat(indexString);
            return mf.format(objects);
        }
        catch(Exception e)
        {
            return indexString;
        }
    }

	///////////////////////////////////////////////////////////////////////////
	
	private List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
}
