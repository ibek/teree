package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface UserWidget extends Messages{
    
    public static UserWidget LANG = GWT.create(UserWidget.class);

    String sign_in();
    String join();
    String settings();
    String logout();
    String set_language();
    
}
