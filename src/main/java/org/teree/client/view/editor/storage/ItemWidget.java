package org.teree.client.view.editor.storage;

import org.teree.client.view.editor.storage.event.BrowserItemDelete;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public abstract class ItemWidget extends Composite {
	
	private static final int MAX_TITLE_LENGTH = 12;
	
	protected Well container;
	private FlowPanel panel;
	
	private Label title;
	private Button delete;
	
	public ItemWidget() {
		panel = new FlowPanel();
		Style ps = panel.getElement().getStyle();
		ps.setFloat(Style.Float.LEFT);
		ps.setMarginLeft(5, Unit.PX);
		ps.setMarginRight(5, Unit.PX);
		ps.setMarginBottom(-10, Unit.PX);
		container = new Well();
		panel.setWidth(160+"px");
		container.setHeight(110+"px");
		
		title = new Label();
		Style ts = title.getElement().getStyle();
		ts.setFloat(Style.Float.LEFT);
        panel.add(title);
		
		delete = new Button("", IconType.TRASH);
		Style ds = delete.getElement().getStyle();
		ds.setFloat(Style.Float.RIGHT);
		Tooltip dt = new Tooltip("Delete");
        dt.add(delete);
        panel.add(dt);
		
        panel.add(container);
		
		initWidget(panel);
		
		bind();
		
	}
	
	public abstract String getUrl();
	
	public abstract String getItemTitle();
	
	public abstract ItemType getItemType();
	
	private void bind() {
        
        delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getParent().getParent().fireEvent(new BrowserItemDelete(ItemWidget.this));
			}
		});
        
	}
	
	protected void loaded() {
		String t = getItemTitle();
		if (t.length() > MAX_TITLE_LENGTH) {
			t = t.substring(0, MAX_TITLE_LENGTH) + "...";
		}
		title.setText(t);
	}

}
