package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface UIConstants extends Messages {
    
    public static UIConstants LANG = GWT.create(UIConstants.class);

    String attention();
    String change_logs();
    String features();
    String bug_tracking();
    String teree_details();
    String technical();
    String contact_and_feedback();
    
    String create();
    String explore();
    String help();
    String join();
    String settings();
    String logout();
    String set_language();
    
    String remove_scheme();
    String set_permissions();
    String edit();
    String view();
    String next();
    String back();
    
    String register_user();
    String username();
    String name();
    String password();
    String confirm_password();
    String change_password();
    String old_password();
    String email();
    String register();
    String clear();
    String sign_in();
    String update();
    String reset();
    String change();
    
    String profile();
    String account_settings();
    String delete_account();
    String joined_on();
    String import_from();
    
    String export_as();
    String freemind_map();
    String image();
    
}
