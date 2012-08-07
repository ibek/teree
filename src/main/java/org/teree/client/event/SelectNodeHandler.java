package org.teree.client.event;

import org.teree.client.view.editor.NodeWidget;

import com.google.gwt.event.shared.EventHandler;

public interface SelectNodeHandler extends EventHandler {

    public void select(SelectNode event, NodeWidget node);
    
}
