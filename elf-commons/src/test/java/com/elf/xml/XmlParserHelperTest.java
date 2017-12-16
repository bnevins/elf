package com.elf.xml;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.*;
import javax.xml.stream.XMLStreamReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 *
 * @author bnevins
 */
public class XmlParserHelperTest {

    private static XmlParserHelper helper;
    private XMLStreamReader parser;

    public XmlParserHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        carFile = new File(XmlParserHelperTest.class.getClassLoader().getResource("cars.xml").getPath());
        assertTrue(carFile.isFile());
        helper = new XmlParserHelper(carFile);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class XmlParserHelper.
     */
    @Test
    public void testCars() {
        try {
            parser = helper.get();
            read();
            helper.stop();
        }
        catch (XMLStreamException ex) {
            Logger.getLogger(XmlParserHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void read() throws XMLStreamException {
        while (true) {
            int event = parser.next();

            if (event == END_DOCUMENT)
                return;

            if (event == START_ELEMENT) {
                System.out.print("name; " + parser.getLocalName());
                try {
                    System.out.println("  text:" + parser.getElementText());
                }
                catch (Exception e) {
                    System.out.println("  NO TEXT");
                }
            }
        }
    }
    private static File carFile;
}
