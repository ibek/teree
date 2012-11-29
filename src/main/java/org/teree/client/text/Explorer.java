package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Explorer extends Messages{
    
    public static Explorer LANG = GWT.create(Explorer.class);

    String publish();
    String remove();
    String permissions();
    String edit();
    String view();
    String next();
    String back();
    String no_scheme();
    
}
