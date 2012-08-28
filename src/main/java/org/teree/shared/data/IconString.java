package org.teree.shared.data;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class IconString {

    private String text;
    private Integer iconid;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Integer getIconid() {
        return iconid;
    }
    
    public void setIconid(Integer iconid) {
        this.iconid = iconid;
    }
    
}
