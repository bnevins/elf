/*
 * xml.java
 *
 * Created on February 13, 2004, 12:27 AM
 */

package com.elf.xml;

import java.net.*;

/*/XML imports
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;  
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
*/
/**
 *
 * @author  bnevins
 */
public class xml
{
	
	/** Creates a new instance of xml */
	public xml()
	{
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		URL url = xml.class.getClassLoader().getResource("com/elf/xml/cars.xml");

		EmployeeHandler handler = new EmployeeHandlerImpl();
		EmployeeParser parser	= new EmployeeParser(handler, null);
		try
		{
			parser.parse(url);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
/***
 *

 *
 *
 *
 *
 *
 *
 *
 *        try 
        {
	    // setup schema processing
	    DOMParser parser = new DOMParser();
            parser.setFeature( "http://apache.org/xml/features/dom/defer-node-expansion", true);
            parser.setFeature( "http://xml.org/sax/features/validation", true );
            parser.setFeature( "http://xml.org/sax/features/namespaces", true );
            parser.setFeature( "http://apache.org/xml/features/validation/schema", true );
            parser.setFeature ("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);

            //factory.setValidating(true);
	    // CLILogger.getInstance().printDebugMessage("Validation against DTD is " + factory.isValidating());
            //DocumentBuilder builder = factory.newDocumentBuilder();
            if (descriptors == null)
            {
                getDescriptors();
                if (descriptors == null) 
		{
		    LocalStringsManager lsm = 
			LocalStringsManagerFactory.getFrameworkLocalStringsManager();
		    throw new CommandValidationException(lsm.getString("NoDescriptorsDefined"));
		}
            }
            for (int i = 0; i < descriptors.size(); i++)
            {
                URL descriptorURL = (URL) descriptors.get(i);
                //InputSource is = new InputSource(descriptorURL.toString());
                //Document document = builder.parse(is);
                parser.parse(new InputSource(descriptorURL.toString()));
                Document document = parser.getDocument();
                generateOptionsAndCommands(document);
            }
            if (serializeDescriptors == SERIALIZE_COMMANDS_TO_FILES)
            {
                saveCommandsAsMultipleFiles();
            }
            else if (serializeDescriptors == SERIALIZE_COMMANDS_TO_FILE)
            {
                saveCommandsAsSingleFile();
            }

        } 
        catch (SAXException sxe) 
        {
           // Error generated during parsing)
            Exception  x = sxe;
            if (sxe.getException() != null)
               x = sxe.getException();
	    throw new CommandValidationException(x);
        }
        catch (IOException ioe) 
        {
            // I/O error
	    throw new CommandValidationException(ioe);
            //throw new CommandValidationException(ioe.getLocalizedMessage());
        }
    }
   private void generateOptionsAndCommands(Document document)
    {
        if (document != null) 
        {
            for (Node nextKid = document.getDocumentElement().getFirstChild();
                    nextKid != null; nextKid = nextKid.getNextSibling()) 
            {
                String nodeName = nextKid.getNodeName();
                if (nodeName.equalsIgnoreCase("CommandProperties"))
                {
                    Properties props = new Properties();
                    for (Node grandKid = nextKid.getFirstChild();
                       grandKid != null; grandKid = grandKid.getNextSibling()) 
                    {
                        String grandKidNodeName = grandKid.getNodeName();
                        if (grandKidNodeName.equalsIgnoreCase("CommandProperty")) 
                        {
                            NamedNodeMap nodeMap = grandKid.getAttributes();
                            String nameAttr = 
                                    nodeMap.getNamedItem("name").getNodeValue();
                            String valueAttr = 
                                    nodeMap.getNamedItem("value").getNodeValue();
                            props.setProperty(nameAttr, valueAttr);
                        }
                    }
		    final NamedNodeMap commandPropertiesAttribute = nextKid.getAttributes();
		    if (commandPropertiesAttribute != null &&
			commandPropertiesAttribute.getNamedItem("defaultcommand") != null )
			defaultCommand = commandPropertiesAttribute.getNamedItem(
					 "defaultcommand").getNodeValue();
		    if (commandPropertiesAttribute != null &&
			commandPropertiesAttribute.getNamedItem("helpclass") != null )
			helpClass = commandPropertiesAttribute.getNamedItem(
					 "helpclass").getNodeValue();

                    properties.add(props);
                }
                else if (nodeName.equalsIgnoreCase("Options")) 
                {
                    for (Node grandKid = nextKid.getFirstChild();
                       grandKid != null; grandKid = grandKid.getNextSibling()) 
                    {
                        String grandKidNodeName = grandKid.getNodeName();
                        if (grandKidNodeName.equalsIgnoreCase("Option")) 
                        {
                            ValidOption option = generateOption(document, grandKid);
                            validOptions.put(option.getName(), option);
                        }
                    }
                }
                //Generates the Valid Commands List from the descriptor file by 
                // reading all the Valid Commands
                else if (nodeName.equalsIgnoreCase("Commands")) 
                {
                    for (Node grandKid = nextKid.getFirstChild();
                       grandKid != null; grandKid = grandKid.getNextSibling()) 
                    {
                        String grandKidNodeName = grandKid.getNodeName();
                        if (grandKidNodeName.equalsIgnoreCase("Command")) 
                        {
                            ValidCommand command = generateCommand(document, grandKid);
                            commandsList.addCommand(command);
                        }
                    }
                }
            }
        }
 
 */
