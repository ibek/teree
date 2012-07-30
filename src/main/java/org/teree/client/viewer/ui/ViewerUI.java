package org.teree.client.viewer.ui;

import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ViewerUI extends Composite {

    private static ViewerUiBinder uiBinder = GWT.create(ViewerUiBinder.class);

    interface ViewerUiBinder extends UiBinder<Widget, ViewerUI> {
    }

    private final Caller<ViewerService> _service;

    @UiField(provided = true)
    Scene _scene;

    public ViewerUI(Caller<ViewerService> service) {
        Window.enableScrolling(false);
        _service = service;
        String oid = Window.Location.getParameter("oid");
        if(oid == null){
            _service.call(new RemoteCallback<String>() {
                @Override
                public void callback(String response) {
                    System.out.println("inserted oid="+response);
                    setNode(response);
                }
            }).insertMap(TestMap.complex());
        } else {
            setNode(oid);
        }
        
        
        _scene = new Scene();
        initWidget(uiBinder.createAndBindUi(this));
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
