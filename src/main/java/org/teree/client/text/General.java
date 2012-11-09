package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface General extends Messages {
    
    public static General LANG = GWT.create(General.class);

    String schemeReceived(String oid);
    String schemeUpdated(String oid);
    String schemeCreated(String oid);
    String schemePublished(String oid);
    String schemeRemoved(String oid);
    
    String loginFailed();
    String connectionIssue();
    
}
