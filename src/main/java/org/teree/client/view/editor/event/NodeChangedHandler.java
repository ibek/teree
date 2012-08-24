package org.teree.client.view.editor.event;

import org.teree.client.view.editor.NodeWidget;

import com.google.gwt.event.shared.EventHandler;

public interface NodeChangedHandler extends EventHandler {

    public void changed(NodeChanged event, NodeWidget node);
    
}
