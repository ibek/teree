package org.teree.client.viewer.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.OnKeyUp;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.util.gwt.Keyboard;

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
    
	private static final int WAIT = 100; // ms, to get right size of content
	
    private Node _root;
    private NodeWidget _selected;
    private MapType _map;
    
    private boolean _editable;
    
    private AbsolutePanel _panel;
    private ScrollPanel _spanel;
    
    public Scene(){
        this(true);
    }
    
    public Scene(boolean editable){
        _map = new MindMap();
        _panel = new AbsolutePanel();
        _editable = editable;
        
        _spanel = new ScrollPanel(_panel);
        _spanel.setWidth("100%");
        _spanel.setHeight(Window.getClientHeight() + "px");
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
                        _selected.remove();
                    }
                    else if (keyCode == 45) { // Insert
                        Node child = _selected.createChild();
                        if(child.getParent().getParent() == null){ // is child of root
                            child.setLocation(_map.getRootChildNodeLocation(_root));
                        }
                        regenerateMap(child);
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
                _selected.unselect();
                _selected = null;
            }
        });
    }
    
    private void selectNodeWidget(NodeWidget nw) {
        if(_selected != null){
            _selected.unselect();
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
            return left.get((int)Math.floor(id/2.0));
        }else if(nwn.getLocation() == NodeLocation.LEFT){
            List<Node> cn = nwn.getChildNodes();
            if(cn.size() == 0){
                return null;
            }
            return cn.get((int)Math.floor(cn.size()/2.0));
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
            return right.get((int)Math.floor(id/2.0));
        }else if(nwn.getLocation() == NodeLocation.RIGHT){
            List<Node> cn = nwn.getChildNodes();
            if(cn.size() == 0){
                return null;
            }
            return cn.get((int)Math.floor(cn.size()/2.0));
        }else{ // left
            return nwn.getParent();
        }
    }
    
    public void setRoot(Node root) {
        _root = root;
        regenerateMap(null);
    }
    
    private void regenerateMap(final Node edit) {
        final int pcount = _panel.getWidgetCount();
        
        _map.prepare(_panel, _root, false); // #1
        
        final Regenerate reg = new Regenerate() {
            @Override
            public void regenerate() {
                regenerateMap(null);
            }
        };
        
        Timer timer = new Timer() { // workaround http://code.google.com/p/google-web-toolkit/issues/detail?id=4286
            @Override
            public void run() {
            	
                boolean succ = _map.resize(_panel); // #2
                
                if(!succ){ // some node is too wide
                	
                	for(int i=_panel.getWidgetCount()-1; i>pcount; --i){
                    	// remove NodeWidgets from previous preparation.
                        _panel.remove(i);
                    }
                	
                    _map.prepare(_panel, _root, !succ); // #3.1
                    
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            _map.resize(_panel); // #4
                            generate(edit, reg); // #5
                        }
                    };
                    t.schedule(WAIT);
                }else{
                    generate(edit, reg); // #3.2
                }
            }
        };
        timer.schedule(WAIT);
    }
    
    private void generate(final Node edit, Regenerate reg) {
    	_map.generate(_panel, _root, reg, _editable);
    	if(_editable && edit != null){ // only if we can edit the map
    	    // for new child node
            _selected = getNodeWidget(edit);
            _selected.edit();
    	}
    }
    
}
