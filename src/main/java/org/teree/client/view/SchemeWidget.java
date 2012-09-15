package org.teree.client.view;

import org.teree.client.Settings;
import org.teree.shared.data.Scheme;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class SchemeWidget extends Composite {

	private FlowPanel panel;
	private Well w;
	private Image screen;
	private Button remove;
	private Button edit;
	private Button view;
	
	private Scheme scheme;
	
	public SchemeWidget() {
		
		panel = new FlowPanel();
		Style ps = panel.getElement().getStyle();
		ps.setFloat(Style.Float.LEFT);
		ps.setMarginLeft(5, Unit.PX);
		ps.setMarginRight(5, Unit.PX);
		ps.setMarginBottom(-10, Unit.PX);
		w = new Well();
		panel.setWidth(Settings.SAMPLE_MAX_WIDTH+"px");
		w.setHeight((Settings.SAMPLE_MAX_HEIGHT)+"px");
		
		screen = new Image();
		remove = new Button("", IconType.TRASH);
		Style rs = remove.getElement().getStyle();
		rs.setFloat(Style.Float.RIGHT);
		
		edit = new Button("Edit");
		edit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.EDIT_LINK + scheme.getOid());
			}
		});
		
		view = new Button("View");
		view.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(Settings.VIEW_LINK + scheme.getOid());
			}
		});
		
		Style vs = view.getElement().getStyle();
		vs.setFloat(Style.Float.RIGHT);
		
		panel.add(remove);
		w.add(screen);
		w.add(edit);
		w.add(view);
		panel.add(w);
		
		initWidget(panel);
		
	}
	
	public Scheme getScheme() {
		return scheme;
	}
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
		screen.setUrl(scheme.getSchemePicture());
	}
	
}
