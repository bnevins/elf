/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import com.elf.util.StringUtils;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Byron Nevins
 */
public class Path {

    public static void main(String[] args) {
        for(String path : paths)
            System.out.println(path);
    }

    public final static String[] paths;

    static {
        String path = System.getenv("Path");

        if (!StringUtils.ok(path))
            paths = new String[0];
        else
            paths = path.split(File.pathSeparator);
    }
}
