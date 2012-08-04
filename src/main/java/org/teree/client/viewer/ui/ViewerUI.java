package org.teree.client.viewer.ui;

import org.teree.client.shared.Text;
import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.ioc.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ViewerUI extends Composite {

    private static ViewerUiBinder uiBinder = GWT.create(ViewerUiBinder.class);

    interface ViewerUiBinder extends UiBinder<Widget, ViewerUI> {
    }

    private final Caller<ViewerService> _service;
    
    private Text.Type TT;
    
    private String _oid;
    
    @UiField
    Label _status;

    @UiField
    Button _btnNew;

    @UiField
    Button _btnSave;

    @UiField(provided = true)
    Scene _scene;

    public ViewerUI(Caller<ViewerService> service, RequestDispatcher dispatcher, MessageBus bus) {
        Window.enableScrolling(false);
        _service = service;
        _scene = new Scene(service, dispatcher, bus);
        TT = Text.produceTextTypes();
        _oid = Window.Location.getParameter("oid");
        if(_oid == null){
        	newMap(null);
        } else {
            setNode(_oid);
        }
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler("_btnNew")
    void newMap(ClickEvent e) {
        _oid = null;
    	_scene.setRoot(NodeGenerator.complex(), _oid);
    }
    
    @UiHandler("_btnSave")
    void save(ClickEvent e) {
    	if (_oid != null) { // update
    		_service.call(new RemoteCallback<Void>() {
                @Override
                public void callback(Void response) {
                    setStatus(TT.mapUpdated());
                }
            }).update(_oid, _scene.getRoot());
    	} else { // insert new
            _service.call(new RemoteCallback<String>() {
                @Override
                public void callback(String response) {
                    setStatus(TT.mapSaved(response));
                    _oid = response;
                }
            }).insertMap(_scene.getRoot());
    	}
    }
    
    private void setNode(String oid) {
        _service.call(new RemoteCallback<Node>() {
            @Override
            public void callback(Node response) {
                setStatus(TT.mapReceived(_oid));
                _scene.setRoot(response, _oid);
            }
        }, new ErrorCallback() {
            @Override
            public boolean error(Message message, Throwable throwable) {
                setStatus("Error: " + message);
                return false;
            }
        }).getMap(oid);
    }
    
    private void setStatus(String msg) {
        _status.setText(msg);
        Timer t = new Timer() {
            @Override
            public void run() {
                _status.setText("");
            }
        };
        t.schedule(5000);
    }

}
