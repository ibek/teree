package org.teree.client.view.resource;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class MathExpressionTools {
	
	private static final String MATHJAX_URL = "http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML";

	/**
	 * TODO: check if style already exists
	 */
	public static void initScript() { 
	    Element e = DOM.createElement("script"); 
	    DOM.setElementProperty(e, "language", "JavaScript"); 
	    DOM.setElementProperty(e, "src", MATHJAX_URL);
	    DOM.appendChild(RootPanel.get().getElement(), e); 
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
