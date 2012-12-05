package org.teree.client.view.explorer;

import org.teree.client.Settings;
import org.teree.client.text.Explorer;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.scheme.Permissions;
import org.teree.shared.data.scheme.Scheme;

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
import com.google.gwt.user.client.ui.Image;

public class SchemeWidget extends Composite {

	private ThumbnailLink th;
	
	private Image screen;
	private Badge author;
	private Button remove;
	private Button edit;
	private Button view;
	private Button permissions;
	
	private Scheme scheme;
	private PermissionsDialog pdialog;
	
	private Explorer TEXT = Explorer.LANG;
	
	public SchemeWidget() {
		
		th = new ThumbnailLink();
		th.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		th.setSize(Settings.SAMPLE_MAX_WIDTH+"px", (Settings.SAMPLE_MAX_HEIGHT+40)+"px");
		th.getElement().getStyle().setMargin(5.0, Unit.PX);
		th.getAnchor().getElement().getStyle().setBackgroundColor("white");
		th.getAnchor().getElement().getStyle().setProperty("textAlign", "center");
		
		screen = new Image();
		
		edit = new Button(TEXT.edit());
		edit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.EDIT_LINK + scheme.getOid());
			}
		});
		edit.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		view = new Button(TEXT.view());
		view.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		
		Style vs = view.getElement().getStyle();
		vs.setFloat(Style.Float.RIGHT);
		
		author = new Badge();
		author.setType(BadgeType.INFO);
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
		
        Tooltip pt = new Tooltip(TEXT.permissions());
        pt.add(permissions);
		
		remove = new Button("", IconType.TRASH);
		remove.setVisible(false);
		Style rs = remove.getElement().getStyle();
		rs.setFloat(Style.Float.RIGHT);
		
        Tooltip rt = new Tooltip(TEXT.remove());
        rt.add(remove);

        th.add(pt);
        th.add(rt);
		th.add(screen);
		th.add(edit);
        th.add(author);
		th.add(view);
		
		initWidget(th);
		
	}
	
	public Scheme getScheme() {
		return scheme;
	}
	
	public Permissions getPermissions() {
		return pdialog.getPermissions();
	}
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
		screen.setUrl(scheme.getSchemePicture());
		author.setText(scheme.getAuthor().getName());
		if (scheme.getPermissions() != null) {
			permissions.setVisible(true);
			remove.setVisible(true);
		}
	}
	
	public HasClickHandlers getRemoveButton() {
		return remove;
	}
	
	public HasClickHandlers getUpdatePermissionsButton() {
		return pdialog.getOk();
	}
	
}
