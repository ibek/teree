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
package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.Settings;
import org.teree.client.presenter.HomePage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

public class Home extends TemplateScene implements HomePage.Display {

	private static HomeBinder uiBinder = GWT.create(HomeBinder.class);

    interface HomeBinder extends UiBinder<Widget, Home> {
    }
	
	@UiField
	Anchor changeLogs;
	
	@UiField
	Anchor changeLogs2;
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        
        ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.CHANGE_LOGS_LINK);
			}
		};
        changeLogs.addClickHandler(ch);
        changeLogs2.addClickHandler(ch);
        
    }

}
