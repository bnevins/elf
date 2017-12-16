/*
 * A place for useful static methods dealing with Collections
 */

package com.elf.util;

import java.util.*;

/**
 *
 * @author bnevins
 */
public class CollectionsUtils 
{
    
    private CollectionsUtils()
    {
        // all static methods
    }
    /**
     * It is annoying that Properties is a Map<Object,Object> even though
     *  Properties are supposed to be all Strings.  This will create a Map<String,String>
     *  that is easier to work with.
     * 
     * @param props The Properties object to convert
     * @return A Map copy with the correct types
     */
    public static Map<String,String> propsToMap(Properties props)
    {
        // there is probably a more efficient, cool way to do this, but
        // this works fine and properties objects are generally small.
        // Feel free to improve it!
        
        if(props == null || props.size() <= 0)
            return new HashMap<String,String>();
        
        Map<String,String> map = new HashMap<String,String>(props.size());
        Set<Map.Entry<Object,Object>> pset = props.entrySet();
        
        for(Map.Entry<Object,Object> entry : pset)
        {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            map.put(key, value);
        }
        
        return map;
    }
    
    /**
     * 
     * @param props The Properties object to convert
     * @return An array of String with key=value
     */
    public static String[] propsToStrings(Properties props)
    {
        Map<String, String> map = propsToMap(props);
        Set<Map.Entry<String,String>> set = map.entrySet();
        String[] ret = new String[set.size()];
        int index = 0;
        
        for(Map.Entry<String,String> entry : set)
        {
            ret[index++] = entry.getKey() + "=" + entry.getValue();
        }
        
        return ret;
    }
    
    /*
     * simple test main
     *
    public static void main(String[] args)
    {
        Properties p = new Properties();
        p.setProperty("a", "b");
        p.setProperty("a", "c");
        p.put("g", new File("c:/tmp"));
        Map<String, String> map = CollectionsUtils.propsToMap(p);
        Set<Map.Entry<String,String>> set = map.entrySet();
        for(Map.Entry<String,String> entry : set)
        {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    } 
     */   
}
