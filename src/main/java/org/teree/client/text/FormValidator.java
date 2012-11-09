package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface FormValidator extends Messages{
    
    public static FormValidator LANG = GWT.create(FormValidator.class);

    String username_short();
    String username_noletter();
    String username_bad_chars();
    
    String name_short();
    String name_bad_chars();
    
    String email_not_valid();
    
    String password_short();
    String password_confirmation();
    
}
