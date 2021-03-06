/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.editor.event.CheckNode;
import org.teree.client.view.editor.event.CheckNodeHandler;
import org.teree.client.view.type.Actions;
import org.teree.client.view.type.BehaviorController;
import org.teree.client.view.type.TreeController;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.MathExpression;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.PercentText;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.Tree;

import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class Scene extends Composite {
    
    private BehaviorController<NodeWidget> controller;
    
    private AbsolutePanel container;
    private Canvas canvas;
    private Frame tmpFrame;
    
    private boolean move = false;
    private Integer lastx;
    private Integer lasty;

	public Scene() {
        
        container = new AbsolutePanel();
        container.getElement().setId(DOM.createUniqueId());
        Style style = container.getElement().getStyle();
        style.setProperty("margin", "0 auto");
        style.setProperty("textAlign", "center");
        
        canvas = Canvas.createIfSupported();
        if (canvas == null) { // canvas is not supported
            // deal with it
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
        
        sp.getParent().addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				controller.selectNode(null);
			}
		}, ClickEvent.getType());
        
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
        
    	container.addHandler(new SelectNodeHandler<NodeWidget>() {
            @Override
            public void select(SelectNode<NodeWidget> event) {
            	controller.selectNode(event.getNodeWidget());
            }
            
        }, SelectNode.TYPE);
        
        container.addHandler(new NodeChangedHandler() {
			@Override
			public void changed(NodeChanged event) {
				controller.update(event.getNode());
			}
		}, NodeChanged.TYPE);
        
        container.addHandler(new CheckNodeHandler() {
			@Override
			public void check(CheckNode event) {
				controller.checkAllNodes();
			}
		}, CheckNode.TYPE);
        
    }
    
    public void setScheme(Scheme scheme) {
    	switch (scheme.getStructure()) {
	    	case Tree: {
		    	controller = new TreeController<NodeWidget>(container, canvas, tmpFrame, new EditorNodeWidgetFactory<NodeWidget>(), (Tree)scheme);
		    	break;
	    	}
    	}
    }

	public void setTmpFrame(Frame tmpFrame) {
		this.tmpFrame = tmpFrame;
	}
    
    public void createTextChildNode() {
    	Node child = new Node();
    	Text content = new Text();
    	content.setText("");
    	child.setContent(content);
    	createChildNode(child);
    }
    
    public void createImageChildNode() {
    	Node child = new Node();
    	child.setContent(new ImageLink());
    	createChildNode(child);
    }
    
    public void createLinkChildNode() {
    	Node child = new Node();
    	child.setContent(new Link());
    	createChildNode(child);
    }
    
    public void createMathExpressionChildNode() {
    	Node child = new Node();
    	MathExpression me = new MathExpression();
    	me.setExpression("");
    	child.setContent(me);
    	createChildNode(child);
    }
    
    public void createConnectorChildNode() {
    	Node child = new Node();
    	Connector con = new Connector();
    	con.setRoot(new Text());
    	child.setContent(con);
    	createChildNode(child);
    }
    
    public void createPercentChildNode() {
    	Node child = new Node();
    	PercentText pt = new PercentText();
    	child.setContent(pt);
    	createChildNode(child);
    }
    
    public String getSchemePicture() {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceHeight(this.canvas.getOffsetHeight());
        canvas.setCoordinateSpaceWidth(this.canvas.getOffsetWidth());
        controller.renderPicture(canvas, controller.getNodeWidgets());
        return canvas.toDataUrl();
    }
    
    public String getSchemeSamplePicture() {
        Canvas canvas = Canvas.createIfSupported();
        double scale = 0.7;
        canvas.setCoordinateSpaceHeight((int)(this.canvas.getOffsetHeight()*scale));
        canvas.setCoordinateSpaceWidth((int)(this.canvas.getOffsetWidth()*scale));
        canvas.getContext2d().scale(scale, scale);
        List<NodeWidget> nw = controller.getNodeWidgets();
        List<Boolean> collapsed = collapseForSample(nw, true);
        int[] rp = controller.renderPicture(canvas, nw);
        NodeWidget rw = nw.get(0);
        int x,y,w,h;
        x = (int)(rp[0] + rw.getWidgetWidth()/2 - Settings.SAMPLE_MAX_WIDTH/2/scale);
        if (x < 0) {
        	x = 0;
        	w = (int)(rp[0] + rw.getWidgetWidth()/2 + Settings.SAMPLE_MAX_WIDTH/2/scale);
        	if (w > rp[2]) {
        		w = rp[2];
        	}
        } else {
        	w = (int)(Settings.SAMPLE_MAX_WIDTH/scale);
        }
        
        y = (int)(rp[1] + rw.getWidgetHeight()/2 - Settings.SAMPLE_MAX_HEIGHT/2/scale);
        if (y < 0) {
        	y = 0;
        	h = (int)(rp[1] + rw.getWidgetHeight()/2 + Settings.SAMPLE_MAX_HEIGHT/2/scale);
        	if (h > rp[3]) {
        		h = rp[3];
        	}
        } else {
        	h = (int)(Settings.SAMPLE_MAX_HEIGHT/scale);
        }
        
        ImageData data = canvas.getContext2d().getImageData(x*scale, y*scale, w*scale, h*scale);
        Canvas canvasTmp = Canvas.createIfSupported();
        canvasTmp.setCoordinateSpaceHeight(data.getHeight());
        canvasTmp.setCoordinateSpaceWidth(data.getWidth());
        Context2d context = canvasTmp.getContext2d();
        context.putImageData(data, 0, 0);
        
        for (int i=1; i<nw.size(); ++i) { // return the state of collapsed nodes before rendering
        	nw.get(i).setCollapsed(collapsed.get(i-1));
        }
        
        controller.update(null);
        
        return canvasTmp.toDataUrl();
    }
    
    public Actions<NodeWidget> getController() {
    	return controller;
    }
    
    /**======================================================*/
    
    private void createChildNode(Node childNode) {
    	if (controller.getSelectedNode() != null &&
    		!(controller.getSelectedNode() instanceof ConnectorNodeWidget) &&
    		!(controller.getSelectedNode() instanceof LinkNodeWidget)) {
	    	controller.insertChildNode(childNode);
	    	
		   	 // to ensure that the node can be focused after insert
		   	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		        @Override
		        public void execute() {
		   			controller.editNode();
		        }
	        });
    	}
    }
    
    private List<Boolean> collapseForSample(List<NodeWidget> widgets, boolean collapse) {
    	List<Boolean> tmp = new ArrayList<Boolean>();
    	for (int i=1; i<widgets.size(); ++i) {
    		NodeWidget nw = widgets.get(i);
    		tmp.add(nw.isCollapsed());
    		if (nw.getNode().getParent() != null &&
    				nw.getNode().getChildNodes() != null && 
    				nw.getNode().getChildNodes().size() > 2) {
    			nw.setCollapsed(collapse);
    		}
    	}
    	return tmp;
    }
    
}
