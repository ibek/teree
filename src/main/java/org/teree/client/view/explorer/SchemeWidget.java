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
package org.teree.client.view.explorer;

import org.teree.client.CurrentUser;
import org.teree.client.Settings;
import org.teree.client.text.UIConstants;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Permissions;
import org.teree.shared.data.common.Scheme;
import com.github.gwtbootstrap.client.ui.Badge;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ThumbnailLink;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.BadgeType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class SchemeWidget extends Composite {
	
	private static final int COMPONENTS_HEIGHT = 65;

	private ThumbnailLink th;

	private Badge root;
	private Image screen;
	private Badge author;
	private Button remove;
	private Button edit;
	private Button view;
	private Button permissions;
	
	private Scheme scheme;
	private PermissionsDialog pdialog;
	
	private UIConstants UIC = UIConstants.LANG;
	
	public SchemeWidget() {
		
		th = new ThumbnailLink();
		th.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		th.setSize(Settings.SAMPLE_MAX_WIDTH+"px", (Settings.SAMPLE_MAX_HEIGHT+COMPONENTS_HEIGHT)+"px");
		th.getElement().getStyle().setMargin(5.0, Unit.PX);
		th.getAnchor().getElement().getStyle().setBackgroundColor("white");
		th.getAnchor().getElement().getStyle().setProperty("textAlign", "center");
		
		root = new Badge();
		root.setType(BadgeType.INFO);
		root.getElement().getStyle().setProperty("lineHeight", "30px");
		root.setWidth((Settings.SAMPLE_MAX_WIDTH - 20)+"px");
		
		FlowPanel fscreen = new FlowPanel();
		fscreen.setSize(Settings.SAMPLE_MAX_WIDTH+"px", (Settings.SAMPLE_MAX_HEIGHT)+"px");
		screen = new Image();
		fscreen.add(screen);
		
		edit = new Button(UIC.edit());
		edit.setEnabled(false);
		edit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.EDIT_LINK + Settings.OID_PARAM + scheme.getOid());
			}
		});
		edit.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		view = new Button(UIC.view());
		view.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		
		Style vs = view.getElement().getStyle();
		vs.setFloat(Style.Float.RIGHT);
		vs.setZIndex(10);
		
		author = new Badge();
		//author.setType(BadgeType.INFO);
		author.getElement().getStyle().setProperty("lineHeight", "30px");
		author.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				if (scheme != null) {
					History.newItem(Settings.USERHOME_LINK + scheme.getAuthor().getUserId());
				}
			}
		});
		author.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				author.getElement().getStyle().setColor("white");
			}
		});
		author.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				author.getElement().getStyle().setColor("black");
			}
		});
		
		pdialog = new PermissionsDialog();

		permissions = new Button("", IconType.LOCK);
		permissions.setVisible(false);
		permissions.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				
				pdialog.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop());
				pdialog.show();
				pdialog.setPermissions(scheme.getPermissions());
			}
		});
		Style ps = permissions.getElement().getStyle();
		ps.setFloat(Style.Float.LEFT);
		
        Tooltip pt = new Tooltip(UIC.set_permissions());
        pt.add(permissions);
		
		remove = new Button("", IconType.TRASH);
		remove.setVisible(false);
		Style rs = remove.getElement().getStyle();
		rs.setFloat(Style.Float.RIGHT);
		
        Tooltip rt = new Tooltip(UIC.remove_scheme());
        rt.add(remove);

        th.add(pt);
        th.add(root);
        th.add(rt);
		th.add(fscreen);
		FlowPanel fp = new FlowPanel();
		fp.add(edit);
        fp.add(author);
		fp.add(view);
		th.add(fp);
		
		initWidget(th);
		
		th.getAnchor().setSize(Settings.SAMPLE_MAX_WIDTH+"px", (Settings.SAMPLE_MAX_HEIGHT+COMPONENTS_HEIGHT)+"px");
		
	}
	
	public Scheme getScheme() {
		return scheme;
	}
	
	public Permissions getPermissions() {
		return pdialog.getPermissions();
	}
	
	public void closePermissionsDialog() {
		pdialog.setVisible(false);
	}
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
		if (scheme.getSchemePicture() != null) {
			screen.setUrl(scheme.getSchemePicture());
		}
		String t = scheme.toString();
		if (t.length() > 25) {
			t = t.substring(0, 25) + "...";
		}
		root.setText(t);
		if (scheme.getAuthor() != null) {
			author.setText(scheme.getAuthor().getName());
		}
		if (scheme.getPermissions() != null) {
			UserInfo ui = CurrentUser.getInstance().getUserInfo();
			if (ui != null && scheme.getAuthor().getUserId().equals(ui.getUserId())) {
				permissions.setVisible(true);
				remove.setVisible(true);
				root.setWidth((Settings.SAMPLE_MAX_WIDTH - 110)+"px");
				edit.setEnabled(true);
			} else if (scheme.getPermissions().canEdit(ui)) {
				edit.setEnabled(true);
			}
		}
	}
	
	public HasClickHandlers getRemoveButton() {
		return remove;
	}
	
	public HasClickHandlers getUpdatePermissionsButton() {
		return pdialog.getOk();
	}
	
}
