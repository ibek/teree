package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface ViewPanel extends Messages{
    
    public static ViewPanel LANG = GWT.create(ViewPanel.class);

    String export_as();
    String image();
    String freemind_map();
    
}
