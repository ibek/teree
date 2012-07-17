package org.teree.client.viewer.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.enterprise.event.Observes;

import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class Scene extends Composite {

    private boolean _editable;
    
    private Canvas _canvas;
    private Node _root;
    private List<NodeWidget> _nodes;
    private MapType _map;
    
    private AbsolutePanel _panel;
    
    public Scene(Node root){
        this(root, false);
    }
    
    public Scene(Node root, boolean editable){
        _editable = editable;
        _canvas = Canvas.createIfSupported(); // !!!
        _map = new MindMap();
        _panel = new AbsolutePanel();
        _panel.add(_canvas);
        regenerate();
        initWidget(_panel);
    }
    
    public void regenerate(@Observes NodeWidget nw) {
        regenerate();
    }
    
    private void regenerate() {
        _nodes = _map.generate(_canvas, _root);
        Iterator<NodeWidget> it = _nodes.iterator();
        while(it.hasNext()) {
            NodeWidget nw = it.next();
            Box bounds = nw.getBounds();
            _panel.add(nw, bounds.x, bounds.y);
        }
    }
    
}
