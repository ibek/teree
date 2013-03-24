package org.teree.client.view.editor;

import org.teree.client.view.common.ColorPicker;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.FormActions;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class NodeCategoryDialog extends Composite {

	private Modal window;
	
	private WellForm form;
	
	private Button save;
	private Button reset;
	
	private TextBox name;
	private CheckBox bold;
	private String color;
	private Button colorButton;
	private ColorPicker colorPicker;
	
	private NodeCategory original;
	private TextNodeWidget preview;
	
	public NodeCategoryDialog() {
		window = new Modal(false);
		
		form = new WellForm();
		form.setType(FormType.HORIZONTAL);
		Fieldset fs = new Fieldset();
		fs.add(createName());
		fs.add(createBold());
		fs.add(createFGColor());
		form.add(fs);
		
		reset = new Button("Reset");
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setNodeCategory(original);
			}
		});
		save = new Button("Save");
		save.setType(ButtonType.PRIMARY);
		FormActions fa = new FormActions();
		fa.add(reset);
		fa.add(save);
		form.add(fa);
		
		window.add(form);
		
		Node n = new Node();
		n.setCategory(new NodeCategory());
		IconText it = new IconText();
		it.setText("Test node");
		n.setContent(it);
		preview = new TextNodeWidget(n);
		DOM.setStyleAttribute(preview.getElement(), "visibility", "visible");
		
		Well wp = new Well();
		wp.add(preview);
		
		window.add(wp);
		
	}
	
	public void show() {
		window.show();
	}
	
	public void hide() {
		window.hide();
	}
	
	public void setNodeCategory(NodeCategory nc) {
		this.original = nc;
		if (nc == null) {
			window.setTitle("Add node category");
			nc = new NodeCategory();
		} else {
			window.setTitle("Edit node category");
		}
		name.setText(nc.getName());
		bold.setValue(nc.isBold());
		color = nc.getColor();
		colorButton.getElement().getStyle().setColor(color);
		updatePreview();
	}
	
	public NodeCategory getNodeCategory() {
		NodeCategory nc = new NodeCategory();
		nc.set(original);
		nc.setName(name.getText());
		nc.setBold(bold.getValue());
		nc.setColor(color);
		return nc;
	}
	
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	private ControlGroup createName() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Name"));
		cg.add(cl);
		Controls controls = new Controls();
		name = new TextBox();
		name.setMaxLength(16);
		controls.add(name);
		cg.add(controls);
		return cg;
	}
	
	private ControlGroup createBold() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Bold"));
		cg.add(cl);
		Controls controls = new Controls();
		bold = new CheckBox();
		bold.setValue(false);
		bold.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				updatePreview();
			}
		});
		controls.add(bold);
		cg.add(controls);
		return cg;
	}
	
	private ControlGroup createFGColor() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Foreground color"));
		cg.add(cl);
		Controls controls = new Controls();
		colorPicker = new ColorPicker();
		colorButton = new Button("", IconType.SIGN_BLANK);
		
		colorPicker.addColorHandler(new ColorPicker.ColorHandler() {
			@Override
			public void newColorSelected(String color) {
				NodeCategoryDialog.this.color = color;
				colorButton.getElement().getStyle().setColor(color);
				updatePreview();
			}
		});
		colorButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				colorPicker.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = colorButton.getAbsoluteLeft() - offsetWidth + colorButton.getOffsetWidth();
		                int top = colorButton.getAbsoluteTop() + colorButton.getOffsetHeight();
		                colorPicker.setPopupPosition(left, top);
					}
				});
			}
		});
		controls.add(colorButton);
		cg.add(controls);
		return cg;
	}
	
	private void updatePreview() {
		preview.getNode().setCategory(getNodeCategory());
		preview.update();
	}
	
}
