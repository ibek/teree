package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class NodeWidget extends Composite {

    private final Node _node;
    private ContentWidget _content;
    
    private boolean _selected;
    private final boolean _editable;

    private Regenerate _regEvent;
    private final ViewMode _viewEvent;
    private final EditMode _editEvent;
    
    private AbsolutePanel _panel;

    public NodeWidget(Node n, boolean editable) {
        _node = n;
        _selected = false;
        _editable = editable;
        _panel = new AbsolutePanel();
        
        final ContentChanged cclistener = new ContentChanged() {
            @Override
            public void changed(NodeContent content) {
                regenerate();
            }
        };
        
        _editEvent = new EditMode() {
            @Override
            public void edit() {
                _panel.remove(_content);
                ContentWidget cw = new ContentWidget(_node.getContent(), null, _viewEvent, _editable);
                _content = cw;
                cw.setContentChangeListener(cclistener);
                _panel.add(cw);
            }
        };
        
        _viewEvent = new ViewMode() {
            @Override
            public void view() {
                _panel.remove(_content);
                ContentWidget cw = new ContentWidget(_node.getContent(), _editEvent, null, _editable);
                _content = cw;
                cw.setContentChangeListener(cclistener);
                _panel.add(cw);
            }
        };
        
        _content = new ContentWidget(n.getContent(), _editEvent, null, _editable);
        _content.setContentChangeListener(cclistener);
        
        _panel.add(_content);
        initWidget(_panel);
    }
    
    /**==================================**/

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
    
    public void edit() {
        _editEvent.edit();
    }
    
    public void view() {
    	_viewEvent.view();
    }

    public void remove() {
        _node.remove();
        regenerate();
    }
    
    public void select() {
    	setSelected(true);
    }
    
    public void unselect() {
    	setSelected(false);
    }
    
    private void setSelected(boolean state) {
        _selected = state;
        if (_selected) {
            _panel.getElement().getStyle().setBackgroundColor("grey");
        } else {
            _panel.getElement().getStyle().setBackgroundColor("transparent");
        }
    }
    
    private void regenerate() {
        if (_regEvent != null) {
            _regEvent.regenerate();
        }
    }
    
    /**==================================**/

    public Node getNode() {
        return _node;
    }

    public ContentWidget getContent() {
        return _content;
    }
    
    public boolean isEdited(){
        return _content.isEdited();
    }
    
    public boolean isSelected() {
        return _selected;
    }
    
    public Regenerate getRegenerateListener() {
        return _regEvent;
    }
    
    public void setRegenerateListener(Regenerate listener) {
        _regEvent = listener;
    }

}
