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
package org.teree.client;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

public abstract class Keyboard {
    
    private static WindowCloseHandlerImpl listener;
    
    public void init() {
    	listener = new WindowCloseHandlerImpl(this);
    	Window.addCloseHandler(listener);
    	listener.init();
    }

    public abstract void onKeyUp(Event event);
    
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
