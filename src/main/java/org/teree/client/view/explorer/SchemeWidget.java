package org.teree.client.view.explorer;

import org.teree.client.Settings;
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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

public class SchemeWidget extends Composite {

	private ThumbnailLink th;
	
	private Image screen;
	private Badge author;
	private Button publish;
	private Button remove;
	private Button edit;
	private Button view;
	
	private Scheme scheme;
	
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
		
		edit = new Button("Edit");
		edit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.EDIT_LINK + scheme.getOid());
			}
		});
		edit.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		view = new Button("View");
		view.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		
		Style vs = view.getElement().getStyle();
		vs.setFloat(Style.Float.RIGHT);
		
		publish = new Button("", IconType.GLOBE);
		publish.setVisible(false);
		publish.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		author = new Badge();
		author.setType(BadgeType.INFO);
		author.getElement().getStyle().setProperty("lineHeight", "30px");
		//author.getElement().getStyle().setFloat(Style.Float.RIGHT);
		
		remove = new Button("", IconType.TRASH);
		Style rs = remove.getElement().getStyle();
		rs.setFloat(Style.Float.RIGHT);

		Tooltip pt = new Tooltip("Publish scheme");
        pt.add(publish);
		
        Tooltip rt = new Tooltip("Remove scheme");
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
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
		screen.setUrl(scheme.getSchemePicture());
		if (scheme.getAuthor() != null) {
			author.setText(scheme.getAuthor().getName());
		} else if (scheme.getOwner() != null) {
			author.setText(scheme.getOwner().getName());
		}
	}
	
	public HasClickHandlers getPublishButton() {
		return publish;
	}
	
	public HasClickHandlers getRemoveButton() {
		return remove;
	}
	
	public void enablePublish(boolean state) {
		publish.setVisible(state);
	}
	
}
