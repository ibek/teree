package org.teree.client.viewer.ui.widget;

import java.util.List;

import org.teree.client.viewer.ui.widget.event.NodeChangeRequest;
import org.teree.client.viewer.ui.widget.event.ContentChanged;
import org.teree.client.viewer.ui.widget.event.EditMode;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.client.viewer.ui.widget.event.ViewMode;
import org.teree.shared.data.MapChange;
import org.teree.shared.data.MapChange.Type;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class NodeWidget extends Composite {

    public interface Resources extends ClientBundle {
        
        @Source("add.png")
        ImageResource addIcon();
        
        @Source("up.png")
        ImageResource upIcon();
        
        @Source("down.png")
        ImageResource downIcon();
        
        Styles nodeButtons();

        public interface Styles extends CssResource {
            String button();
        }
        
    }
    
    private Resources _resources = GWT.create(Resources.class);
    
    private final Node _node;
    private ContentWidget _content;
    
    private PushButton _btnCreateChild;
    private PushButton _btnInsertBefore;
    private PushButton _btnInsertAfter;
    private PushButton _btnUp;
    private PushButton _btnDown;
    
    private boolean _selected;
    private final boolean _editable;

    private NodeChangeRequest _changeEvent;
    
    private final ViewMode _vlistener;
    private final EditMode _elistener;
    
    private AbsolutePanel _panel;
    private AbsolutePanel _parent;

    public NodeWidget(Node n, NodeChangeRequest nclistener, boolean editable) {
        _node = n;
        _selected = false;
        _changeEvent = nclistener;
        _editable = editable;
        _panel = new AbsolutePanel();
        
        final ContentChanged cclistener = new ContentChanged() {
            @Override
            public void changed(NodeContent content) {
                _changeEvent.req(new MapChange()
                    .setNodeId(_parent.getWidgetIndex(_panel))
                    .setType(Type.EDIT));
            }
        };
        
        _elistener = new EditMode() {
            @Override
            public void edit() {
                if(_selected){
                    unselect();
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
    
    public void init() {
        _parent = (AbsolutePanel)_panel.getParent();
    }
    
    private PushButton createButton(ImageResource ir) {
        final PushButton pb = new PushButton(new Image(ir));
        pb.setStylePrimaryName("action-button");
        pb.addStyleName(_resources.nodeButtons().button());
        pb.setTabIndex(-1);
        pb.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                pb.setFocus(false);
            }
        });
        return pb;
    }
    
    private void setButtons() {
        if(_btnCreateChild == null){
            _resources.nodeButtons().ensureInjected();
            _btnCreateChild = createButton(_resources.addIcon());
            _btnCreateChild.addClickHandler(new ClickHandler() {                
                @Override
                public void onClick(ClickEvent event) {
                    /**_changeEvent.change(, 
                            new MapChange()
                            .setNodeId((())_panel.getParent())
                            .setType(Type.CREATE_CHILD));*/
                }
            });
            _btnInsertBefore = createButton(_resources.addIcon());
            _btnInsertBefore.addClickHandler(new ClickHandler() {                
                @Override
                public void onClick(ClickEvent event) {
                    
                }
            });
            _btnInsertAfter = createButton(_resources.addIcon());
            _btnInsertAfter.addClickHandler(new ClickHandler() {                
                @Override
                public void onClick(ClickEvent event) {
                    
                }
            });
            _btnUp = createButton(_resources.upIcon());
            _btnUp.addClickHandler(new ClickHandler() {                
                @Override
                public void onClick(ClickEvent event) {
                    /**Node n = _node.clone();
                    Node upper = _node.upper();
                    _changeEvent.remove(_node);
                    List<Node> cn = upper.getParent().getChildNodes();
                    n.setParent(upper.getParent());
                    cn.add(cn.indexOf(upper), n);
                    if (_changeEvent != null) {
                        _changeEvent.regenerate(n, false);
                    }*/
                }
            });
            _btnDown = createButton(_resources.downIcon());
            _btnDown.addClickHandler(new ClickHandler() {                
                @Override
                public void onClick(ClickEvent event) {
                    /**Node n = _node.clone();
                    Node under = _node.under();
                    _changeEvent.remove(_node);
                    List<Node> cn = under.getParent().getChildNodes();
                    n.setParent(under.getParent());
                    cn.add(cn.indexOf(under)+1, n);
                    if (_changeEvent != null) {
                        _changeEvent.regenerate(n, false);
                    }*/
                }
            });
        }

        int addh = _resources.addIcon().getHeight();
        int addw = _resources.addIcon().getWidth();
        int uph = _resources.upIcon().getHeight();
        int upw = _resources.upIcon().getWidth();

        int posx = (_node.getLocation() == NodeLocation.LEFT)?-addw:_node.getContent().getWidth();
        int posr = (_node.getLocation() == NodeLocation.LEFT)?_node.getContent().getWidth():-upw;
        
        AbsolutePanel parent = ((AbsolutePanel)getParent());
        parent.add(_btnCreateChild, parent.getWidgetLeft(this)+posx, parent.getWidgetTop(this)+(_node.getContent().getHeight()-addh)/2);
        parent.add(_btnInsertBefore, parent.getWidgetLeft(this)+_node.getContent().getWidth()/2-addw, parent.getWidgetTop(this)-addh);
        parent.add(_btnInsertAfter, parent.getWidgetLeft(this)+_node.getContent().getWidth()/2-addw, parent.getWidgetTop(this)+_node.getContent().getHeight());
        if(_node.getParent() != null){
            if(_node.upper() != null){
                parent.add(_btnUp, parent.getWidgetLeft(this)+posr, parent.getWidgetTop(this)-uph/2);
            }
            if(_node.under() != null){
                parent.add(_btnDown, parent.getWidgetLeft(this)+posr, parent.getWidgetTop(this)+_node.getContent().getHeight()-uph/2);
            }
        }else{ // node is root
            //parent.add(_btnCreateChild, parent.getWidgetLeft(this)-addw, parent.getWidgetTop(this)+(_node.getContent().getHeight()-addh)/2);
        }
    }
    
    private void removeButtons() {
        
        AbsolutePanel parent = ((AbsolutePanel)getParent());
        if(parent != null && _btnCreateChild != null){
            parent.remove(_btnCreateChild);
            parent.remove(_btnInsertBefore);
            parent.remove(_btnInsertAfter);
            parent.remove(_btnUp);
            parent.remove(_btnDown);
        }
        
    }
    
    /**==================================**/

    public Node createChild() {
        Node child = new Node();
        NodeContent nc = new NodeContent();
        nc.setText("");
        child.setContent(nc);
        child.setLocation(_node.getLocation());
        return child;
    }

    public Node createBefore() {
        Node newnode = new Node();
        NodeContent nc = new NodeContent();
        nc.setText("");
        newnode.setContent(nc);
        newnode.setLocation(_node.getLocation());
        _node.insertBefore(newnode);
        return newnode;
    }

    public Node createAfter() {
        Node newnode = new Node();
        NodeContent nc = new NodeContent();
        nc.setText("");
        newnode.setContent(nc);
        newnode.setLocation(_node.getLocation());
        _node.insertAfter(newnode);
        return newnode;
    }
    
    public void edit() {
        _elistener.edit();
    }
    
    public void view() {
    	_vlistener.view();
    	//regenerate();
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
            setButtons();
            _panel.getElement().getStyle().setBackgroundColor("grey");
        } else {
            removeButtons();
            _panel.getElement().getStyle().setBackgroundColor("transparent");
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
