package biz.jupload.jdownload.xml;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Represents an XML Element
 * 
 * @author Mike Haller <mike.haller@smartwerkz.com>
 */
public class XmlNode {

	private final String tag;

	private final Hashtable attributes;

	private final XmlNode parentNode;

	private final List nodes = new ArrayList();

	private String str;

	/**
	 * @param parentNode
	 *            can be null
	 * @param tag
	 *            "download"
	 * @param attributes
	 */
	public XmlNode(XmlNode parentNode, String tag, Hashtable attributes) {
		this.parentNode = parentNode;
		this.attributes = attributes;
		if (tag == null)
			tag = "";
		if (tag.indexOf(" ") >= 0) {
			throw new IllegalArgumentException("Tag names must not contain spaces:" + tag);
		} else {
			this.tag = tag;
		}
	}

	private String parseAttributes(String fullElement) {
		StringTokenizer tok = new StringTokenizer(fullElement);
		String tag = tok.nextToken();
		while (tok.hasMoreTokens()) {
			String concat = tok.nextToken();
			StringTokenizer tok2 = new StringTokenizer(concat, "=");
			String name = tok2.nextToken();
			String value = tok2.nextToken();
			if (value.startsWith("\"")) {
				int len = value.length();
				value = value.substring(1, len - 1);
			}
			this.attributes.put(name, value);
		}
		return tag;
	}

	/**
	 * @param node
	 */
	public void addNode(XmlNode node) {
		this.nodes.add(node);
	}

	/**
	 * @return tag name of the node
	 */
	public String getId() {
		return tag;
	}

	/**
	 * @return the parent node of this node
	 */
	public XmlNode getParentNode() {
		return parentNode;
	}

	/**
	 * @param str
	 */
	public void setText(String str) {
		this.str = str;
	}

	/**
	 * @return
	 * 
	 */
	public List getChildNodes() {
		return nodes;
	}

	public String toString() {
		return "[XmlNode\n\ttag=" + tag + "\n\ttext=" + str + "\n\tattributes="
				+ attributes + "\n\tchilds=" + nodes + "]";
	}

	/**
	 * @return
	 */
	public String getText() {
		return str;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		if (attributes != null) {
			String label = (String) attributes.get("name");
			if (label != null)
				return label;
			return label;
		}
		if (str != null)
			return str;
		return tag;
	}

	/**
	 * @return
	 */
	public Hashtable getAttributes() {
		return attributes;
	}

}
