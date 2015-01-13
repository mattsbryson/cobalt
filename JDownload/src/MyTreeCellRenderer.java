// $Id: MyTreeCellRenderer.java,v 1.5 2005/03/22 14:42:24 mhaller Exp $



import java.awt.Component;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 * Renders the cells in the tree
 * 
 * @author Mike Haller
 * @since 27.01.2004 01:26:19
 */
public class MyTreeCellRenderer implements TreeCellRenderer {

	// this is the main panel which holds the label, the checkbox and the
	// progress bar
	CellComponent panel = null;

	// constructor for this renderer
	MyTreeCellRenderer() {
		// NOP
	}

	/**
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
	 *      java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		DownloadTreeNode node = (DownloadTreeNode) value;
		Object userObject = node.getUserObject();

		// Debug.println("cellrenderer: value=" + value.getClass());
		if (userObject instanceof DownloadItem) {
			DownloadItem item = (DownloadItem) userObject;

			// get the cell component of the download item (that's the thing
			// this the label and the checkbox and the progress bar
			if (!item.hasCellComponent()) {
				// if it hasn't any component yet, create one
				item.setCellComponent(new CellComponent());
			}
			panel = item.getCellComponent();

			if (item.isDownloadable()) {
				panel.getLabel()
						.setIcon(UIManager.getIcon("FileView.fileIcon"));
				panel.getLabel().setText(item.toString());
				panel.getProgressBar().setVisible(true);
				panel.getCheckBox().setVisible(true);
				// give him a checkbox item
				panel.getCheckBox().setSelected(node.isSelected());

				// set the background color, if it's selected / focused
				if (selected) {
					panel.getLabel().setBackground(
							UIManager.getColor("Tree.selectionBackground"));
					panel.getLabel().setForeground(
							UIManager.getColor("Tree.selectionForeground"));
				} else {
					panel.getLabel().setBackground(
							UIManager.getColor("Tree.textBackground"));
					panel.getLabel().setForeground(
							UIManager.getColor("Tree.textForeground"));
				}

				// give him the panel to draw
				return panel;
			}

			panel.getLabel().setIcon(
					UIManager.getIcon("FileView.directoryIcon"));

			// Return just a text
			// e.g. it's not a downloadable item (e.g. a album name)
			panel.getProgressBar().setVisible(false);
			panel.getCheckBox().setVisible(false);
			panel.getLabel().setVisible(true);
			panel.getLabel().setText(item.toString());

			if (selected) {
				panel.getLabel().setBackground(
						UIManager.getColor("Tree.selectionBackground")); //$NON-NLS-1$
				panel.getLabel().setForeground(
						UIManager.getColor("Tree.selectionForeground"));
			} else {
				panel.getLabel().setBackground(
						UIManager.getColor("Tree.textBackground")); //$NON-NLS-1$
				panel.getLabel().setForeground(
						UIManager.getColor("Tree.textForeground"));
			}

			return panel;

		}

		return null;
	}

}
