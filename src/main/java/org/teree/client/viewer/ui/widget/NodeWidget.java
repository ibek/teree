package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.client.viewer.ui.Box;
import org.teree.shared.annotation.Removed;
import org.teree.shared.data.Node;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class NodeWidget extends Composite {

    private Node _node;
    private ContentWidget _content;
    
    private Box _bounds;
    private AbsolutePanel _panel;
    
    private Event<NodeWidget> nodeEvent;
    
    public NodeWidget(Node n, Box b) {
        _node = n;
        _bounds = b;
        _panel = new AbsolutePanel();
        _panel.add(_content);
        initWidget(_panel);
    }
    
    public Box getBounds() {
        return _bounds;
    }
    
    public void createChild() {
        Node child = new Node();
        _node.addChild(child);
        // fire event
    }
    
    public void createBefore() {
        Node newnode = new Node();
        _node.insertBefore(newnode);
        // fire event
    }
    
    public void createAfter() {
        Node newnode = new Node();
        _node.insertAfter(newnode);
        // fire event
    }
    
    @Removed
    public void remove() {
        _node.remove();
        nodeEvent.fire(this);
    }
    
}
