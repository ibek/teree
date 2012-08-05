package org.teree.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Viewer extends Composite {

    private static ViewerUiBinder uiBinder = GWT.create(ViewerUiBinder.class);

    interface ViewerUiBinder extends UiBinder<Widget, Viewer> {
    }

}
