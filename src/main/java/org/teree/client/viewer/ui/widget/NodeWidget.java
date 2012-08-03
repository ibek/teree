package org.teree.client.viewer.ui.widget;

import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class NodeWidget extends Composite {

    private final Node _node;
    private ContentWidget _content;
    
    private boolean _selected;
    private final boolean _editable;

    private Regenerate _regEvent;
    
    private final ViewMode _vlistener;
    private final EditMode _elistener;
    
    private AbsolutePanel _panel;

    public NodeWidget(Node n, Regenerate rlistener, boolean editable) {
        _node = n;
        _selected = false;
        _regEvent = rlistener;
        _editable = editable;
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
                if(_selected){
                    _panel.remove(_content);
                    ContentWidget cw = ContentWidget.create(_node.getContent(), null, _vlistener, _editable);
                    _content = cw;
                    cw.setContentChangeListener(cclistener);
                    _panel.add(cw);
                }else{
                    SelectNode.select(_node);
                }
            }
        };
        
        _vlistener = new ViewMode() {
            @Override
            public void view() {
                _panel.remove(_content);
                ContentWidget cw = ContentWidget.create(_node.getContent(), _elistener, null, _editable);
                _content = cw;
                cw.setContentChangeListener(cclistener);
                _panel.add(cw);
            }
        };
        
        _panel.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SelectNode.select(_node);
            }
        }, ClickEvent.getType());
        
        _content = ContentWidget.create(n.getContent(), _elistener, _vlistener, editable);
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
        _elistener.edit();
    }
    
    public void view() {
    	_vlistener.view();
    	regenerate();
        SelectNode.select(_node);
    }

    public void remove() {
        _node.remove();
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
        System.out.println("ahoj");
        if (_regEvent != null) {
            System.out.println("cau");
            _regEvent.regenerate(_node);
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

}
