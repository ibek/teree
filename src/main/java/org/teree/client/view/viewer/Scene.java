package org.teree.client.view.viewer;

import javax.inject.Inject;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Scene {
    
    @Inject
    private HandlerManager eventBus;
    
    private Canvas canvas;
    
    private AbsolutePanel container;
    
    public Scene() {
        
        bind();
        
        container = new AbsolutePanel();
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        container.add(canvas);
    }
    
    public void bind() {
        
    }
    
}
