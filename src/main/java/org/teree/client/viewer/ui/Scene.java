package org.teree.client.viewer.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.shared.Keyboard;
import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.OnKeyUp;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

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
 * TODO: fix select after createChildNode ...
 *
 * @author ibek
 *
 */
public class Scene extends Composite {
	
    private Node _root;
    private NodeWidget _selected;
    private Node _copied;
    private Node _cutted;
    private MapType _map;
    
    private boolean _editable;
    
    final Regenerate _reg;
    
    private AbsolutePanel _panel;
    private ScrollPanel _spanel;
    
    public Scene(){
        this(true);
    }
    
    public Scene(boolean editable){
        
        _reg = new Regenerate() {
            @Override
            public void regenerate(Node changed) {
                regenerateMap(changed);
            }
        };
        
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
                        remove(_selected.getNode());
                        _selected.remove();
                        regenerateMap(null);
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
    
    public void setRoot(Node root) {
        _root = root;
        generateFirstMap();
    }
    
    public void remove(Node node){
        List<Node> nc = node.getChildNodes();
        for(int i=0; nc != null && i<nc.size(); ++i){
            remove(nc.get(i));
        }
        _panel.remove(getNodeWidget(node));
    }
    
    private void regenerateMap(final Node changed) {
        regenerateMap(changed, false);
    }
    
    private void regenerateMap(final Node changed, final boolean edit) {

        final long from = System.currentTimeMillis();
        
        _map.prepare(_panel, _root, changed, false, _reg, _editable, 1);
        
        Timer t = new Timer() {
            @Override
            public void run() {
                boolean succ = _map.resize(_panel);
                if(!succ){ // some node is too wide
                    _map.prepare(_panel, _root, changed, false, _reg, _editable, 1); // #3.1
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
        _map.prepare(_panel, _root, null, false, _reg, _editable, -1); // #1
        
        boolean succ = _map.resize(_panel); // #2
        
        if(!succ){ // some node is too wide
            
            for(int i=_panel.getWidgetCount()-1; i>=0; --i){
                // remove NodeWidgets from previous preparation.
                _panel.remove(i);
            }
            
            _map.prepare(_panel, _root, null, !succ, _reg, _editable, -1); // #3.1
            _map.resize(_panel); // #4
            _map.generate(_panel, _root, _editable); // #5
        }else{
            _map.generate(_panel, _root, _editable); // #3.2
        }
    }
    
}
