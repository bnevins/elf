/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bnevins
 */
public class ObjectAnalyzerTest {
    
    public ObjectAnalyzerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of toString method, of class ObjectAnalyzer.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Object obj = null;
        String expResult = "";
        String result = ObjectAnalyzer.toString(obj);
        System.out.println("XXX: " + result + "\n");
//assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of toStringWithSuper method, of class ObjectAnalyzer.
     */
    @Test
    public void testToStringWithSuper() {
        System.out.println("toStringWithSuper");
        Object obj = null;
        String expResult = "";
        String result = ObjectAnalyzer.toStringWithSuper(obj);
        System.out.println("ZZZ: " + result + "\n");
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of main method, of class ObjectAnalyzer.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ObjectAnalyzer.main(args);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
