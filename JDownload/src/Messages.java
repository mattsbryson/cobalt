// $Id: Messages.java,v 1.1 2005/01/21 21:22:38 mhaller Exp $



import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Reads i18n messages from messages.properties
 * 
 * @author Mike Haller
 * @since 21.01.2005
 */
public class Messages {
    private static final String BUNDLE_NAME = "biz.jupload.jdownload.messages";//$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Messages() {
        // Do not instantiate this class
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
