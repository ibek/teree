package org.teree.client;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.teree.client.event.GlobalKeyUp;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

@Dependent
public class Keyboard {
    
    private static WindowCloseHandlerImpl listener;
    
    @Inject @Named(value="eventBus")
    private HandlerManager eventBus;
    
    public void init() {
    	listener = new WindowCloseHandlerImpl(this);
    	Window.addCloseHandler(listener);
    	listener.init();
    }

    public void onKeyUp(Event event) {
    	eventBus.fireEvent(new GlobalKeyUp(DOM.eventGetKeyCode(event)));
    }
    
    private final static class WindowCloseHandlerImpl implements CloseHandler<Window> {
    	
    	private static Keyboard kb;
    	
    	public WindowCloseHandlerImpl(Keyboard kb) {
    		WindowCloseHandlerImpl.kb = kb;
    	}

        private native void init()
        /*-{
            $doc.onkeyup = function(evt) {
                @org.teree.client.Keyboard.WindowCloseHandlerImpl::onKeyUp(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
            }
        }-*/;

        private static void onKeyUp(Event event) {
        	kb.onKeyUp(event);
        }
        
        @Override
        public void onClose(CloseEvent<Window> event) {
            /*-{
            $doc.onkeyup = null;
            }-*/;
        }
    }
}
