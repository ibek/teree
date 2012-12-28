package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.scheme.MindMap;
import org.teree.client.scheme.SchemeType;
import org.teree.client.scheme.Renderer;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.client.view.viewer.NodeWidget;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Scheme;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
	
	private Renderer<NodeWidget> renderer;

    private AbsolutePanel container;
    private Canvas canvas;
    private Scheme scheme;
    private boolean move = false;
    private Integer lastx;
    private Integer lasty;
    
    public Scene() {
    	
        setSchemeType(Settings.DEFAULT_SCHEME_TYPE);
        
        container = new AbsolutePanel();
        Style style = container.getElement().getStyle();
        style.setProperty("margin", "0 auto");
        style.setProperty("textAlign", "center");
        
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
        }
        
        container.add(canvas);
        
        final ScrollPanel sp = new ScrollPanel(container);
        sp.setWidth(Window.getClientWidth()+"px");
        sp.setHeight((Window.getClientHeight()-Settings.SCENE_HEIGHT_LESS)+"px");
        Window.addResizeHandler(new ResizeHandler() {
            @Override
			public void onResize(ResizeEvent event) {
                sp.setWidth(event.getWidth() + "px");
                sp.setHeight((event.getHeight()-Settings.SCENE_HEIGHT_LESS) + "px");
                renderer.renderViewer(canvas, getNodeWidgets(), scheme.getRoot());
            }
		});
        
        initWidget(sp);
        
        bind();
        
        container.addDomHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				move = true;
				lastx = null;
				lasty = null;
			}
		}, MouseDownEvent.getType());
    	
        container.addDomHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				move = false;
			}
		}, MouseUpEvent.getType());
    	
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
    	container.addHandler(new NodeChangedHandler() {
			@Override
			public void changed(NodeChanged event) {
				renderer.renderViewer(canvas, getNodeWidgets(), scheme.getRoot());
			}
		}, NodeChanged.TYPE);
    }
    
    public void setSchemeType(SchemeType type) {
    	switch(type) {
	    	case MindMap: {
	    		renderer = new MindMap<NodeWidget>();
	    	}
    	}
    }
    
    private boolean requiresRender = false;
    public void setScheme(Scheme scheme) {
    	this.scheme = scheme;
    	container.clear();
        container.add(canvas);
        
        init(scheme.getRoot());
        final List<NodeWidget> widgets = getNodeWidgets();
        
        renderer.renderViewer(canvas, widgets, scheme.getRoot());
        if (requiresRender) { // to fix size and position of math expressions
        	requiresRender = false;
        	Timer t = new Timer() {
				@Override
				public void run() {
					renderer.renderViewer(canvas, widgets, Scene.this.scheme.getRoot());
				}
        	};
        	t.schedule(1000);
        }
    }
    
    public Scheme getScheme() {
    	return scheme;
    }
    
    public String getSchemePicture() {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceHeight(this.canvas.getOffsetHeight());
        canvas.setCoordinateSpaceWidth(this.canvas.getOffsetWidth());
        renderer.renderPicture(canvas, getNodeWidgets(), scheme.getRoot());
        return canvas.toDataUrl();
    }
    
    public boolean changeCollapseAll(boolean collapseAll) {
    	List<NodeWidget> widgets = getNodeWidgets();
		for (int i=1; i<widgets.size(); ++i) {
			NodeWidget nw = widgets.get(i);
			if (nw.getNode().getChildNodes() != null && 
					!nw.getNode().getChildNodes().isEmpty() && 
					nw.getNode().getParent().getParent() == null &&
					!(nw instanceof LinkNodeWidget)) {
				nw.setCollapsed(collapseAll);
			}
		}
		renderer.renderViewer(canvas, widgets, scheme.getRoot());
		return collapseAll;
    }

    private boolean initMathScript = true;
    private void init(Node node) {
    	
		NodeWidget nw = null;
		switch(node.getType()){
	        case IconText: {
	            nw = new TextNodeWidget(node);
	            nw.addDomHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Object src = event.getSource();
						if (src instanceof TextNodeWidget) {
							changeCollapseNode((TextNodeWidget) src);
						}
					}
				}, ClickEvent.getType());
	            break;
	        }
	        case ImageLink: {
	        	nw = new ImageNodeWidget(node);
	        	break;
	        }
	        case Link: {
	        	nw = new LinkNodeWidget(node);
	        	break;
	        }
	        case MathExpression: {
	        	if (initMathScript) {
	        		initMathScript = false;
	        		MathExpressionTools.initScript();
	        	}
	        	requiresRender = true;
	        	nw = new MathExpressionNodeWidget(node);
	        	break;
	        }
	    }
		container.add(nw,0,0);
		
    	List<Node> cn = node.getChildNodes();
    	for(int i=0; cn!=null && i<cn.size(); ++i){
    		Node n = cn.get(i);
    		init(n);
    	}
    }
    
    private void changeCollapseNode(TextNodeWidget nw) {
    	if (nw.getNode().getChildNodes() != null && !nw.getNode().getChildNodes().isEmpty()) { // has child nodes
	    	nw.setCollapsed(!nw.isCollapsed());
	        renderer.renderViewer(canvas, getNodeWidgets(), scheme.getRoot());
    	}
    }
    
    private List<NodeWidget> getNodeWidgets() {
    	Iterator<Widget> it = container.iterator();
    	List<NodeWidget> nodes = new ArrayList<NodeWidget>();
    	while (it.hasNext()) {
    		Widget w = it.next();
    		if(w instanceof NodeWidget){
    			nodes.add((NodeWidget)w); // there is the casting from Widget to NodeWidget
    		}
    	}
    	
    	return nodes;
    }
    
}
