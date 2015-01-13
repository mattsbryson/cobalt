// $Id: Explorer.java,v 1.7 2005/03/22 14:42:24 mhaller Exp $



import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
//import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import biz.jupload.jdownload.xml.SimpleXmlHandler;
import biz.jupload.jdownload.xml.XmlNode;

/**
 * Explorer represents the graphical main part of the applet, also initializes
 * the model
 * 
 * @author Mike Haller
 */
public class Explorer extends JScrollPane {

	/**
	 * Serializable Unique Identifier
	 */
	private static final long serialVersionUID = 3258128063895123251L;
	
	//private static final Logger log = Logger.getAnonymousLogger();
	
	private DefaultTreeModel treemodel;

	private JTree tree;

	private boolean flagInited;

	private XmlNode xml_root;

	/**
	 * @param db
	 *            DocumentBuilder from main applet
	 */
	public Explorer() {
		this.flagInited = false;
	}

	public DefaultTreeModel getTreeModel() {
		return treemodel;
	}

	public void init() {
		this.flagInited = true;
		setBackground(Manager.bgColor);

		// Get the URL from the PARAM settings
		String dataURL = Manager.getInstance().getParameter("dataURL"); //$NON-NLS-1$
		//log.fine("init() loading data from " + dataURL); //$NON-NLS-1$

		try {
			// Now parse the XML file.

			// debug
			if (null == dataURL) {
				//System.err.println(Messages.getString("Explorer.ErrorMessage.NoDataURLSpecified")); //$NON-NLS-1$
				Manager.getInstance().disableAll();
				return;
			}

			URL context = Manager.getInstance().getCodeBase();
			URL url = new URL(context, dataURL);

			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();
				xml_root = new XmlNode(null, null, null);
				SimpleXmlHandler docHandler = new SimpleXmlHandler(xml_root);
				parser.parse(url.openStream(), docHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			DownloadItem rootDownloadItem = new DownloadItem(xml_root);
			DownloadTreeNode root = new DownloadTreeNode(rootDownloadItem);
			treemodel = new DefaultTreeModel(root);

			//log.fine("doc=" + xml_root); //$NON-NLS-1$
			processRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create list and add to scrollpane
		tree = new JTree(treemodel);

		// Do not display the root node
		// tree.setRootVisible(false);
		tree.setScrollsOnExpand(true);
		tree.setRootVisible(false);
		tree.setExpandsSelectedPaths(true);

		// create the customized renderer for the cells
		// so we have the checkbox and the progress bars in the cells/nodes
		MyTreeCellRenderer myTreeCellRenderer = new MyTreeCellRenderer();
		tree.setCellRenderer(myTreeCellRenderer);
		tree.addMouseListener(new NodeSelectionListener(tree));
		tree.setRowHeight(24);
		// expand all nodes
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}

		// set the scrollpane view to the tree
		setViewportView(tree);
	}

	/**
	 * @param root
	 *            the tree root node
	 */
	private void processRoot(DownloadTreeNode root) {
		// get all childs
		List nl = xml_root.getChildNodes();
		for (Iterator iter = nl.iterator(); iter.hasNext();) {
			XmlNode node = (XmlNode) iter.next();
			DownloadItem di = new DownloadItem(node);
			if (node.getId().equals("file")) {
				di.setDownloadable(true);
			}
			DownloadTreeNode treeNode = new DownloadTreeNode(di);
			if (!node.getChildNodes().isEmpty()) {
				processChilds(node, treeNode);
			}
			root.add(treeNode);
		}
	}

	/**
	 * @param node
	 *            a node in the xml
	 * @param treeNode
	 *            a node in the tree
	 */
	private void processChilds(XmlNode node, MutableTreeNode treeNode) {
		int counter = 0;
		List nl = node.getChildNodes();
		for (Iterator iter = nl.iterator(); iter.hasNext();) {
			XmlNode subnode = (XmlNode) iter.next();
			if (!subnode.getId().equals("file") //$NON-NLS-1$
					&& !subnode.getId().equals("folder")) //$NON-NLS-1$
				continue;

			DownloadItem di = new DownloadItem(subnode);
			if (subnode.getId().equals("file")) //$NON-NLS-1$
			{
				di.setDownloadable(true);
			}

			DownloadTreeNode subtreenode = new DownloadTreeNode(di);
			treeNode.insert(subtreenode, counter++);
			if (!subnode.getChildNodes().isEmpty()) {
				processChilds(subnode, subtreenode);
			}

		}

	}

	/**
	 * return the content of a tag
	 */
	public String getNodeValue(XmlNode node) {
		// StringBuffer buf = new StringBuffer();
		// Hashtable children = node.getChildNodes();
		// for (int i = 0; i < children.getLength(); i++) {
		// Node textChild = children.item(i);
		// /*
		// * if (textChild.getNodeType() != Node.TEXT_NODE) {
		// * System.err.println("Mixed content! Skipping child element " +
		// * textChild.getNodeName()); continue; }
		// */
		// buf.append(textChild.getNodeValue());
		// }
		// return buf.toString();
		return node.getText();
	}

	/**
	 * Scrolls the tree to the given row
	 */
	public void scrollTo(DownloadTreeNode node) {
		tree.scrollPathToVisible(new TreePath(node.getPath()));
	}

	/**
	 * @return true if explorer has been initialized
	 */
	public boolean isInited() {
		return flagInited;
	}

}
