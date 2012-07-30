package org.teree.client.viewer.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.OnKeyUp;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.util.gwt.Keyboard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
    
    private Node _root;
    private NodeWidget _selected;
    private MapType _map;
    
    private AbsolutePanel _panel;
    private ScrollPanel _spanel;
    
    public Scene(){
        this(null);
    }
    
    public Scene(Node root){
        _map = new MindMap();
        
        _panel = new AbsolutePanel();
        _panel.setWidth("100%");
        _panel.setHeight("100%");
        /**_spanel = new ScrollPanel(_panel);
        _spanel.setWidth("100%");
        _spanel.setHeight(Window.getClientHeight() + "px");
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(ResizeEvent event) {
                _spanel.setWidth(event.getWidth() + "px");
                _spanel.setHeight(event.getHeight() + "px");
            }

           });
        */
        //initWidget(_spanel);
        if(root != null){
            setRoot(root);
        }
        Keyboard.forceStaticInit();
        Keyboard.clearOnKeyUpListeners();
        Keyboard.addOnKeyUpListener(new OnKeyUp() {            
            @Override
            public void onKeyUp(int keyCode) {
                Node next = null;
                NodeWidget nextnw = null;
                System.out.println("key:"+keyCode + " edit:"+_selected.isEdited());
                if(_selected != null && !_selected.isEdited()){
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
                        regenerateRoot(child);
                    }
                    nextnw = getNodeWidget(next);
                    if(nextnw != null){
                        if(_selected != null){
                            _selected.setSelected(false);
                        }
                        _selected = nextnw;
                        _selected.setSelected(true);
                    }
                }
            }
        });
        initWidget(_panel);
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
        regenerateRoot(null);
    }
    
    private void regenerateRoot(final Node select) {
        final int pcount = _panel.getWidgetCount();
        _map.prepare(_panel, _root, false); // #1
        final Regenerate reg = new Regenerate() {
            @Override
            public void regenerate() {
                regenerateRoot(null);
            }
        };
        Timer timer = new Timer() { // workaround http://code.google.com/p/google-web-toolkit/issues/detail?id=4286
            @Override
            public void run() {
                boolean succ = _map.resize(_panel); // #2
                if(!succ){ // some node is too wide
                    clear(pcount);
                    _map.prepare(_panel, _root, !succ); // #3.1
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            _map.resize(_panel); // #4
                            NodeWidget s = _map.generate(_panel, _root, reg); // #5
                            if(_selected == null){
                                _selected = s; 
                            }else if(select != null){ // for create child node
                                _selected = getNodeWidget(select);
                                _selected.edit();
                            }else{ // to select back the changed node
                                _selected = getNodeWidget(_selected.getNode());
                            }
                            _selected.setSelected(true);
                        }
                    };
                    t.schedule(100);
                }else{
                    NodeWidget s = _map.generate(_panel, _root, reg); // #3.2
                    if(_selected == null){
                        _selected = s; 
                    }else if(select != null){ // for create child node
                        _selected = getNodeWidget(select);
                        _selected.edit();
                    }else{ // to select back the changed node
                        _selected = getNodeWidget(_selected.getNode());
                    }
                    _selected.setSelected(true);
                }
            }
        };
        timer.schedule(100);
    }
    
    private void clear(int from){
        for(int i=_panel.getWidgetCount()-1; i>from; --i){
            _panel.remove(i);
        }
    }
    
}
