package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Settings extends Messages{
    
    public static Settings LANG = GWT.create(Settings.class);

    String profile();
    String name();
    String email();
    String update();
    String reset();
    
    String account_settings();
    String change_password();
    String old_password();
    String password();
    String confirm_password();
    String change();
    
    String delete_account();
    
}
