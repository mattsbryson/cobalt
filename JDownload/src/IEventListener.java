/* $Id: IEventListener.java,v 1.1 2005/01/22 14:09:15 mhaller Exp $
 * Project biz.jupload
 * (c) Mike Haller, Patrick Haller
 *
 * Created on 16.10.2004 00:53:35 by Patrick
 * Package package biz.jupload.engine;
 *
 */


/**
 * @author Patrick
 * 
 * Generic synchronous event listener interface.
 */
public interface IEventListener {
    /**
     * Event code DETACH is sent whenever the listener is removed from the
     * notifier
     */
    public final static int DETACH = 0;

    public final static int ATTACH = 1;

    public final static int APPLET_INITIALIZED = 2;

    public final static int APPLET_WAITING = 3;

    public final static int DOWNLOADITEM_QUEUESTATUS_CHANGED = 4;

    public final static int TARGETPATH_CHANGED = 5;

    public final static int DOWNLOAD_STARTING = 10;

    public final static int DOWNLOAD_PROGRESS = 11;

    public final static int DOWNLOAD_FINISHED = 12;

    public final static int DOWNLOAD_STOPPED = 13;

    public final static int DOWNLOADITEM_STARTING = 20;

    public final static int DOWNLOADITEM_PROGRESS = 21;

    public final static int DOWNLOADITEM_FINISHED = 22;

    public final static int DOWNLOADITEM_STOPPED = 23;

    public final static int DOWNLOADITEM_UNCOMPRESS_STARTING = 30;

    public final static int DOWNLOADITEM_UNCOMPRESS_PROGRESS = 31;

    public final static int DOWNLOADITEM_UNCOMPRESS_FINISHED = 32;

    public final static int DOWNLOADITEM_UNCOMPRESS_STOPPED = 33;

    /**
     * The interface for receiving events from the system
     * 
     * @param t_Event
     *            the event code
     * @param t_arrObj
     *            the parameter array
     */
    public void receiveEvent(final int t_Event, final Object[] t_arrObj);
}

/*
 * History: $Log: IEventListener.java,v $
 * History: Revision 1.1  2005/01/22 14:09:15  mhaller
 * History: v0.4, new feature: LiveConnect API and events being sent to javascript
 * History: Revision 1.1.2.1 2005/01/09 03:05:37
 * phaller Maven port
 * 
 * Revision 1.1.2.3 2004/11/03 17:16:40 phaller Added FileScanner and
 * FileScannerFilter. Corrected (C) comments.
 * 
 * Revision 1.1.2.2 2004/10/23 00:49:43 phaller Package name refactoring
 * 
 * Revision 1.1.2.1 2004/10/22 18:00:03 phaller JUploadPro branch
 * 
 */