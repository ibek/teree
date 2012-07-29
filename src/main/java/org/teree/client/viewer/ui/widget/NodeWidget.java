package org.teree.client.viewer.ui.widget;

import javax.enterprise.event.Event;

import org.teree.client.viewer.ui.Box;
import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.shared.annotation.Removed;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

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
    
    private Regenerate _regEvent;

    public NodeWidget(Node n) {
        _node = n;
        _panel = new AbsolutePanel();
        _content = new ContentWidget(n.getContent(), false);
        _content.setContentChangeListener(new ContentChanged() {
            @Override
            public void changed(NodeContent content) {
                regenerate();
            }
        });
        _panel.add(_content);
        initWidget(_panel);
    }

    public Node getNode() {
        return _node;
    }

    public ContentWidget getContent() {
        return _content;
    }
    
    private void regenerate() {
        if (_regEvent != null) {
            _regEvent.regenerate();
        }
    }

    public void createChild() {
        Node child = new Node();
        _node.addChild(child);
        regenerate();
    }

    public void createBefore() {
        Node newnode = new Node();
        _node.insertBefore(newnode);
        regenerate();
    }

    public void createAfter() {
        Node newnode = new Node();
        _node.insertAfter(newnode);
        regenerate();
    }

    @Removed
    public void remove() {
        _node.remove();
        regenerate();
    }
    
    public Regenerate getRegenerateListener() {
        return _regEvent;
    }
    
    public void setRegenerateListener(Regenerate listener) {
        this._regEvent = listener;
    }

}
