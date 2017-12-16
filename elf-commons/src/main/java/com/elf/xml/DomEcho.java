/*
 * xml.java
 *
 * Created on June 16, 2007, 12:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.xml;

import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;
import org.w3c.dom.*;

/**
 *
 * @author bnevins
 */
public class DomEcho
{
    static Document document;
    /** Creates a new instance of xml */
    public DomEcho()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv)
    {
        if (argv.length != 1)
        {
            System.err.println("Usage: java DomEcho filename");
            System.exit(1);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new SimpleErrorHandler());
            document = builder.parse( new File(argv[0]) );
        }
        catch (SAXException sxe)
        {
            // Error generated during parsing
            Exception  x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
            
        }
        catch (ParserConfigurationException pce)
        {
            // Parser with specified options can't be built
            pce.printStackTrace();
            
        }
        catch (IOException ioe)
        {
            // I/O error
            ioe.printStackTrace();
        }
    }
}

class SimpleErrorHandler implements  org.xml.sax.ErrorHandler
{
    // ignore fatal errors (an exception is guaranteed)
    public void fatalError(SAXParseException exception) throws SAXException
    {
    }
    
    // treat validation errors as fatal
    public void error(SAXParseException e) throws SAXParseException
    {
        throw e;
    }
    
    // dump warnings too
    public void warning(SAXParseException err) throws SAXParseException
    {
        System.out.println("** Warning"
            + ", line " + err.getLineNumber()
            + ", uri " + err.getSystemId());
        System.out.println("   " + err.getMessage());
    }
}

