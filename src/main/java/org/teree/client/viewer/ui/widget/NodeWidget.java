package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.client.viewer.ui.Box;
import org.teree.shared.annotation.Removed;
import org.teree.shared.data.Node;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NodeWidget extends Composite {

    private Node _node;
    private ContentWidget _content;

    private AbsolutePanel _panel;

    public NodeWidget(Node n) {
        _node = n;
        _panel = new AbsolutePanel();
        _content = new ContentWidget(_node.getContent(), false);
        _panel.add(_content);
        initWidget(_panel);
    }

    public Node getNode() {
        return _node;
    }

    public ContentWidget getContent() {
        return _content;
    }

    public void createChild() {
        Node child = new Node();
        _node.addChild(child);
        //nodeEvent.fire(this);
    }

    public void createBefore() {
        Node newnode = new Node();
        _node.insertBefore(newnode);
        //nodeEvent.fire(this);
    }

    public void createAfter() {
        Node newnode = new Node();
        _node.insertAfter(newnode);
        //nodeEvent.fire(this);
    }

    @Removed
    public void remove() {
        _node.remove();
        //nodeEvent.fire(this);
    }

}
