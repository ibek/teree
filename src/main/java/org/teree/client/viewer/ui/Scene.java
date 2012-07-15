package org.teree.client.viewer.ui;

import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.NodeWidget;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Composite;

public class Scene extends Composite {

    private boolean _editable;
    
    private Canvas _canvas;
    private NodeWidget _root;
    private MapType _map;
    
    public Scene(boolean editable){
        _editable = editable;
        _canvas = Canvas.createIfSupported(); // !!!
        _map = new MindMap();
        //_map.render(_canvas, _root);
        initWidget(_canvas);
    }
    
}
