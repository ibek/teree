package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class NodeWidget extends Composite {

    private final Node _node;
    private ContentWidget _content;

    private AbsolutePanel _panel;
    
    private Regenerate _regEvent;
    
    private boolean _selected;
    
    private final ViewMode _vlistener;
    
    private final EditMode _elistener;

    public NodeWidget(Node n) {
        _node = n;
        _selected = false;
        _panel = new AbsolutePanel();
        final ContentChanged cclistener = new ContentChanged() {
            @Override
            public void changed(NodeContent content) {
                regenerate();
            }
        };
        _elistener = new EditMode() {
            @Override
            public void edit() {
                _panel.remove(_content);
                ContentWidget cw = new ContentWidget(_node.getContent(), null, _vlistener);
                _content = cw;
                cw.setContentChangeListener(cclistener);
                _panel.add(cw);
            }
        };
        _vlistener = new ViewMode() {
            @Override
            public void view() {
                _panel.remove(_content);
                ContentWidget cw = new ContentWidget(_node.getContent(), _elistener, null);
                _content = cw;
                cw.setContentChangeListener(cclistener);
                _panel.add(cw);
            }
        };
        _content = new ContentWidget(n.getContent(), _elistener, null);
        _content.setContentChangeListener(cclistener);
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

    public Node createChild() {
        Node child = new Node();
        NodeContent nc = new NodeContent();
        nc.setText("");
        child.setContent(nc);
        child.setLocation(_node.getLocation());
        _node.addChild(child);
        return child;
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
    
    public boolean isEdited(){
        return _content.isEdited();
    }
    
    public void edit(){
        _content.edit();
    }
    
    public boolean isSelected() {
        return _selected;
    }
    
    public void setSelected(boolean selected) {
        _selected = selected;
        if (_selected) {
            _panel.getElement().getStyle().setBackgroundColor("grey");
        } else {
            _panel.getElement().getStyle().setBackgroundColor("transparent");
        }
    }

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
