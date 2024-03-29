/*
 * File:           EmployeeScanner.java
 * Generated from: employee.dtd
 * Date:           February 13, 2004  1:37 AM
 *
 * @author  bnevins
 * @version generated by NetBeans XML module
 */
package com.elf.xml;
/**
 * This is a scanner of DOM tree.
 *
 * Example:
 * <pre>
 *     javax.xml.parsers.DocumentBuilderFactory builderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
 *     javax.xml.parsers.DocumentBuilder builder = builderFactory.newDocumentBuilder();
 *     org.w3c.dom.Document document = builder.parse (new org.xml.sax.InputSource (???));
 *     <font color="blue">EmployeeScanner scanner = new EmployeeScanner (document);</font>
 *     <font color="blue">scanner.visitDocument();</font>
 * </pre>
 *
 * @see org.w3c.dom.Document
 * @see org.w3c.dom.Element
 * @see org.w3c.dom.NamedNodeMap
 *
 */
public class EmployeeScanner
{
	/** org.w3c.dom.Document document */
	org.w3c.dom.Document document;
	
	/** Create new EmployeeScanner with org.w3c.dom.Document. */
	public EmployeeScanner(org.w3c.dom.Document document)
	{
		this.document = document;
	}
	
	/** Scan through org.w3c.dom.Document document. */
	public void visitDocument()
	{
		org.w3c.dom.Element element = document.getDocumentElement();
		if ((element != null) && element.getTagName().equals("CARS"))
		{
			visitElement_CARS(element);
		}
		if ((element != null) && element.getTagName().equals("CAR"))
		{
			visitElement_CAR(element);
		}
		if ((element != null) && element.getTagName().equals("NAME"))
		{
			visitElement_NAME(element);
		}
	}
	
	/** Scan through org.w3c.dom.Element named CARS. */
	void visitElement_CARS(org.w3c.dom.Element element)
	{ // <CARS>
		// element.getValue();
		org.w3c.dom.NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			org.w3c.dom.Node node = nodes.item(i);
			switch (node.getNodeType())
			{
				case org.w3c.dom.Node.CDATA_SECTION_NODE:
					// ((org.w3c.dom.CDATASection)node).getData();
					break;
				case org.w3c.dom.Node.ELEMENT_NODE:
					org.w3c.dom.Element nodeElement = (org.w3c.dom.Element)node;
					if (nodeElement.getTagName().equals("CAR"))
					{
						visitElement_CAR(nodeElement);
					}
					break;
				case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
					// ((org.w3c.dom.ProcessingInstruction)node).getTarget();
					// ((org.w3c.dom.ProcessingInstruction)node).getData();
					break;
			}
		}
	}
	
	/** Scan through org.w3c.dom.Element named CAR. */
	void visitElement_CAR(org.w3c.dom.Element element)
	{ // <CAR>
		// element.getValue();
		org.w3c.dom.NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			org.w3c.dom.Node node = nodes.item(i);
			switch (node.getNodeType())
			{
				case org.w3c.dom.Node.CDATA_SECTION_NODE:
					// ((org.w3c.dom.CDATASection)node).getData();
					break;
				case org.w3c.dom.Node.ELEMENT_NODE:
					org.w3c.dom.Element nodeElement = (org.w3c.dom.Element)node;
					if (nodeElement.getTagName().equals("NAME"))
					{
						visitElement_NAME(nodeElement);
					}
					break;
				case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
					// ((org.w3c.dom.ProcessingInstruction)node).getTarget();
					// ((org.w3c.dom.ProcessingInstruction)node).getData();
					break;
			}
		}
	}
	
	/** Scan through org.w3c.dom.Element named NAME. */
	void visitElement_NAME(org.w3c.dom.Element element)
	{ // <NAME>
		// element.getValue();
		org.w3c.dom.NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			org.w3c.dom.Node node = nodes.item(i);
			switch (node.getNodeType())
			{
				case org.w3c.dom.Node.CDATA_SECTION_NODE:
					// ((org.w3c.dom.CDATASection)node).getData();
					break;
				case org.w3c.dom.Node.ELEMENT_NODE:
					org.w3c.dom.Element nodeElement = (org.w3c.dom.Element)node;
					break;
				case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
					// ((org.w3c.dom.ProcessingInstruction)node).getTarget();
					// ((org.w3c.dom.ProcessingInstruction)node).getData();
					break;
				case org.w3c.dom.Node.TEXT_NODE:
					// ((org.w3c.dom.Text)node).getData();
					break;
			}
		}
	}
	
	
}
