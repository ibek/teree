package org.teree.client.view.editor.event;

import com.google.gwt.event.shared.EventHandler;

public interface NodeChangedHandler extends EventHandler {

    public void changed(NodeChanged event);
    
}
