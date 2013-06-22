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
package org.teree.client.presenter;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.event.GlobalKeyUp;
import org.teree.client.event.GlobalKeyUpHandler;
import org.teree.client.event.SchemeReceived;
import org.teree.client.event.SchemeReceivedHandler;
import org.teree.client.text.UIMessages;
import org.teree.client.view.KeyAction;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class Editor extends Presenter {

	public interface Display extends KeyAction, Template {
		HasClickHandlers getSaveButton();

		Widget asWidget();

		void setScheme(Scheme scheme);

		String getSchemeSamplePicture();
	}

	@Inject
	private Display display;

	private Scheme scheme;

	public void bind() {
		getEventBus().addHandler(SchemeReceived.TYPE,
				new SchemeReceivedHandler() {
					@Override
					public void received(SchemeReceived event) {
						scheme = event.getScheme();
						display.setScheme(scheme);
						display.info(UIMessages.LANG.schemeReceived(scheme
								.toString()));
					}
				});

		getEventBus().addHandler(GlobalKeyUp.TYPE, new GlobalKeyUpHandler() {
			@Override
			public void onKeyUp(GlobalKeyUp event) {
				Event e = event.getEvent();
				int key = e.getKeyCode();

				if (key == 113) // #F2
				{
					display.edit();
				} else if (key == 45) // Insert
				{
					display.insert();
				} else if (key == KeyCodes.KEY_DELETE) {
					display.delete();
				} else if (key == 67 && e.getCtrlKey()) {
					// display.copy(); // TODO: create buttons for copy, cut and paste
				} else if (key == 88 && e.getCtrlKey()) {
					// display.cut();
				} else if (key == 86 && e.getCtrlKey()) {
					// display.paste();
				} else if (key == KeyCodes.KEY_UP) {
					display.up();
				} else if (key == KeyCodes.KEY_DOWN) {
					display.down();
				} else if (key == KeyCodes.KEY_LEFT) {
					display.left();
				} else if (key == KeyCodes.KEY_RIGHT) {
					display.right();
				}
			}
		});

		display.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scheme.setSchemePicture(display.getSchemeSamplePicture());
				saveScheme(scheme);
			}
		});

	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public Template getTemplate() {
		return display;
	}

	public void searchFrom(String fromOid, String text,
			RemoteCallback<List<Scheme>> callback) {
		SchemeFilter filter = new SchemeFilter();
		filter.setSearchText(text);
		selectFrom(fromOid, filter, callback);
	}

	@Override
	public String getTitle() {
		return "Editor";
	}

}
