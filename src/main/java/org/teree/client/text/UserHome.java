package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface UserHome extends Messages{
    
    public static UserHome LANG = GWT.create(UserHome.class);

    String joined_on();
    String import_from();
    
}
