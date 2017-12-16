/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
public class ReflectionUtilsTest {

    public ReflectionUtilsTest() {
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
     * Test of getAllAnnotations method, of class ReflectionUtils.
     */
    @Test
    public void testGetAllAnnotations() {

        try {
            System.out.println(System.getProperty("java.class.path"));
            Class clazz = Class.forName("com.elf.util.Child");
            System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
            System.out.println(new ReflectionUtils().getAllAnnotations(clazz));
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
//
//        System.out.println("getAllAnnotations");
//        Class clazz = null;
//        ReflectionUtils instance = new ReflectionUtils();
//        String expResult = "";
//        String result = instance.getAllAnnotations(clazz);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
