package org.teree.client.view.editor;

import java.util.List;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.CurrentPresenter;
import org.teree.client.presenter.Editor;
import org.teree.client.text.UIConstants;
import org.teree.client.view.common.TDialog;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Scheme;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConnectorDialog extends TDialog {

	private static final int WIDTH = 226;
	private static final int HEIGHT = 100;
	
	private Button next;
	private Button back;
	private Button okButton;
	private Button search;

	private Image preview;
	private TextBox oid;
	
	private Scheme scheme;
	
	private List<Scheme> searched;
	private int current = 0;

	public ConnectorDialog() {
		
		setTitle("Set Connector");

		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(0.0, Unit.PX);
		//panel.setWidth(WIDTH+"px");
		panel.setHeight(HEIGHT+"px");
		
		preview = new Image();
		//preview.setWidth(Settings.SAMPLE_MAX_WIDTH + "px");
		//preview.setHeight(Settings.SAMPLE_MAX_HEIGHT + "px");
		panel.add(preview);

		FlowPanel fp = new FlowPanel();
		back = new Button(UIConstants.LANG.back());
		//back.getElement().getStyle().setFloat(Float.LEFT);
		back.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				back();
			}
		});
		next = new Button(UIConstants.LANG.next());
		next.getElement().getStyle().setFloat(Float.RIGHT);
		next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				next();
			}
		});
		fp.add(back);
		fp.add(next);
		panel.add(fp);

		HorizontalPanel oidhp = new HorizontalPanel();
		oidhp.getElement().getStyle().setMarginTop(10, Unit.PX);
		oid = new TextBox();
		oid.setPlaceholder("oid or root to search");
		oid.setWidth(200+"px");

		search = new Button("", IconType.SEARCH);
		search.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				checkOid();
			}
		});
		oidhp.add(oid);
		oidhp.add(search);
		panel.add(oidhp);
		
		okButton = new Button();
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!isReady()) {
					event.stopPropagation();
					checkOid();
				}
			}
		});
		setOkButton();
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ConnectorDialog.this.hide();
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(okButton);
		buttons.add(cancel);
		
		panel.add(buttons);
		
		setWidget(panel);

	}
	
	private void checkOid() {
		String t = oid.getText();
		if (oid.getText().isEmpty()) {
			// error - oid should be set
		} else if (t.length() == 25 && !t.contains(" ")) { // it is oid
			load(oid.getText());
		} else {
			((Editor)CurrentPresenter.getInstance().getPresenter()).searchFrom(null, t, new RemoteCallback<List<Scheme>>() {
				@Override
				public void callback(List<Scheme> response) {
					if (response.isEmpty()) {
						clearDialog();
					} else {
						boolean canNext = response.size() > 1;
						next.setVisible(canNext);
						back.setVisible(canNext);
						searched = response;
						current = 0;
						loadCurrent();
					}
					setOkButton();
				}
			});
		}
	}
	
	private void next() {
		current++;
		if (current >= searched.size()) {
			current = 0;
		}
		loadCurrent();
	}
	
	private void back() {
		current--;
		if (current < 0) {
			current = searched.size()-1;
		}
		loadCurrent();
	}
	
	private void loadCurrent() {
		if (searched != null) {
			scheme = searched.get(current);
			preview.setVisible(true);
			if (scheme.getSchemePicture() != null) {
				preview.setUrl(scheme.getSchemePicture());
			}
		}
	}
	
	private void setOkButton() {
		if (isReady()) {
			okButton.setText("Ok");
		} else {
			okButton.setText("Check");
		}
	}
	
	public void clearDialog() {
		searched = null;
		scheme = null;
		preview.setVisible(false);
		next.setVisible(false);
		back.setVisible(false);
	}
	
	private void load(String o) {
		if (o != null && !o.isEmpty()) {
			CurrentPresenter.getInstance().getPresenter().getScheme(o, new RemoteCallback<Scheme>() {
				@Override
				public void callback(Scheme response) {
					scheme = response;
					if (response != null) {
						preview.setVisible(true);
						if (scheme.getSchemePicture() != null) {
							preview.setUrl(scheme.getSchemePicture());
						}
					}
					setOkButton();
				}
			});
		}
	}
	
	public boolean isReady() {
		return scheme != null;
	}
	
	public HasClickHandlers getOk() {
		return okButton;
	}
	
	public IconText getRoot() {
		IconText it = null;
		if (scheme != null) {
			it = new IconText();
			it.setText(scheme.toString());
		}
		return it;
	}
	
	public String getOid() {
		return (scheme == null)?null:scheme.getOid();
	}

	public void setOid(String oid) {
		this.oid.setText(oid);
		load(oid);
	}

}
