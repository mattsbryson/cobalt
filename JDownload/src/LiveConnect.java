/* $Id: LiveConnect.java,v 1.5 2005/04/02 07:56:31 mhaller Exp $
 * 
 * Project biz.jupload
 * (c) Mike Haller, Patrick Haller
 *
 * Created on 16.10.2004 00:33:50 by Patrick
 * Package package biz.jupload.engine.liveConnect;
 *
 * Copied from JUploadPro to JDownload and merged changes
 */


import java.util.logging.Level;
//import java.util.logging.Logger;

import netscape.javascript.JSObject;

/**
 * Encapsulates the applet's JavaScript call interface.
 * 
 * @author Patrick Haller
 */
public class LiveConnect implements IEventListener {

	//private static final Logger log = Logger.getAnonymousLogger();
	
    private JSObject jsWin;

    /**
     * LiveConnect constructor
     */
    public LiveConnect() {
        super();
        initialize();
    }

    /**
     * Initialize the LiveConnect component
     */
    public void initialize() {
        // get StartupApplet reference from the Environment
        try {
            final Manager a_StartupApplet = Manager.getInstance();
            jsWin = JSObject.getWindow(a_StartupApplet);
            // attach to the system event dispatcher
            Manager.getInstance().getEventDispatcher().addListener(this);
        } catch (Exception e) {
            String msg = "Exception while initializing JavaScript LiveConnect";
			//log.log(Level.WARNING, msg, e);
        } catch (Error e) {
            String msg = "Error initializing JavaScript LiveConnect";
			//log.log(Level.WARNING, msg, e);
        }
    }

    /**
     * Terminate the LiveConnect component
     */
    public void terminate() {
        // detach from the system event dispatcher
        Manager.getInstance().getEventDispatcher().removeListener(this);
    }

    /**
     * Receive an event from the listener interface
     * 
     * @param t_Event
     *            the received event code
     * @param t_arrObj
     *            array of parameter objects
     * 
     * @see biz.jupload.jdownload.IEventListener#receiveEvent(int,
     *      java.lang.Object[])
     */
    public void receiveEvent(int t_Event, Object[] t_arrObj) {
        try {
            if (jsWin != null) {
                Object[] a_newArray = new Object[3];
                a_newArray[0] = new Integer(t_Event);
                a_newArray[1] = t_arrObj;
                a_newArray[2] = arr2string(t_arrObj);
                jsWin.call("onJDownloadEvent", a_newArray); //$NON-NLS-1$
            }
        } catch (Exception e) {
            // NOP
        }
    }

    /**
     * Converts the given array into a string, each element is toString()-ed
     * and separated with a pipe symbol ("|").
     * 
     * @param obj an array with elements
     * @return a string representation
     */
    private String arr2string(Object[] obj) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obj.length; i++) {
            if (i>0) sb.append("|"); //$NON-NLS-1$
            Object object = obj[i];
            sb.append(object.toString());
        }
        return sb.toString();
    }

    /**
     * Query the current document cookie from the browser window
     * 
     * @return the cookie string or <code>null</code>
     */
    public String getDocumentCookie() {
		if (jsWin==null) return "";
        String r_strCookieString = jsWin.eval("document.cookie").toString(); //$NON-NLS-1$
        return r_strCookieString;
    }
}
