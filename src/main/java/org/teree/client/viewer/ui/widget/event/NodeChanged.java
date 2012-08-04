package org.teree.client.viewer.ui.widget.event;

import org.teree.shared.data.Node;

public interface NodeChanged {

    public void regenerate(Node changed, boolean edit);
    
    public void remove(Node node);
    
}
