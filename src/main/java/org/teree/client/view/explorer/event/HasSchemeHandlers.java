package org.teree.client.view.explorer.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasSchemeHandlers extends HasHandlers {

	  HandlerRegistration addPublishHandler(PublishSchemeHandler handler);

	  HandlerRegistration addRemoveHandler(RemoveSchemeHandler handler);
	  
}
