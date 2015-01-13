// $Id: ControlPanel.java,v 1.6 2005/03/22 14:42:24 mhaller Exp $


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * ControlPanel
 * 
 * @author Mike Haller
 * @since 27.01.2004 02:57:40
 */
public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3257571723896174135L;

	JButton btnDownload;

	JButton btnStop;

	BoxLayout bl;

	private ActionDownload actionDownload;

	ControlPanel() {

		// create the layout manager who aligns all the stuff in here
		bl = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(bl);
                
                add(Box.createVerticalStrut(5));

		// make the panel black and white
		setBackground(Manager.bgColor);
		setForeground(Manager.fgColor);

		// create the buttons
		btnDownload = new JButton("\bStart\b"); //$NON-NLS-1$
		btnDownload.setActionCommand("DOWNLOAD"); //$NON-NLS-1$
		btnStop = new JButton("\bStop!\b"); //$NON-NLS-1$
		btnStop.setEnabled(false);
		btnStop.setActionCommand("STOP"); //$NON-NLS-1$

		// make the buttons centered
		btnDownload.setAlignmentX(CENTER_ALIGNMENT);
		btnStop.setAlignmentX(CENTER_ALIGNMENT);

		// add the buttons to the panel
		add(btnDownload);
		add(Box.createVerticalStrut(5)); // Margin between the two buttons is
		// 5 pixel
		add(btnStop);

		// Add eventlistener, so something happens when we press the buttons
		actionDownload = new ActionDownload();
		btnDownload.addActionListener(actionDownload);
		btnStop.addActionListener(actionDownload);
	}

	/**
	 * @return the Stop-button
	 */
	public JButton getButtonStop() {
		return btnStop;
	}

	/**
	 * @return the Download-button
	 */
	public JButton getButtonDownload() {
		return btnDownload;
	}

	/**
	 * @return the action download instance
	 */
	public ActionDownload getActionDownload() {
		return actionDownload;
	}
}
