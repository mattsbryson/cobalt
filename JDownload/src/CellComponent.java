// $Id: CellComponent.java,v 1.6 2005/03/22 14:42:24 mhaller Exp $



import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * CellComponent
 * 
 * @author Mike Haller
 * @since 04.02.2004 13:42:49
 */
public class CellComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3257565109562914609L;

	private JLabel label;

	private JProgressBar progressbar;

	private JCheckBox checkbox;

	CellComponent() {
		checkbox = new JCheckBox();
		checkbox.setBorder(new EmptyBorder(0, 0, 0, 0));
		label = new JLabel();
		label.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressbar = new JProgressBar();
		// progressbar.setBorder(new EmptyBorder(0,0,0,0));
		int height = 15;
		int width = 130; // 30 pixel
		progressbar.setPreferredSize(new Dimension(width, height));
		// make the label non-transparent (so we can see the selection)
		// setOpaque(true);
		progressbar.setOpaque(false);
		checkbox.setOpaque(false);
		label.setOpaque(true);
		setOpaque(false);
		add(checkbox);
		add(label);
		add(progressbar);
	}

	/**
	 * @return the label of this cell component
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * @return the JProgressBar of this cell component
	 */
	public JProgressBar getProgressBar() {
		return progressbar;
	}

	/**
	 * @return the checkbox of this cell component
	 */
	public JCheckBox getCheckBox() {
		return checkbox;
	}
}
