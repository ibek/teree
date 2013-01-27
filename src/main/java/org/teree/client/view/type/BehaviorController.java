package org.teree.client.view.type;

import org.teree.client.view.NodeInterface;
import org.teree.client.view.common.NodeWidgetFactory;
import org.teree.client.visualization.Renderer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public abstract class BehaviorController<T extends Widget & NodeInterface> implements Actions<T>, Renderer<T> {

    protected AbsolutePanel container;
    protected Canvas canvas;
    protected Frame tmpFrame;
    protected NodeWidgetFactory<T> nodeFactory;
    
    public BehaviorController(AbsolutePanel container, Canvas canvas, Frame tmpFrame, NodeWidgetFactory<T> nodeFactory) {
    	this.nodeFactory = nodeFactory;
    	this.container = container;
    	this.canvas = canvas;
    	this.tmpFrame = tmpFrame;
    }
	
}
