package biz.jupload.jdownload.xml;

import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Builds the XmlNode-Tree by parsing the events coming from the SAX Parser
 * 
 * @author Mike Haller <mike.haller@smartwerkz.com>
 * @since 0.8
 */
public class SimpleXmlHandler extends DefaultHandler {

	private XmlNode lastNode;

	private XmlNode root;

	public SimpleXmlHandler(XmlNode root) {
		this.root = root;
	}

	public void startDocument() throws SAXException {
		lastNode = root;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Hashtable attrs = convert(attributes);
		XmlNode node = new XmlNode(lastNode, qName, attrs);
		lastNode.addNode(node);
		lastNode = node;
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		lastNode = lastNode.getParentNode();
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		StringBuilder buf = new StringBuilder(lastNode.getText()==null?"":lastNode.getText());
		String str = new String(ch, start, length);
		buf.append(str);
		lastNode.setText(buf.toString());
	}

	/**
	 * Converts SAX-Attributs to Java-Hashtable
	 * 
	 * @param attributes
	 * @return
	 */
	private Hashtable convert(Attributes attributes) {
		Hashtable result = new Hashtable();
		for (int i = 0; i < attributes.getLength(); i++) {
			String key = attributes.getQName(i);
			String value = attributes.getValue(i);
			result.put(key, value);
		}
		return result;
	}

}
