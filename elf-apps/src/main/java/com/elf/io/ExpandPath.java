/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.io;

import com.elf.util.StringUtils;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Byron Nevins
 */
public class ExpandPath {
    public static void main(String[] args) {
        String s = System.getenv("Path");
        
        if(!StringUtils.ok(s))
            System.out.println("No Path Found?!?");

        List<String> elems = Arrays.asList(s.split(File.pathSeparator));
        Collections.sort(elems);
        
        for(String elem : elems)
            System.out.println(elem);
    }
}
