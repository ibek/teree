package org.teree.client.view.editor.type;

import org.teree.client.view.NodeInterface;
import org.teree.client.visualization.Renderer;

import com.github.gwtbootstrap.client.ui.constants.BaseIconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public abstract class BehaviorController<T extends Widget & NodeInterface> implements Actions<T>, Renderer<T> {

    protected AbsolutePanel container;
    protected Canvas canvas;
    protected Frame tmpFrame;
    protected Class<T> clazz;
    
    public BehaviorController(Class<T> clazz, AbsolutePanel container, Canvas canvas, Frame tmpFrame) {
    	this.clazz = clazz;
    	this.container = container;
    	this.canvas = canvas;
    	this.tmpFrame = tmpFrame;
    }
	
}
