package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Header extends Messages{
    
    public static Header LANG = GWT.create(Header.class);

    String create();
    String explore();
    String help();
    
}
