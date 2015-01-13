// $Id: Manager.java,v 1.9 2005/04/02 07:56:31 mhaller Exp $



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.security.AccessController;
import java.security.PrivilegedAction;
//import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import netscape.javascript.JSObject;


/**
 * JDownload main Manager class
 * 
 * @author Mike Haller
 * @since 27.01.2004 00:14:14
 */
public class Manager extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3257854272614708279L;

	//private static final Logger log = Logger.getAnonymousLogger();
	
	// global color definitions
	public static Color bgColor = Color.white;

	public static Color fgColor = Color.black;

	// the Singleton
	private static Manager instance;

	Explorer explorer;

	ControlPanel controlpanel;

	HeaderPanel header;

	private JLabel stat;

	private EventDispatcher eventDispatcher;

	public static Manager getInstance() {
		return instance;
	}

	public Explorer getExplorer() {
		return explorer;
	}

	public ControlPanel getControlPanel() {
		return controlpanel;
	}

	public HeaderPanel getHeaderPanel() {
		return header;
	}

	/**
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		super.init();

		// Display the name of the application
		String infoString = getAppletInfo() + " running at " + getCodeBase(); //$NON-NLS-1$
		System.out.println(infoString);
		showStatus(infoString);

		// save the instance
		instance = this;

		eventDispatcher = new EventDispatcher();

		// Initialize LiveConnect bridge to JavaScript
		new LiveConnect();
		
		EventLogger logger = new EventLogger();
		getEventDispatcher().addListener(logger);

		LookAndFeel laf = new WindowsLookAndFeel();
		try {
			UIManager.setLookAndFeel(laf);
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		setBackground(Manager.bgColor);

		//initSkinLF();

		// Create the different panels
		controlpanel = new ControlPanel();
		header = new HeaderPanel();

		// Create the explorer
		explorer = new Explorer();

		// add them to the applet
		if (getParameter("showControls") == null || !getParameter("showControls").equalsIgnoreCase("false")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			getContentPane().add(controlpanel, BorderLayout.EAST);

		if (getParameter("showBrowser") == null || !getParameter("showBrowser").equalsIgnoreCase("false")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			getContentPane().add(header, BorderLayout.NORTH);

		// Create a simple status bar
		JPanel status = new JPanel();
		status.setBackground(Manager.bgColor);
		status.setForeground(Manager.fgColor);

		stat = new JLabel("Status: waiting"); //$NON-NLS-1$
		stat.setBackground(Manager.bgColor);
		stat.setForeground(Manager.fgColor);

		status.add(stat);
		if (getParameter("showStatus") == null || !getParameter("showStatus").equalsIgnoreCase("false")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			getContentPane().add(status, BorderLayout.SOUTH);

		// Add the main explorer (Tree) to the main window
		if (getParameter("showExplorer") == null || !getParameter("showExplorer").equalsIgnoreCase("false")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			getContentPane().add(explorer, BorderLayout.CENTER);

		explorer.setMinimumSize(new Dimension(400, 400));

                //explorer.setSize(400, 400);

		// set the maximum width of the left and right panels
		controlpanel.setPreferredSize(new Dimension(90, 480));

		doLayout();

		getEventDispatcher().fireEvent(IEventListener.APPLET_INITIALIZED, null);
	}

	/**
	 * Load the skinLF package, if possible
	 */
        
        /*
	private void initSkinLF() {
		// load the stuff for the Skinning technology
		try {
			Class skinLF = Class
					.forName("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"); //$NON-NLS-1$
			if (skinLF == null)
				return;

			try {
				// load the skin
				SkinLookAndFeel
						.setSkin(SkinLookAndFeel
								.loadThemePack(getCodeBase()
										+ Messages
												.getString("Manager.SkinLF.ThemepackURL"))); //$NON-NLS-1$

				// set it as new user interface look and feel
				UIManager
						.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"); //$NON-NLS-1$
			} catch (Exception e) {
				// This exception only means that the file could not be found
				// e.printStackTrace();
				//log.fine("Could not load skinlf theme."); //$NON-NLS-1$
			}
		} catch (ClassNotFoundException e) {
			//log.fine("Could not initialize skinlf."); //$NON-NLS-1$
		} catch (SecurityException se) {
			//log.fine("Could not initialize skinlf."); //$NON-NLS-1$
		}
		// update the graphical components so the new Look and Feel is displayed
		SwingUtilities.updateComponentTreeUI(this);
	}
        */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#start()
	 */
	public void start() {
		super.start();

		// Initialize the explorer after all other work is done
		if (!explorer.isInited()) {
			Runnable initExplorer = new Runnable() {
				public void run() {
					explorer.init();
				}
			};
			SwingUtilities.invokeLater(initExplorer);
		}

		// realign everything to fit the new settings
		doLayout();

		// De-Focus possible? Java >1.4
		// if (isFocusOwner()) {
		// Debug.println("Transfering focus..."); //$NON-NLS-1$
		// transferFocus();
		// }
	}

	/**
	 * Sets the text in the status line
	 * 
	 * @param newStatus
	 *            new message text in status bar
	 */
	public void setStatus(String newStatus) {
		if (stat != null)
			stat.setText(newStatus);
	}

	/**
	 * @see java.applet.Applet#getAppletInfo()
	 */
	public String getAppletInfo() {
		return "JDownload v0.9 (info@jupload.biz)"; //$NON-NLS-1$
	}

	/**
	 * Disable all components of the manager applet due to an error, so the user
	 * can't do anything and should restart.
	 */
	public void disableAll() {
		controlpanel.getButtonDownload().setEnabled(false);
		controlpanel.getButtonStop().setEnabled(false);
	}

	/**
	 * Returns the event dispatcher, who will send events generated by the
	 * applet to all registered listeners (the applet itself, javascript
	 * functions..)
	 * 
	 * @return the event dispatcher
	 */
	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	public void clickDownload() {
		//log.fine("Clicked 'Download' button"); //$NON-NLS-1$
		try {
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					controlpanel.getActionDownload().stopped = false;                                        
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickStop() {
		//log.fine("Clicked 'Stop' button"); //$NON-NLS-1$
		try {
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					controlpanel.getActionDownload().stopped = true;
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickBrowse() {
		//log.fine("Clicked 'Browse' button"); //$NON-NLS-1$
		try {
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					header.actionPerformed(null);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
