// $Id: HeaderPanel.java,v 1.5 2005/03/22 14:42:24 mhaller Exp $



import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.AccessControlException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import netscape.javascript.JSObject;

/**
 * HeaderPanel
 * 
 * @author Mike Haller
 * @since 27.01.2004 02:59:31
 */
public class HeaderPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3257848792169590841L;

	JFileChooser jfc;

	JLabel lbl1;
        
        JLabel lbl2;

	JButton btn1;

	JTextField edt1;

	HeaderPanel() {
		// set the colors
		setBackground(Manager.bgColor);
		setForeground(Manager.fgColor);
		BoxLayout bl = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(bl);

		// create the file/folder chooser
		try {
			jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} catch (AccessControlException e) {
			System.err.println("*** ERROR: APPLET NOT SIGNED, OR USER DID NOT ACCEPT SIGNATURE ***"); //$NON-NLS-1$
		}

		// create the labels and buttons
		lbl1 = new JLabel("Save to folder: "); //$NON-NLS-1$
		lbl1.setForeground(Manager.fgColor);
                
 		//lbl2 = new JLabel("\b\b\b"); //$NON-NLS-1$
		//lbl2.setForeground(Manager.fgColor);
                
		edt1 = new JTextField();
		btn1 = new JButton("Browse"); //$NON-NLS-1$
                btn1.setAlignmentX(LEFT_ALIGNMENT);
                
		// Copy the current working folder to the edit field.
		if (jfc != null)
                {
                        JSObject window = JSObject.getWindow(Manager.getInstance());
                        String savePath = (String)window.call("getLocalPath", new Object[] {});
                        if(savePath != "")
                        {
                            edt1.setText(savePath);
                            File currentFolder = new File(savePath);
                            jfc.setCurrentDirectory(currentFolder);
                        }
                        else
                        {
                            edt1.setText(jfc.getCurrentDirectory().getPath());
                        }
                }
		// make the edit field read-only
		edt1.setEditable(false);
		// set their sizes
		// lbl1.setPreferredSize(new Dimension(150,20));
		// btn1.setPreferredSize(new Dimension(100,20));
		// edt1.setPreferredSize(new Dimension(300,70));
		// add them to the panel
		add(lbl1);
		add(edt1);
                //add(lbl2);
		add(btn1);
		// setPreferredSize(new Dimension(0, 70));
		// enable reacting to mouse click
		btn1.addActionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (jfc == null)
			return;

		// Someone clicked the "Change" button
		jfc.showDialog(getParent(), "OK"); //$NON-NLS-1$
		// get the selected folder
		File selected = jfc.getSelectedFile();
		// copy it to the edit field
		if (null != selected) {
			Manager.getInstance().getEventDispatcher().fireEvent(
					IEventListener.TARGETPATH_CHANGED,
					new Object[] { edt1.getText(), selected.getPath() });
			edt1.setText(selected.getPath());
                        
                        JSObject window = JSObject.getWindow(Manager.getInstance());
                        //window.call("setLocalPath", new Object[] {edt1.getText()});
                        window.eval("setLocalPath(\""+selected.getPath().replace("\\", "/")+"\")");
		}
	}

	/**
	 * Get the folder the user has choosen to download his files to
	 * 
	 * @return the target location as File
	 */
	public File getTargetLocation() {
		File f = new File(edt1.getText());
		return f;
	}
}
