package org.teree.client.view.editor.event;

import com.google.gwt.event.shared.EventHandler;

public interface SelectNodeHandler<T> extends EventHandler {

    public void select(SelectNode<T> event);
    
}
