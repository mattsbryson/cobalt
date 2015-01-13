// $Id: NodeSelectionListener.java,v 1.3 2005/03/22 14:42:23 mhaller Exp $



import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JTree;

/**
 * NodeSelectionListener
 * 
 * @author Mike Haller <mike.haller@smartwerkz.com>
 */
class NodeSelectionListener extends JCheckBox implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3689631406360048953L;

	JTree tree;

	NodeSelectionListener(JTree tree) {
		this.tree = tree;
	}

	public void mouseEntered(MouseEvent e) {
		// NOP
	}

	public void mouseExited(MouseEvent e) {
		// NOP
	}

	public void mousePressed(MouseEvent e) {
		// NOP
	}

	public void mouseReleased(MouseEvent e) {
		// NOP
	}

	public void mouseClicked(MouseEvent me) {
		if (me.getSource().equals(tree)) {
			// if (me.getClickCount() == 3) {
			// which node was clicked?
			if (tree.getLastSelectedPathComponent() instanceof DownloadTreeNode) {
				DownloadTreeNode node = (DownloadTreeNode) tree
						.getLastSelectedPathComponent();
				// if it's an album, preview album information
				// toggle the node's selected-state
				if (me.getClickCount() == 2) {
					node.toggle();
				}
			}
			// ... and let the tree repaint itself
			tree.repaint();
			// }
		}
	}

}
