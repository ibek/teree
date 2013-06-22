/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface UIMessages extends Messages {
    
    public static UIMessages LANG = GWT.create(UIMessages.class);
    
    String subtitle();

    String schemeReceived(String oid);
    String schemeUpdated(String oid);
    String schemeCreated(String oid);
    String schemeRemoved(String oid);
    
    String loginFailed();
    String connectionIssue();
    String cannot_export_scheme();
    
    String attention_testing();
    String new1();
    String new2();
    
    String feature1();
    String feature2();
    String feature3();
    String feature4();
    String feature5();
    String feature6();
    
    String technical_detail1();
    String technical_detail2();
    String technical_detail3();
    
    String no_scheme();
    
    String username_short();
    String username_noletter();
    String username_bad_chars();
    String name_short();
    String name_bad_chars();
    String email_not_valid();
    String password_short();
    String password_confirmation();
    String all_fields_required();

    String create_text();
    String create_image();
    String create_link();
    String create_math();
    String bold_text();
    String choose_icon();
    
}
