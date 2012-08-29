package org.teree.client.view.explorer;

import java.util.List;

import org.teree.client.Settings;
import org.teree.shared.data.Scheme;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Image;

public class Scene extends Composite {

	private FlowPanel container;
	
	private FlexTable table;
	
	public Scene() {
		
		container = new FlowPanel();
		table = new FlexTable();
		table.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cell c = table.getCellForEvent(event);
				String oid = table.getText(c.getRowIndex(), 0);
				History.newItem(Settings.VIEW_LINK+oid);
			}
		});
		container.add(table);
		
		initWidget(container);
	}
	
	public void setData(List<Scheme> slist) {
		table.clear();
		table.setText(0, 0, "oid");
		table.setText(0, 1, "map");
		for(int i=0; i<slist.size(); ++i) {
			Scheme s = slist.get(i);
			table.setText(i+1, 0, s.getOid());
			Image im = new Image(s.getSchemePicture());
			//im.setWidth(200+"px");
			//im.setHeight(150+"px");
			table.setWidget(i+1, 1, im);
		}
	}
	
}
