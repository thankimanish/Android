package com.hdik.main.simpleparsing.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hdik.main.simpleparsing.beans.BeanContact;
import com.hdik.main.simpleparsing.beans.BeanItems;

public class ParseContent {
	/*Function Parse XML Using Dom Parser*/
	public void parseXmlUsingDom(String response, ArrayList<BeanItems> listItems) {

		final String KEY_ITEM = "item"; // parent node
		final String KEY_NAME = "name";
		final String KEY_COST = "cost";
		final String KEY_DESC = "description";
		DomParser parser = new DomParser();
		String xml = response;
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		BeanItems beanItems;
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			beanItems = new BeanItems();
			Element e = (Element) nl.item(i);
			beanItems.name = parser.getValue(e, KEY_NAME); // name child value
			beanItems.cost = parser.getValue(e, KEY_COST); // cost child value
			beanItems.description = parser.getValue(e, KEY_DESC); // description
																	// child
																	// value
			listItems.add(beanItems);
		}

	}
	/*Function Parse Json */
	public void parseJson(String response, ArrayList<BeanContact> listContect) {
		BeanContact beanContact;
		try {
			JSONObject jObject = new JSONObject(response);
			JSONArray jArrayContect = jObject.getJSONArray("contacts");
			for (int i = 0; i < jArrayContect.length(); i++) {
				beanContact = new BeanContact();
				JSONObject jObjectInner = jArrayContect.getJSONObject(i);
				beanContact.id = jObjectInner.getString("id");
				beanContact.address = jObjectInner.getString("address");
				beanContact.name = jObjectInner.getString("name");
				beanContact.email = jObjectInner.getString("email");
				if (jObjectInner.has("phone")) {
					beanContact.home = jObjectInner.getJSONObject("phone")
							.getString("home");
					beanContact.mobile = jObjectInner.getJSONObject("phone")
							.getString("mobile");
					beanContact.office = jObjectInner.getJSONObject("phone")
							.getString("office");
				}
				listContect.add(beanContact);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*Function Parse XML Using Sax Parser*/
	public void parseXmlUsingSax(String response,
			final ArrayList<BeanItems> listItems) {

		DefaultHandler handler = new DefaultHandler() {
			private BeanItems beanItems;
			private String tempValue = null;

			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				// TODO Auto-generated method stub

				if (qName.equalsIgnoreCase("item")) {
					beanItems = new BeanItems();
				}

			}

			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				// TODO Auto-generated method stub
				if (qName.equals("item")) {
					listItems.add(beanItems);
				}
				if (qName.equalsIgnoreCase("name")) {
					beanItems.name = tempValue;
				}
				if (qName.equalsIgnoreCase("description")) {
					beanItems.description = tempValue;
				}
				if (qName.equalsIgnoreCase("id")) {
					beanItems.id = tempValue;
				}
				if (qName.equalsIgnoreCase("cost")) {
					beanItems.cost = tempValue;
				}

			}

			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException {
				// TODO Auto-generated method stub
				tempValue = new String(ch, start, length);
			}

		};

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(new InputSource(new StringReader(response)), handler);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
