package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface PrivateHome extends Messages{
    
    public static PrivateHome LANG = GWT.create(PrivateHome.class);

    String public_schemes();
    String private_schemes();
    String joined_on();
    String import_from();
    
}
