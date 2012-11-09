package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Login extends Messages{
    
    public static Login LANG = GWT.create(Login.class);

    String username();
    String password();
    String sign_in();
    
}
