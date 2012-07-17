package org.teree.client.viewer.ui.type;

import java.util.List;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;

import com.google.gwt.canvas.client.Canvas;

public interface MapType {

    public List<NodeWidget> generate(Canvas canvas, Node root);
    
}
