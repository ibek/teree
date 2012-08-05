package org.teree.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public class Text {
    
    private static Text.Type tt;
    
    private Text(){}
    
    public interface Type extends Messages {

        String mapReceived(String oid);
        
        String mapUpdated();
        
        String mapSaved(String oid);
        
    }
    
    public static Text.Type produceTextTypes() {
        if(tt == null){
            tt = GWT.create(Text.Type.class);
        }
        return tt;
    }
    
}
