package org.teree.client.view.resource.icon;

import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface CustomIcons extends ClientBundle {

    @CssResource.NotStrict
    @Source("customIcons.css")
    CssResource css();

    @Source("connector.png")
    ImageResource connector();
}