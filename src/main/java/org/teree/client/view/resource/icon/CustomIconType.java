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
package org.teree.client.view.resource.icon;

import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.google.gwt.core.client.GWT;

public enum CustomIconType implements BaseIconType {

    connector;

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
