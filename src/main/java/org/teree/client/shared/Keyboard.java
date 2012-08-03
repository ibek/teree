package org.teree.client.shared;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.viewer.ui.widget.event.OnKeyUp;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

public final class Keyboard {

    static {
        WindowCloseHandlerImpl listener = new WindowCloseHandlerImpl();
        Window.addCloseHandler(listener);
        listener.init();
    }
    
    private static List<OnKeyUp> listeners = new ArrayList<OnKeyUp>();

    private Keyboard() {
    }

    public static void forceStaticInit() {
    };
    
    public static void addOnKeyUpListener(OnKeyUp listener) {
        listeners.add(listener);
    }
    
    public static void clearOnKeyUpListeners() {
        listeners.clear();
    }
    
    public static void removeOnKeyUpListener(OnKeyUp listener) {
        listeners.remove(listener);
    }

    private static void onKeyUp(Event event) {
        for(int i=0; i<listeners.size(); ++i){
            listeners.get(i).onKeyUp(DOM.eventGetKeyCode(event));
        }
    }
    
    private static final class WindowCloseHandlerImpl implements CloseHandler<Window> {

        private native void init()
        /*-{
            $doc.onkeyup = function(evt) {
                @org.teree.client.shared.Keyboard::onKeyUp(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
            }
        }-*/;

        /**
            $doc.onkeydown = function(evt) {
                @org.teree.util.gwt.Keyboard::onKeyDown(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
            }
            $doc.onkeypress = function(evt) {
                @org.teree.util.gwt.Keyboard::onKeyPress(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
            }
        */

        @Override
        public void onClose(CloseEvent<Window> event) {
            /*-{
            $doc.onkeydown = null;
            $doc.onkeypress = null;
            $doc.onkeyup = null;
            }-*/;
        }
    }
}
