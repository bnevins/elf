/*
 * BinaryTree.java
 *
 * Created on November 9, 2007, 10:56 PM
 *
 */

package com.elf.util.sort;

import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class BinaryTree
{
    public BinaryTree()
    {
    }

    public boolean add(int value)
    {
        BinaryTreeItem node = root;
        BinaryTreeItem item = new BinaryTreeItem(value);

        if(node == null)
        {
            root = item;
            return true;
        }
        
        while(true)
        {
            int cmp = value - node.value;
            
            if(cmp == 0)
                return false;
            
            else if(cmp < 0)
            {
                if(node.left == null)
                {
                    node.left = item;
                    return true;
                }
                else
                    node = node.left;
            }
         
            else if(cmp > 0)
            {
                if(node.right == null)
                {
                    node.right = item;
                    return true;
                }
                node = node.right;
            }
        }            
    }
    
    public static void main(String[] args)
    {
        /*
        Console cons = System.console();
        BinaryTree tree = new BinaryTree();
        
        while(true)
        {
            String s = cons.readLine("Enter a number to insert: ");
            System.out.println("Entered: " + s);
         
            if(s.equals("-1"))
                System.exit(0);
            
            tree.add(Integer.parseInt(s));
        }
         */
    }
    BinaryTreeItem root;
}    
