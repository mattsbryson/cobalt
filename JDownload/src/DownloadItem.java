// $Id: DownloadItem.java,v 1.6 2005/03/22 14:42:24 mhaller Exp $



import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import biz.jupload.jdownload.xml.XmlNode;

/**
 * DownloadItem
 * 
 * @author Mike Haller
 * @since 27.01.2004 01:30:09
 */
public class DownloadItem {
	private CellComponent cellComponent;

	private boolean downloadable = false;

	private boolean queued = true;

	private XmlNode node = null;

	private File outputfile;

	/**
	 * @param node
	 *            an xml node
	 */
	public DownloadItem(XmlNode node) {
		this.node = node;
	}

	/**
	 * returns if the DownloadItem is downloadable (has an URL)
	 * 
	 * @return true if download item is downloadable (a file), false if not
	 *         (folder)
	 */
	public boolean isDownloadable() {
		return downloadable;
	}

	/**
	 * Queries if a cellComponent has already been set, if so, returns true. If
	 * there is no cell component set (see setCellComponent()), false
	 * 
	 * @return true if the download item has its own cell component
	 */
	public boolean hasCellComponent() {
		if (cellComponent == null)
			return false;
		return true;
	}

	/**
	 * Sets the cell component for this downloaditem so we can get the progress
	 * bar and the other components while downloading
	 */
	public void setCellComponent(CellComponent panel) {
		cellComponent = panel;
	}

	/**
	 * @return the cell component for this download item, so we can access the
	 *         progress bar
	 */
	public CellComponent getCellComponent() {
		return cellComponent;
	}

	/**
	 * returns whether this downloadable item has been queued for download or
	 * not
	 * 
	 * @return true, if the file checkbox is checked (file is queued)
	 */
	public boolean isQueued() {
		return queued;
	}

	/**
	 * Sets whether the file is queued for download or not
	 * 
	 * @param flag
	 *            the new queueing status of the downloadable item
	 */
	public void setQueued(boolean flag) {
		queued = flag;
	}

	/**
	 * @return the URL of the download item
	 */
	public URL getURL() {
		if (node == null)
			return null;

		List childNodes = node.getChildNodes();
		XmlNode child = (XmlNode) childNodes.get(0);

		try {
			URL url = new URL(child.getText());
			return url;
		} catch (MalformedURLException e) {
			// e.printStackTrace();
			try {
				URL url = new URL(Manager.getInstance().getCodeBase()
						+ child.getText());
				return url;
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (node == null || node.getLabel() == null)
			return "Downloads"; //$NON-NLS-1$
		return node.getLabel();
	}

	/**
	 * @return the filename of the Download Item
	 */
	public String getFilename() {
		return toString();
	}

	/**
	 * @return the paths of the parent folders
	 */
	public String getPaths() {
		if (node == null)
			return null;
		StringBuffer sb = new StringBuffer();
		XmlNode parent = node.getParentNode();
		while (parent != null) {
			String parentName = parent.getLabel();
			if (parentName != null) {
				sb.insert(0, parentName + File.separator);
			}
			parent = parent.getParentNode();
		}
		return sb.toString();
	}

	/**
	 * @param outputfile
	 *            the new target output file
	 */
	public void setOutputfile(File outputfile) {
		this.outputfile = outputfile;
	}

	/**
	 * @return true, if the zip archive should be uncompressed after download
	 */
	public boolean doUncompressAfterDownload() {
		if (node == null)
			return false;
		if (node.getAttributes() == null)
			return false;
		if (node.getAttributes().get("uncompress") == null) //$NON-NLS-1$
			return false;
		String uncompress = (String) node.getAttributes().get("uncompress"); //$NON-NLS-1$
		if (uncompress.equals("true")) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * @return the target output file
	 */
	public File getOutputfile() {
		return outputfile;
	}

	/**
	 * @param flag
	 *            sets the download item to be downloadable (true)
	 */
	public void setDownloadable(boolean flag) {
		downloadable = flag;
	}

	/**
	 * @return true if file should be deleted after it has been uncompressed.
	 */
	public boolean doDeleteAfterUncompress() {
		if (node == null)
			return false;
		if (node.getAttributes() == null)
			return false;
		if (node.getAttributes().get("delete") == null) //$NON-NLS-1$
			return false;
		String delete = (String) node.getAttributes().get("delete"); //$NON-NLS-1$
		if (delete.equalsIgnoreCase("true")) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * Returns the id-Attribute of the xml node. Used to reference the
	 * DownloadItem by id instead by filename
	 * 
	 * @return the value of the id-Attribute or an empty string if it does not
	 *         have an id attribute
	 */
	public String getId() {
		if (node == null || node.getAttributes() == null
				|| !node.getAttributes().containsKey("id")) {
			return "";
		}
		return (String) node.getAttributes().get("id");
	}

	/**
	 * Returns true if the download item has an attribute "forceOverwrite" with
	 * the value "true" (case insensitive). If set to true (default is false),
	 * the file will be deleted if it exists prior to downloading it.
	 * 
	 * @return true if the file should be overwritten
	 */
	public boolean doForceOverwrite() {
		if (node == null)
			return false;
		if (node.getAttributes() == null)
			return false;
		if (node.getAttributes().get("forceOverwrite") == null) //$NON-NLS-1$
			return false;
		String delete = (String) node.getAttributes().get("forceOverwrite"); //$NON-NLS-1$
		if (delete.equalsIgnoreCase("true")) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
}
