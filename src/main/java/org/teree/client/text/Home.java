package org.teree.client.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Home extends Messages{
    
    public static Home LANG = GWT.create(Home.class);

    String subtitle();
    
    String attention();
    String attention_testing();
    
    String features();
    String feature1();
    String feature2();
    String feature3();
    String feature4();
    String feature5();
    
    String bug_tracking();
    
    String teree_details();
    String technical();
    String technical_detail1();
    String technical_detail2();
    String technical_detail3();
    
    String contact_and_feedback();
    
}
