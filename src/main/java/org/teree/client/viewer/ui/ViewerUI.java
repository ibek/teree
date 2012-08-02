package org.teree.client.viewer.ui;

import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ViewerUI extends Composite {

    private static ViewerUiBinder uiBinder = GWT.create(ViewerUiBinder.class);

    interface ViewerUiBinder extends UiBinder<Widget, ViewerUI> {
    }

    private final Caller<ViewerService> _service;
    
    private String _oid;

    @UiField
    Button _btnAdd;

    @UiField
    Button _btnSave;

    @UiField(provided = true)
    Scene _scene;

    public ViewerUI(Caller<ViewerService> service) {
        Window.enableScrolling(false);
        _service = service;
        _scene = new Scene();
        _oid = Window.Location.getParameter("oid");
        if(_oid == null){
        	add(null);
        } else {
            setNode(_oid);
        }
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler("_btnAdd")
    void add(ClickEvent e) {
    	_scene.setRoot(NodeGenerator.newNode());
    }
    
    @UiHandler("_btnSave")
    void save(ClickEvent e) {
    	if (_oid != null) { // update
    		_service.call(new RemoteCallback<Void>() {
                @Override
                public void callback(Void response) {
                    System.out.println("updated");
                }
            }).update(_oid, _scene.getRoot());
    	} else { // insert new
            _service.call(new RemoteCallback<String>() {
                @Override
                public void callback(String response) {
                    System.out.println("inserted oid="+response);
                    _oid = response;
                }
            }).insertMap(_scene.getRoot());
    	}
    }
    
    private void setNode(String oid) {
        _service.call(new RemoteCallback<Node>() {
            @Override
            public void callback(Node response) {
                _scene.setRoot(response);
            }
        }, new ErrorCallback() {
            @Override
            public boolean error(Message message, Throwable throwable) {
                System.out.println(throwable.getMessage());
                return false;
            }
        }).getMap(oid);
    }

}
