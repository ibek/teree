package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface ChangeLogs extends Messages{
    
    public static ChangeLogs LANG = GWT.create(ChangeLogs.class);

    String change_logs();
    
}
