package org.teree.client.view.editor.event;

import com.google.gwt.event.shared.GwtEvent;

public class SelectNode<T> extends GwtEvent<SelectNodeHandler> {
    
    public static Type<SelectNodeHandler> TYPE = new Type<SelectNodeHandler>();

    private T node;
    
    public SelectNode(T node) {
        this.node = node;
    }
    
    public T getNodeWidget() {
    	return node;
    }
    
    @Override
    public Type<SelectNodeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectNodeHandler handler) {
        handler.select(this);
    }
}
