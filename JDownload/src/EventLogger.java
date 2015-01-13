

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
//import java.util.logging.Logger;

public class EventLogger implements IEventListener {

	//private static final Logger log = Logger.getAnonymousLogger();

	public EventLogger() {
		super();
		//log.setLevel(Level.FINE);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.FINE);
		//log.addHandler(consoleHandler);
	}

	public void receiveEvent(int t_Event, Object[] t_arrObj) {
		
		if (t_Event == IEventListener.DOWNLOADITEM_FINISHED) {
			String id = (String) t_arrObj[1];
			//log.log(Level.FINE, "Event 'Download Finished' for id "+id);
		}
		
		if (t_arrObj != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < t_arrObj.length; i++) {
				Object object = t_arrObj[i];
				sb.append(" " + i + "=" + object + "\n");
			}
			//log.log(Level.FINE, "Event " + t_Event + " Parameters:  ["
			//		+ sb.toString() + "]");
		} else {
			//log.log(Level.FINE, "Event " + t_Event + " without Parameters");
		}
	}

}
