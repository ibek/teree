package org.teree.client.view.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface NodeCssStyle extends ClientBundle {
	
	public final static NodeCssStyle INSTANCE = GWT.create(NodeCssStyle.class);
	
	@Source("nodeStyle.css")
	NodeStyleCssResource css();

	
	public interface NodeStyleCssResource extends CssResource {
		
	    String node();

	    @ClassName("view")
	    String nodeView();
	    
	    @ClassName("edit")
	    String nodeEdit();
	}
	
}
