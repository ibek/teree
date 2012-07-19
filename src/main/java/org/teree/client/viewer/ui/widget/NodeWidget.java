package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.client.viewer.ui.Box;
import org.teree.shared.annotation.Removed;
import org.teree.shared.data.Node;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class NodeWidget extends Composite {

    private Node _node;
    //private ContentWidget _content;
    
    private Box _bounds;
    private AbsolutePanel _panel;
    
    private Event<NodeWidget> nodeEvent;
    
    public NodeWidget(Node n) {
        _node = n;
        _bounds = new Box();
        _panel = new AbsolutePanel();
        //_panel.add(_content);
        Label l = new Label();
        l.setText("ahoj");
        _panel.add(l);
        initWidget(_panel);
    }
    
    public Box getBounds() {
        return _bounds;
    }
    
    public void createChild() {
        Node child = new Node();
        _node.addChild(child);
        nodeEvent.fire(this);
    }
    
    public void createBefore() {
        Node newnode = new Node();
        _node.insertBefore(newnode);
        nodeEvent.fire(this);
    }
    
    public void createAfter() {
        Node newnode = new Node();
        _node.insertAfter(newnode);
        nodeEvent.fire(this);
    }
    
    @Removed
    public void remove() {
        _node.remove();
        nodeEvent.fire(this);
    }
    
}
