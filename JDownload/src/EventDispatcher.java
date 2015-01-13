/* $Id: EventDispatcher.java,v 1.1 2005/01/22 14:09:16 mhaller Exp $
 * Project biz.jupload
 * (c) Mike Haller, Patrick Haller
 *
 * Created on 16.10.2004 00:45:01 by Patrick
 * Package package biz.jupload.engine;
 *
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.logging.Logger;

/**
 * @author Patrick
 * 
 * Generic event dispatcher.
 */
public class EventDispatcher
{

	//private static final Logger log = Logger.getAnonymousLogger();
	
	/** the list of subscribed listeners */
    private List            m_listListeners;


    /**
     * EventDispatcher constructor
     */
    public EventDispatcher()
    {
        super();

        m_listListeners = new ArrayList();
    }

    /**
     * Add a listener. The listener is sent an ATTACH message.
     * 
     * @param t_Listener
     *            the listener to add
     */
    public synchronized void addListener( final IEventListener t_Listener )
    {
        m_listListeners.add( t_Listener );
        fireEvent( t_Listener, IEventListener.ATTACH, null );
    }

    /**
     * Remove the specified listener. The listener is sent a DETACH message.
     * 
     * @param t_Listener
     *            the listener to remove
     * @return true if successful, false if not
     */
    public synchronized boolean removeListener( final IEventListener t_Listener )
    {
        fireEvent( t_Listener, IEventListener.DETACH, null );
        return m_listListeners.remove( t_Listener );
    }

    /**
     * Remove all listeners. Just before removing, the listener is sent a DETACH
     * message.
     */
    public synchronized void removeAllListeners()
    {
        fireEvent( IEventListener.DETACH, null );
        m_listListeners.clear();
    }

    /**
     * Fire an event and send it down to all registered listeners
     * 
     * @param t_Event
     *            the event code
     * @param t_arrObj
     *            the parameter array
     */
    public void fireEvent( final int t_Event, final Object[] t_arrObj )
    {
        Iterator a_iter = m_listListeners.iterator();
        while( a_iter.hasNext() )
        {
            IEventListener a_Listener = (IEventListener)a_iter.next();
            //log.fine("Sending event "+t_Event+" to "+a_Listener); //$NON-NLS-1$ //$NON-NLS-2$
            fireEvent( a_Listener, t_Event, t_arrObj );
        }
    }

    /**
     * Fire an event and send it down to the specified listener
     * 
     * @param t_Listener
     *            the listener to send the event to
     * @param t_Event
     *            the event code
     * @param t_arrObj
     *            the parameter array
     */
    public void fireEvent( final IEventListener t_Listener,
                          final int t_Event,
                          final Object[] t_arrObj )
    {
        t_Listener.receiveEvent( t_Event, t_arrObj );
    }

}

/*
 * History: $Log: EventDispatcher.java,v $
 * History: Revision 1.1  2005/01/22 14:09:16  mhaller
 * History: v0.4, new feature: LiveConnect API and events being sent to javascript
 * History:
 * History: Revision 1.1.2.1  2005/01/09 03:05:37  phaller
 * History: Maven port
 * History:
 * History: Revision 1.1.2.3  2004/11/03 17:16:40  phaller
 * History: Added FileScanner and FileScannerFilter. Corrected (C) comments.
 * History:
 * History: Revision 1.1.2.2  2004/10/23 00:49:43  phaller
 * History: Package name refactoring
 * History:
 * History: Revision 1.1.2.1  2004/10/22 18:00:03  phaller
 * History: JUploadPro branch
 * History:
 */