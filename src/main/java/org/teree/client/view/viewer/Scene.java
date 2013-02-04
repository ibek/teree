package org.teree.client.view.viewer;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.type.BehaviorController;
import org.teree.client.view.type.TreeController;
import org.teree.client.view.viewer.NodeWidget;
import org.teree.client.view.viewer.event.CollapseNode;
import org.teree.client.view.viewer.event.CollapseNodeHandler;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.Tree;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Scene extends Composite {
	
	private BehaviorController<NodeWidget> controller;

    private AbsolutePanel container;
    private Canvas canvas;
    
    private boolean move = false;
    private Integer lastx;
    private Integer lasty;
    
    public Scene() {
        
        container = new AbsolutePanel();
        Style style = container.getElement().getStyle();
        style.setProperty("margin", "0 auto");
        style.setProperty("textAlign", "center");
        
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // TODO: canvas is not supported
        }
        
        final ScrollPanel sp = new ScrollPanel(container);
        sp.setWidth(Window.getClientWidth()+"px");
        sp.setHeight((Window.getClientHeight()-Settings.SCENE_HEIGHT_LESS)+"px");
        Window.addResizeHandler(new ResizeHandler() {
            @Override
			public void onResize(ResizeEvent event) {
                sp.setWidth(event.getWidth() + "px");
                sp.setHeight((event.getHeight()-Settings.SCENE_HEIGHT_LESS) + "px");
                controller.update(null);
            }
		});
        
        initWidget(sp);
        
        bind();
    	
        container.addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (move) {
					if (lastx != null && lasty != null) {
						sp.setHorizontalScrollPosition(sp.getHorizontalScrollPosition() - (event.getX() - lastx));
						sp.setVerticalScrollPosition(sp.getVerticalScrollPosition() - (event.getY() - lasty));
					}
					lastx = event.getX();
					lasty = event.getY();
				}
			}
		}, MouseMoveEvent.getType());
        
    }
    
    private void bind() {
        
        container.addDomHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
					event.preventDefault();
					move = true;
					container.getElement().getStyle().setCursor(Cursor.MOVE);
					lastx = null;
					lasty = null;
				}
			}
		}, MouseDownEvent.getType());
    	
        container.addDomHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (move) {
					container.getElement().getStyle().clearCursor();
					move = false;
				}
			}
		}, MouseUpEvent.getType());
        
    	container.addHandler(new NodeChangedHandler() {
			@Override
			public void changed(NodeChanged event) {
				controller.update(event.getNode());
			}
		}, NodeChanged.TYPE);
    	
    	container.addHandler(new CollapseNodeHandler() {
			@Override
			public void collapse(CollapseNode event) {
				changeCollapseNode(event.getNode());
			}
		}, CollapseNode.TYPE);
    	
    }
    
    public void setScheme(Scheme scheme) {
    	switch (scheme.getStructure()) {
	    	case Tree: {
	    		controller = new TreeController<NodeWidget>(container, canvas, null, new ViewNodeWidgetFactory<NodeWidget>(), (Tree)scheme);
	    		break;
	    	}
    	}
    }
    
    public String getSchemePicture() {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceHeight(this.canvas.getOffsetHeight());
        canvas.setCoordinateSpaceWidth(this.canvas.getOffsetWidth());
        controller.renderPicture(canvas, controller.getNodeWidgets());
        return canvas.toDataUrl();
    }
    
    public void changeCollapseAll(boolean collapseAll) {
		controller.collapseAll(controller.getNodeWidgets(), collapseAll);
		controller.update(null);
    }
    
    private void changeCollapseNode(NodeWidget nw) {
    	if (nw.getNode().getChildNodes() != null && !nw.getNode().getChildNodes().isEmpty() && nw.getNode().getParent() != null) { // has child nodes
	    	nw.setCollapsed(!nw.isCollapsed());
	    	if (!nw.isCollapsed()) {
	    		nw.update();
	    	}
	        controller.update(null);
    	}
    }
    
}
