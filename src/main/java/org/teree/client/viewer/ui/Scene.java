package org.teree.client.viewer.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.ioc.client.api.Caller;
import org.teree.client.shared.Keyboard;
import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.ChangeNode;
import org.teree.client.viewer.ui.widget.event.NodeChangeRequest;
import org.teree.client.viewer.ui.widget.event.OnKeyUp;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.shared.ViewerService;
import org.teree.shared.data.MapChange;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: improve getDownNode and getUpNode to continue in another branch
 *
 * @author ibek
 *
 */
public class Scene extends Composite {
    
    public static final int PADDING = 20;
    private static final String COOP_CHANGE = "coop-change"; 
    
    private String _oid;
	
    private Node _root;
    private NodeWidget _selected;
    private Node _copied;
    private Node _cutted;
    private MapType _map;

    private boolean _editable;
    private boolean _cooperate;
    
    private final NodeChangeRequest _changereq;
    private final ChangeNode _change;
    
    private AbsolutePanel _panel;
    private ScrollPanel _spanel;
    
    private MessageBus _bus;
    
    public Scene(MessageBus bus){ // for testing purposes
        this(bus, true, false);
    }
    
    public Scene(MessageBus bus, boolean editable, boolean cooperate){
        
        _editable = editable;
        _cooperate = cooperate;
        _bus = bus;
        
        _changereq = new NodeChangeRequest() {
            @Override
            public void req(MapChange mc) {
                mapChanged(mc);
            }
        };
        
        _change = new ChangeNode() {
            
            @Override
            protected void regenerate(Node changed, boolean edit) {
                regenerateMap(changed, edit);
            }
        };
        
        _map = new MindMap();
        _panel = new AbsolutePanel(){
            @Override
            public void add(Widget w, int left, int top){
                super.add(w, left+PADDING, top+PADDING);
            }
            @Override
            public void setWidgetPosition(Widget w, int left, int top) {
                super.setWidgetPosition(w, left+PADDING, top+PADDING);
            }
            public int getWidgetLeft(Widget w) {
                return super.getWidgetLeft(w)-PADDING;
            }
            public int getWidgetTop(Widget w) {
                return super.getWidgetTop(w)-PADDING;
            }
        };
        _panel.getElement().getStyle().setPadding(PADDING, Unit.PX);
        
        _spanel = new ScrollPanel(_panel);
        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                _spanel.setWidth(event.getWidth() + "px");
                _spanel.setHeight(event.getHeight() + "px");
            }
		});
        
        if(_editable){
        	initKeyboard();
        }
        
        initHandlers();
        initWidget(_spanel);
    }
    
    private void initKeyboard() {
    	Keyboard.forceStaticInit();
        Keyboard.clearOnKeyUpListeners();
        Keyboard.addOnKeyUpListener(new OnKeyUp() {            
            @Override
            public void onKeyUp(int keyCode) {
                //System.out.println("key:"+keyCode);
                if(_selected != null && !_selected.isEdited()){
                    Node next = null;
                    NodeWidget nextnw = null;
                    if (keyCode == KeyCodes.KEY_UP) {
                        next = getUpNode(_selected);
                    }
                    else if (keyCode == KeyCodes.KEY_DOWN) {
                        next = getDownNode(_selected);
                    } 
                    else if (keyCode == KeyCodes.KEY_LEFT) {
                        next = getLeftNode(_selected);
                    } 
                    else if (keyCode == KeyCodes.KEY_RIGHT) {
                        next = getRightNode(_selected);
                    }
                    else if (keyCode == 113) { // #F2
                        _selected.edit();
                    }
                    else if (keyCode == KeyCodes.KEY_DELETE) {
                        if(_selected.getNode().getLocation() == NodeLocation.LEFT){
                            next = getRightNode(_selected);
                        }else{
                            next = getLeftNode(_selected);
                        }
                        removeNode(_selected.getNode());
                    }
                    else if (keyCode == 45) { // Insert
                        Node child = _selected.createChild();
                        if(child.getParent().getParent() == null){ // is child of root
                            child.setLocation(_map.getRootChildNodeLocation(_root));
                        }
                        regenerateMap(child, true);
                    }else if(keyCode == 67){ // Copy
                        _copied = _selected.getNode();
                        _cutted = null;
                    }else if(keyCode == 88){ // Cut
                        _cutted = _selected.getNode().clone();
                        _copied = null;
                        if(_selected.getNode().getLocation() == NodeLocation.LEFT){
                            next = getRightNode(_selected);
                        }else{
                            next = getLeftNode(_selected);
                        }
                        _selected.remove();
                    }else if(keyCode == 86){ // Paste
                        Node changed;
                        if(_cutted != null){
                            changed = _cutted;
                            _selected.getNode().addChild(changed);
                            _cutted = null;
                        }else if(_copied != null){
                            changed = _copied.clone();
                            _selected.getNode().addChild(changed);
                        }else{
                            return;
                        }
                        next = changed;
                        regenerateMap(changed);
                    }
                    nextnw = getNodeWidget(next);
                    if(nextnw != null){
                        selectNodeWidget(nextnw);
                    }
                }
            }
        });
    }
    
    private void initHandlers() {
        SelectNode.addListener(new SelectNode.Listener() {
            @Override
            public void select(Node n) {
                selectNodeWidget(getNodeWidget(n));
            }
            @Override
            public void unselect() {
                if(_selected != null){
                    _selected.unselect();
                    _selected = null;
                }
            }
        });
    }
    
    private void initCooperation() {
        if(_oid != null && _editable && _cooperate){
            _bus.subscribe(_oid, new MessageCallback() {
                @Override
                public void callback(Message message) {
                    System.out.println("ahoj je to tady");
                    MapChange mc = message.get(MapChange.class, COOP_CHANGE);
                    _change.change(_panel, mc);
                }
            });
        }
    }
    
    private void selectNodeWidget(NodeWidget nw) {
        if(_selected != null){
            _selected.unselect();
        }
        if(nw == null){
            return;
        }
        _selected = nw;
        _selected.select();
    }
    
    private NodeWidget getNodeWidget(Node n) {
        if(n == null){
            return null;
        }
        NodeWidget nw = null;
        Iterator<Widget> it = _panel.iterator();
        while(it.hasNext()){
            Widget w = it.next();
            if(w instanceof NodeWidget){
                if(((NodeWidget)w).getNode() == n){ // found
                    return (NodeWidget)w;
                }
            }
        }
        return nw;
    }
    
    private Node getUpNode(NodeWidget nw) {
        Node last = null;
        Node parent = nw.getNode().getParent();
        if(parent != null){
            List<Node> cn = parent.getChildNodes();
            for(int i=0; i<cn.size(); ++i){
                Node n = cn.get(i);
                if(nw.getNode() == n){
                    return last;
                }
                last = n;
            }
        }
        return null;
    }
    
    private Node getDownNode(NodeWidget nw) {
        Node parent = nw.getNode().getParent();
        boolean next = false;
        if(parent != null){
            List<Node> cn = parent.getChildNodes();
            for(int i=0; i<cn.size(); ++i){
                Node n = cn.get(i);
                if(next){
                    return n;
                }
                if(nw.getNode() == n){
                    next = true;
                }
            }
        }
        return null;
    }
    
    private Node getLeftNode(NodeWidget nw) {
        Node nwn = nw.getNode();
        if(nwn.getParent() == null){ // root
            List<Node> cn = nwn.getChildNodes();
            List<Node> left = new ArrayList<Node>();
            int id = 0;
            if(cn.size() == 0){
                return null;
            }
            for(int i=0; i<cn.size(); ++i){
                if(cn.get(i).getLocation() == NodeLocation.LEFT){
                    id++;
                    left.add(cn.get(i));
                }
            }
            return left.get((int)Math.floor(id/2.0-0.5)); // select middle, but first if there are 2
        }else if(nwn.getLocation() == NodeLocation.LEFT){
            List<Node> cn = nwn.getChildNodes();
            if(cn.size() == 0){
                return null;
            }
            return cn.get((int)Math.floor(cn.size()/2.0-0.5)); // select middle, but first if there are 2
        }else{ // right
            return nwn.getParent();
        }
    }
    
    private Node getRightNode(NodeWidget nw) {
        Node nwn = nw.getNode();
        if(nwn.getParent() == null){ // root
            List<Node> cn = nwn.getChildNodes();
            List<Node> right = new ArrayList<Node>();
            int id = 0;
            if(cn.size() == 0){
                return null;
            }
            for(int i=0; i<cn.size(); ++i){
                if(cn.get(i).getLocation() == NodeLocation.RIGHT){
                    id++;
                    right.add(cn.get(i));
                }
            }
            return right.get((int)Math.floor(id/2.0-0.5)); // select middle, but first if there are 2
        }else if(nwn.getLocation() == NodeLocation.RIGHT){
            List<Node> cn = nwn.getChildNodes();
            if(cn.size() == 0){
                return null;
            }
            return cn.get((int)Math.floor(cn.size()/2.0-0.5)); // select middle, but first if there are 2
        }else{ // left
            return nwn.getParent();
        }
    }
    
    public Node getRoot() {
    	return _root;
    }
    
    public void setRoot(Node root, String oid) {
        _oid = oid;
        _root = root;
        initCooperation();
        generateFirstMap();
    }
    
    public void removeNode(Node node){
        _selected.unselect();
        removeNodeFromPanel(node);
        _selected.remove();
        regenerateMap(null);
    }
    
    public void removeNodeFromPanel(Node node){
        List<Node> nc = node.getChildNodes();
        for(int i=0; nc != null && i<nc.size(); ++i){
            removeNodeFromPanel(nc.get(i));
        }
        _panel.remove(getNodeWidget(node));
    }
    
    private void regenerateMap(final Node changed) {
        regenerateMap(changed, false);
    }
    
    private void regenerateMap(final Node changed, final boolean edit) {
        
        if(_selected != null){
            _selected.unselect(); // have to unselect because of the buttons
            _selected = null;
        }

        final long from = System.currentTimeMillis();
        
        _map.prepare(_panel, _root, changed, false, _changereq, _editable, 1);
        
        Timer t = new Timer() {
            @Override
            public void run() {
                boolean succ = _map.resize(_panel);
                if(!succ){ // some node is too wide
                    _map.prepare(_panel, _root, changed, false, _changereq, _editable, 1); // #3.1
                    _map.resize(_panel); // #4
                    _map.generate(_panel, _root, _editable); // #5
                }else{
                    _map.generate(_panel, _root, _editable); // #3.2
                }
                selectNodeWidget(getNodeWidget(changed));
                if(_editable && edit){
                    _selected.edit();
                }
                System.out.println("totalRegenerate:"+(System.currentTimeMillis()-from)+"ms");
            }
        };
        t.schedule(50); // to ensure that widget automatically resized size is already set
    }
    
    private void generateFirstMap() {

        _panel.clear();
        _map.prepare(_panel, _root, null, false, _changereq, _editable, -1); // #1
        
        boolean succ = _map.resize(_panel); // #2
        
        if(!succ){ // some node is too wide
            
            for(int i=_panel.getWidgetCount()-1; i>=0; --i){
                // remove NodeWidgets from previous preparation.
                _panel.remove(i);
            }
            
            _map.prepare(_panel, _root, null, !succ, _changereq, _editable, -1); // #3.1
            _map.resize(_panel); // #4
            _map.generate(_panel, _root, _editable); // #5
        }else{
            _map.generate(_panel, _root, _editable); // #3.2
        }
    }
    
    private void mapChanged(MapChange mc) {
        if(_oid != null && _editable && _cooperate) {
            mc.setOid(_oid);
            MessageBuilder.createMessage()
            .toSubject("CooperationService")
            .with(COOP_CHANGE, mc)
            .done()
            .sendNowWith(_bus);
        }else {
            _change.change(_panel, mc);
        }
    }
    
}
