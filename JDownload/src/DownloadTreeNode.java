// $Id: DownloadTreeNode.java,v 1.4 2005/03/22 14:42:23 mhaller Exp $



import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DownloadTreeNode
 * 
 * @author Mike Haller
 * @since 27.01.2004 01:53:34
 */
class DownloadTreeNode extends DefaultMutableTreeNode {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3257001060081349937L;
	private boolean selected;

    /**
     * create a new node element (a single item of the tree) which has
     * optionally a DownloadItem
     * 
     * @param userObject
     */
    public DownloadTreeNode(Object userObject) {
        super(userObject);
        if (userObject instanceof DownloadItem) {
            DownloadItem dItem = (DownloadItem) userObject;
            if (dItem.isDownloadable()) {
                selected = true;
                dItem.setQueued(isSelected());
            }
        }
    }

    /**
     * returns if the node has been marked with the checkbox
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * set the mark of the checkbox
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * switch on or off the mark of the checkbox
     */
    public void toggle() {
        setSelected(!isSelected());

        if (userObject instanceof DownloadItem) {
            ((DownloadItem) userObject).setQueued(isSelected());
            Manager.getInstance().getEventDispatcher().fireEvent(IEventListener.DOWNLOADITEM_QUEUESTATUS_CHANGED,new Object[]{
                    userObject,
                    new Boolean(isSelected())
            });

        }
    }

}
