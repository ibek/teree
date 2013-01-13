package org.teree.client.view.resource.icon;

import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.google.gwt.core.client.GWT;

public enum CustomIconType implements BaseIconType {

    connector,
    mindmap,
    hierarchicalhorizontal;

    static {
        CustomIcons icons = GWT.create(CustomIcons.class);
        icons.css().ensureInjected();
    }

    private static final String PREFIX = "customIcon_";
    private String className;

    private CustomIconType() {
        this.className = this.name().toLowerCase();
    }
    
    @Override public String get() {
        return PREFIX + className;
    }

}
