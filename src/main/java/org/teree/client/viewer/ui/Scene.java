package org.teree.client.viewer.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.enterprise.event.Observes;

import org.teree.client.viewer.ui.type.MapType;
import org.teree.client.viewer.ui.type.MindMap;
import org.teree.client.viewer.ui.widget.ContentWidget;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Scene extends Composite {
    
    private Node _root;
    private MapType _map;
    
    private AbsolutePanel _panel;
    private ScrollPanel _spanel;
    
    public Scene(){
        this(null);
    }
    
    public Scene(Node root){
        _map = new MindMap();
        
        _panel = new AbsolutePanel();
        _panel.setWidth("100%");
        _panel.setHeight("100%");
        /**_spanel = new ScrollPanel(_panel);
        _spanel.setWidth("100%");
        _spanel.setHeight(Window.getClientHeight() + "px");
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(ResizeEvent event) {
                _spanel.setWidth(event.getWidth() + "px");
                _spanel.setHeight(event.getHeight() + "px");
            }

           });
        */
        //initWidget(_spanel);
        if(root != null){
            setRoot(root);
        }
        initWidget(_panel);
    }
    
    public void setRoot(Node root) {
        _root = root;
        regenerate();
    }
    
    public void regenerate(@Observes NodeWidget nw) {
        regenerate();
    }
    
    public void regenerate(@Observes ContentWidget cw) {
        regenerate();
    }
    
    private void regenerate() {
        _map.prepare(_panel, _root);
        Timer timer = new Timer() { // workaround http://code.google.com/p/google-web-toolkit/issues/detail?id=4286
            @Override
            public void run() {
                _map.resize(_panel);
                _map.generate(_panel, _root);
            }
        };
        timer.schedule(100);
    }
    
}
