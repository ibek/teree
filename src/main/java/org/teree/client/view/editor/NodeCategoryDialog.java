package org.teree.client.view.editor;

import org.teree.client.view.common.ColorPicker;
import org.teree.client.view.validate.FormValidator;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.FormActions;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class NodeCategoryDialog extends Composite {

	private static final int threshold = 10;

	private Modal window;
	private ScrollPanel sp;

	private WellForm form;
	private Well previewPanel;

	private Button save;
	private Button reset;

	private TextBox name;
	private ControlGroup nameCG;
	private HelpInline nameHI;
	
	private CheckBox bold;
	private ProgressBar transparency;

	private String color;
	private Button colorButton;
	private ColorPicker colorPicker;
	private String background;
	private Button backgroundButton;
	private ColorPicker backgroundPicker;

	private NodeCategory original;
	private TextNodeWidget preview;
	
    private FormValidator validator;

	public NodeCategoryDialog() {
		window = new Modal(false);

		form = new WellForm();
		validator = new FormValidator();
		form.setType(FormType.HORIZONTAL);
		Fieldset fs = new Fieldset();
		nameCG = createName();
		fs.add(nameCG);
		fs.add(createBold());
		fs.add(createTransparency());
		fs.add(createColor());
		fs.add(createBackground());
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
		
		sp = new ScrollPanel();
		sp.add(form);

		window.add(sp);

		Node n = new Node();
		n.setCategory(new NodeCategory());
		IconText it = new IconText();
		it.setText("Test node");
		n.setContent(it);
		preview = new TextNodeWidget(n);
		DOM.setStyleAttribute(preview.getElement(), "visibility", "visible");

		previewPanel = new Well();
		previewPanel.add(preview);

		window.add(previewPanel);
		
		FormActions fa = new FormActions();
		fa.add(reset);
		fa.add(save);
		window.add(fa);

	}

	public void show() {
		window.show();
		sp.setHeight((window.getOffsetHeight() - previewPanel.getOffsetHeight() - 220) + "px");
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
		transparency.setPercent(nc.getTransparency());
		color = nc.getColor();
		colorButton.getElement().getStyle().setColor(color);
		background = nc.getBackground();
		backgroundButton.getElement().getStyle().setColor(background);
		updatePreview();
	}

	public NodeCategory getNodeCategory() {
		if (!validate()) {
			return null;
		}
		NodeCategory nc = new NodeCategory();
		nc.set(original);
		nc.setName(name.getText());
		nc.setBold(bold.getValue());
		nc.setTransparency(transparency.getPercent());
		nc.setColor(color);
		nc.setBackground(background);
		return nc;
	}

	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	private boolean validate() {
		boolean hasError = false;
		hasError = hasError || validator.validateUsername(this.name.getText(), nameCG, nameHI);
		return !hasError;
	}

	private ControlGroup createName() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Name"));
		cg.add(cl);
		Controls controls = new Controls();
		name = new TextBox();
		name.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				updatePreview();
			}
		});
		name.setMaxLength(16);
		controls.add(name);
		nameHI = new HelpInline();
		controls.add(nameHI);
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

	private ControlGroup createTransparency() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Transparency"));
		cg.add(cl);
		Controls controls = new Controls();
		transparency = new ProgressBar();
		transparency.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				int percent = (int) ((event.getRelativeX(transparency
						.getElement()) / (double) transparency.getOffsetWidth()) * 100);
				percent += threshold / 2;
				percent = (percent / threshold) * threshold;
				transparency.setPercent(percent);
				updatePreview();
			}
		}, ClickEvent.getType());
		controls.add(transparency);
		cg.add(controls);
		return cg;
	}

	private ControlGroup createColor() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Foreground"));
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
				colorPicker
						.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
							@Override
							public void setPosition(int offsetWidth,
									int offsetHeight) {
								int left = colorButton.getAbsoluteLeft()
										- offsetWidth
										+ colorButton.getOffsetWidth();
								int top = colorButton.getAbsoluteTop()
										+ colorButton.getOffsetHeight();
								colorPicker.setPopupPosition(left, top);
							}
						});
			}
		});
		controls.add(colorButton);
		cg.add(controls);
		return cg;
	}

	private ControlGroup createBackground() {
		ControlGroup cg = new ControlGroup();
		ControlLabel cl = new ControlLabel();
		cl.add(new Label("Background"));
		cg.add(cl);
		Controls controls = new Controls();
		backgroundPicker = new ColorPicker();
		backgroundButton = new Button("", IconType.SIGN_BLANK);

		backgroundPicker.addColorHandler(new ColorPicker.ColorHandler() {
			@Override
			public void newColorSelected(String color) {
				NodeCategoryDialog.this.background = color;
				backgroundButton.getElement().getStyle().setColor(color);
				updatePreview();
			}
		});
		backgroundButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				backgroundPicker
						.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
							@Override
							public void setPosition(int offsetWidth,
									int offsetHeight) {
								int left = backgroundButton.getAbsoluteLeft()
										- offsetWidth
										+ backgroundButton.getOffsetWidth();
								int top = backgroundButton.getAbsoluteTop()
										+ backgroundButton.getOffsetHeight();
								backgroundPicker.setPopupPosition(left, top);
							}
						});
			}
		});
		controls.add(backgroundButton);
		cg.add(controls);
		return cg;
	}

	private void updatePreview() {
		preview.getNode().setCategory(getNodeCategory());
		preview.update();
	}

}
