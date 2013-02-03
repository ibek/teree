package org.teree.client.view.editor;

import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.NodeChangedHandler;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.SelectNodeHandler;
import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.client.view.type.BehaviorController;
import org.teree.client.view.type.TreeController;
import org.teree.shared.data.common.Connector;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.MathExpression;
import org.teree.shared.data.common.Node;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ScrollPanel;

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
                update(null);
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
        
    	container.addHandler(new SelectNodeHandler() {
            @Override
            public void select(SelectNode event) {
            	controller.selectNode(event.getNodeWidget());
            }
            
        }, SelectNode.TYPE);
        
        container.addHandler(new NodeChangedHandler() {
			@Override
			public void changed(NodeChanged event) {
				update(event.getNode());
			}
		}, NodeChanged.TYPE);
        
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
    
    public void update(Node changed) {
    	controller.update(changed);
    }
    
    public void editSelectedNode() {
    	controller.editNode();
    }
    
    public void removeSelectedNode() {
    	controller.removeNode();
		update(null);
    }
    
    public void createTextChildNode() {
    	Node child = new Node();
    	IconText content = new IconText();
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
    	con.setRoot(new IconText());
    	child.setContent(con);
    	createChildNode(child);
    }
    
    public void mergeSelectedConnector() {
		controller.mergeConnectorNode();
    }
    
    public void splitSelectedNode(Frame tmpFrame) {
    	controller.splitAndConnectNode();
    }
    
    public void copySelectedNode() {
    	controller.copyNode();
    }
    
    public void cutSelectedNode() {
    	controller.cutNode();
    }
    
    public void pasteNode() {
    	controller.pasteNode();
    }
    
    public void selectUpperNode() {
    	controller.selectUpperNode();
    }
    
    public void selectUnderNode() {
    	controller.selectUnderNode();
    }
    
    public void selectLeftNode() {
    	controller.selectLeftNode();
    }
    
    public void selectRightNode() {
    	controller.selectRightNode();
    }
    
    public void changeBoldOfSelectedNode() {
    	controller.boldNode();
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
        collapseForSample(nw, true);
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
        collapseForSample(nw, false);
        update(null);
        
        return canvasTmp.toDataUrl();
    }
    
    public void setNodeIcon(IconType icon) {
    	controller.setNodeIcon(icon);
    }
    
    public void addSelectedNodeListener(SelectedNodeListener snl) {
    	controller.addSelectedNodeListener(snl);
    }
    
    /**======================================================*/
    
    private void createChildNode(Node childNode) {
    	controller.insertChildNode(childNode);
    	
	   	 // to ensure that the node can be focused after insert
	   	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	   			editSelectedNode();
	        }
        });
    }
    
    private void collapseForSample(List<NodeWidget> widgets, boolean collapse) {
    	for (int i=1; i<widgets.size(); ++i) {
    		NodeWidget nw = widgets.get(i);
    		if (nw.getNode().getParent() != null &&
    				nw.getNode().getChildNodes() != null && 
    				nw.getNode().getChildNodes().size() > 2) {
    			nw.setCollapsed(collapse);
    		}
    	}
    }
    
}
