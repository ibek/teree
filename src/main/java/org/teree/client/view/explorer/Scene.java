package org.teree.client.view.explorer;

import java.util.List;

import org.teree.shared.data.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

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
				String oid = c.getElement().getFirstChild().getNodeValue();
				History.newItem("view/oid="+oid);
			}
		});
		container.add(table);
		
		initWidget(container);
	}
	
	public void setData(List<Map> maps) {
		table.clear();
		table.setText(0, 0, "oid");
		table.setText(0, 1, "root");
		for(int i=0; i<maps.size(); ++i) {
			Map m = maps.get(i);
			table.setText(i+1, 0, m.getOid());
			table.setText(i+1, 1, m.getRootContent().toString()); // TODO: enable for another types
		}
	}
	
}
