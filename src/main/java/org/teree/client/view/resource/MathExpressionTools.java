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
package org.teree.client.view.resource;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class MathExpressionTools {
	
	private static final String MATHJAX_URL = "http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML";

	private static boolean initialized = false;
	public static boolean initScript() {
		if (!initialized) {
		    Element e = DOM.createElement("script"); 
		    DOM.setElementProperty(e, "language", "JavaScript"); 
		    DOM.setElementProperty(e, "src", MATHJAX_URL);
		    DOM.appendChild(RootPanel.get().getElement(), e);
		    initialized = true;
		    return true;
		}
		return false;
	}
	
	public static final native void renderLatexResult(com.google.gwt.dom.client.Element e) /*-{    
    if (typeof $wnd.MathJax !== 'undefined'  &&  $wnd.MathJax != null ){
        if (typeof $wnd.MathJax.Hub !== 'undefined'  &&  $wnd.MathJax.Hub != null ){
            if (typeof $wnd.MathJax.Hub.Typeset == 'function' ){
                $wnd.MathJax.Hub.Typeset(e);
            }
        }
    }
	}-*/;
	
}
