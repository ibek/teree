package org.teree.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public class Text {
    
    public static Text.Type LANG = GWT.create(Text.Type.class);
    
    private Text(){}
    
    public interface Type extends Messages {

        String mapReceived(String oid);
        
        String mapUpdated(String oid);
        
        String mapCreated(String oid);
        
    }
    
}
