/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.xml;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ItunesParser {

	//No generics
	List myEmpls;
	Document dom;


	public ItunesParser(){
		//create a list to hold the employee objects
		myEmpls = new ArrayList();
	}

	public void runExample() {

		//parse the xml file and get the dom object
		parseXmlFile();

		//get each employee element and create a Employee object
		parseDocument();

		//Iterate through the list and print the data
		printData();

	}


	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse("it.xml");
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement(); // plist

		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("dict");

        if(nl == null)
            return; // todo

        int len = nl.getLength();

        if(len == 0)
            return;

			for(int i = 0 ; i < nl.getLength();i++) {
                Node node =  nl.item(i);

                System.out.printf("Item #%d: %s\n", i, node.getNodeName());
				//get the employee element
				//Element el = (Element)nl.item(i);

				//get the Employee object
				//Employee e = getEmployee(el);

				//add it to list
				//myEmpls.add(e);
		}
	}


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	/**
	 * Iterate through the list and print the
	 * content to console
	 */
	private void printData(){

		System.out.println("No of Employees '" + myEmpls.size() + "'.");

		Iterator it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}




	public static void main(String[] args){
		//create an instance
		ItunesParser dpe = new ItunesParser();

		//call run example
		dpe.runExample();
	}

}