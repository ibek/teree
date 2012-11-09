package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Join extends Messages{
    
    public static Join LANG = GWT.create(Join.class);

    String register_user();
    String required();
    String username();
    String name();
    String password();
    String confirm_password();
    String email();
    String register();
    String clear();
    
}
