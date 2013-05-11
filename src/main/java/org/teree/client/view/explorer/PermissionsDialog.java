package org.teree.client.view.explorer;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.view.common.PopupPanel;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.common.Permissions;
import org.teree.shared.data.common.UserPermissions;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PermissionsDialog extends PopupPanel {

	private static final int WIDTH = 210;
	private static final int HEIGHT = 100;
	private static final int WIDGET_WIDTH = 90;
	
	private VerticalPanel panel;
	private Button okButton;
	private ListBox global;
	private FlexTable userft;
	
	private int selectedRow = -1;

	public PermissionsDialog() {
		
		setTitle("Set Permissions");

		panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(9.0, Unit.PX);
		panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		init();
		
		setWidget(panel);

	}
	
	private void init() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Label("Global: "));
		global = new ListBox();
		global.setWidth(150 + "px");
		global.getElement().getStyle().setMargin(5, Unit.PX);
		global.getElement().getStyle().setMarginTop(0, Unit.PX);
		global.addItem("None", "None");
		global.addItem("Read", "Read");
		global.addItem("Write", "Write");
		hp.add(global);
		panel.add(hp);
		
		hp = new HorizontalPanel();
		hp.add(new Label("Users: "));
		Button addUser = new Button("", IconType.PLUS);
		addUser.getElement().getStyle().setMargin(5, Unit.PX);
		addUser.getElement().getStyle().setMarginTop(0, Unit.PX);
		hp.add(addUser);
		Button removeUser = new Button("", IconType.MINUS);
		removeUser.getElement().getStyle().setMargin(5, Unit.PX);
		removeUser.getElement().getStyle().setMarginTop(0, Unit.PX);
		hp.add(removeUser);
		panel.add(hp);
		
		userft = new FlexTable();
		userft.setWidth(WIDTH + "px");
		panel.add(userft);
		
		okButton = new Button("Ok");
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PermissionsDialog.this.hide();
			}
		});
		
		addUser.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int row = userft.getRowCount();
				if (row == 0) {
					row++;
				}
				addUserPermissions(null, row);
			}
		});
		
		userft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int sr = userft.getCellForEvent(event).getRowIndex();
				if (sr == 0) { // cannot select header
					return;
				}
				if (selectedRow > 0) {
					userft.getRowFormatter().getElement(selectedRow).getStyle().setBackgroundColor("white");
				}
				selectedRow = sr; 
				userft.getRowFormatter().getElement(selectedRow).getStyle().setBackgroundColor("#0081c2");
			}
		});
		
		removeUser.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedRow > 0) {
					userft.removeRow(selectedRow);
					if (userft.getRowCount() == 1) {
						userft.removeAllRows();
					}
					selectedRow--;
					if (selectedRow > 0) {
						userft.getRowFormatter().getElement(selectedRow).getStyle().setBackgroundColor("#0081c2");
					}
				}
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(okButton);
		buttons.add(cancel);
		
		panel.add(buttons);
	}
	
	@Override
	public int getOffsetWidth() {
		return WIDTH;
	}
	
	@Override
	public int getOffsetHeight() {
		return HEIGHT;
	}
	
	public HasClickHandlers getOk() {
		return okButton;
	}
	
	@Override
	public void show() {
		super.show();
	}

	public Permissions getPermissions() {
		Permissions p = new Permissions();
		switch (global.getSelectedIndex()) {
			case 0: {
				p.setWrite(null);
				break;
			}
			case 1: {
				p.setWrite(false);
				break;
			}
			case 2: {
				p.setWrite(true);
				break;
			}
		}
		List<UserPermissions> lup = new ArrayList<UserPermissions>();
		for (int i=1; i<userft.getRowCount(); ++i) {
			UserPermissions up = new UserPermissions();
			UserInfo ui = new UserInfo();
			
			TextBox tb = (TextBox)userft.getWidget(i, 0);
			ui.setEmail(tb.getText());
			
			ListBox lb = (ListBox)userft.getWidget(i, 1);
			switch (lb.getSelectedIndex()) {
				case 0: {
					up.setWrite(null);
					break;
				}
				case 1: {
					up.setWrite(false);
					break;
				}
				case 2: {
					up.setWrite(true);
					break;
				}
			}
			up.setUser(ui);
			lup.add(up);
		}
		p.setUsers(lup);
		return p;
	}
	
	public void setPermissions(Permissions permissions) {
		if (permissions == null) {
			return;
		}
		if (permissions.getWrite() == null) {
			global.setSelectedIndex(0);
		} else if (!permissions.getWrite()) {
			global.setSelectedIndex(1);
		} else {
			global.setSelectedIndex(2);
		}
		userft.removeAllRows();
		List<UserPermissions> up = permissions.getUsers();
		int upsize = up.size();
		for (int i=0; up != null && i < upsize; ++i) {
			addUserPermissions(up.get(i), i+1);
		}
	}
	
	private void addUserPermissions(UserPermissions u, int row) {
		if (userft.getRowCount() == 0) {
			userft.setWidget(0, 0, new Label("Email"));
			userft.setWidget(0, 1, new Label("Permissions"));
		}
		TextBox tb = new TextBox();
		tb.getElement().getStyle().setMargin(5, Unit.PX);
		tb.setWidth(WIDGET_WIDTH + "px");
		if (u != null) {
			tb.setText(u.getUser().getEmail());
		}
		userft.setWidget(row, 0, tb);
		ListBox lb = new ListBox();
		lb.getElement().getStyle().setMargin(5, Unit.PX);
		lb.setWidth(WIDGET_WIDTH + "px");
		lb.addItem("None", "None");
		lb.addItem("Read", "Read");
		lb.addItem("Write", "Write");
		if (u != null) {
			if (u.getWrite() == null) {
				lb.setSelectedIndex(0);
			} else if (!u.getWrite()) {
				lb.setSelectedIndex(1);
			} else {
				lb.setSelectedIndex(2);
			}
		}
		userft.setWidget(row, 1, lb);
	}

}
