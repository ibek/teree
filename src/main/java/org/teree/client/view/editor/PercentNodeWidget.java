package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.CheckNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.common.IconText;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.common.Node.NodeType;
import org.teree.shared.data.common.PercentText;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;

/**
 * 
 * Parent of TextNodeWidget has to be AbsolutePanel.
 * 
 * @author ibek
 * 
 */
public class PercentNodeWidget extends NodeWidget {

	private static final int threshold = 20;

	protected HTML content;
	protected TextBox editContent;
	protected ProgressBar percentage;

	protected PercentText nodeContent;

	public PercentNodeWidget(Node node) {
		super(node);

		nodeContent = (PercentText) node.getContent();
		view();

	}

	@Override
	public void edit() {
		if (editContent == null) {
			editContent = new TextBox();
			editContent.setMaxLength(32);

			editContent.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
							&& !event.isShiftKeyDown()) {
						confirmChanges();
					} else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						view();
					}
				}
			});

			editContent.addKeyDownHandler(new KeyDownHandler() {
				@Override
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
							&& !event.isShiftKeyDown()) {
						// don't create new line if it means confirmation
						event.preventDefault();
					}
				}
			});

			editContent.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					confirmChanges();
				}
			});

			editContent.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation(); // prevents from nodewidget call
				}
			});

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					editContent.setFocus(true);
				}
			});

			editContent.setStylePrimaryName(resources.css().node());
			editContent.addStyleName(resources.css().nodeEdit());

		}

		editContent.setText(nodeContent.getText());

		editContent.getElement().getStyle().setPadding(0.0, Unit.PX);
		editContent.getElement().getStyle().setMargin(0.0, Unit.PX);
		editContent.setWidth(content.getOffsetWidth() + "px");
		editContent.setHeight(content.getOffsetHeight() + "px");

		// to ensure that the editContent will be focused after all events (key F2)
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				editContent.setFocus(true);
			}
		});

		container.remove(content);
		container.insert(editContent, 0);

		// only for better look
		getParent().fireEvent(new SelectNode<PercentNodeWidget>(null));

	}

	public void view() {
		if (content == null) {
			content = new HTML(nodeContent.getText());

			content.getElement().setDraggable(Element.DRAGGABLE_TRUE);
			initDragging(content);

			content.setStylePrimaryName(resources.css().node());
			content.addStyleName(resources.css().nodeView());

		}
		container.getElement().getStyle().setMarginLeft(0, Unit.PX);
		container.setWidth("auto");

		if (percentage == null) {
			percentage = new ProgressBar();
			percentage.addDomHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					if (!nodeContainsPercentChild(node)) {
						int percent = (int) ((event.getRelativeX(percentage
								.getElement()) / (double) percentage
								.getOffsetWidth()) * 100);
						percent += threshold / 2;
						percent = (percent / threshold) * threshold;
						nodeContent.setPercentage(percent);
						update();
						updateParentNodes(node.getParent());
						getParent().fireEvent(new CheckNode());
					}
				}
			}, ClickEvent.getType());
			percentage.getElement().getStyle().setMargin(0.0, Unit.PX);
			percentage.getElement().getStyle().setMarginBottom(2.0, Unit.PX);
			percentage.setHeight("10px");
			percentage.getElement().getStyle()
					.setProperty("lineHeight", "10px");
			container.add(percentage);
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					updateParentNodes(node.getParent());
					getParent().fireEvent(new CheckNode());
				}
			});
		}

		update();

		if (editContent != null) {
			container.remove(editContent);
		}

		container.insert(content, 0);

	}

	private boolean nodeContainsPercentChild(Node node) {
		if (node.getChildNodes() == null) {
			return false;
		}
		for (Node childNode : node.getChildNodes()) {
			if (childNode.getType() == NodeType.Percent) {
				return true;
			}
			/**
			 * if (nodeContainsPercentChild(childNode)) { // recursive return true; }
			 */
		}
		return false;
	}

	private void confirmChanges() {
		String newtext = editContent.getText();
		String oldtext = nodeContent.getText();

		if (oldtext == null || newtext.compareTo(oldtext) != 0) {
			nodeContent.setText(newtext);
			getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
		}

		view();
		getParent().fireEvent(new SelectNode<PercentNodeWidget>(this));
	}

	@Override
	public void update() {
		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = "[empty]";
		}
		content.setText(text);
		percentage.setPercent(nodeContent.getPercentage());
		percentage.setText(String.valueOf(nodeContent.getPercentage()) + "%");
	}

	private void updateParentNodes(Node parent) {
		if (parent != null && parent.getType() == NodeType.Percent) {
			int sum = 0;
			int count = 0;
			for (Node childNode : parent.getChildNodes()) {
				if (childNode.getType() == NodeType.Percent) {
					count++;
					sum += ((PercentText) childNode.getContent())
							.getPercentage();
				}
			}
			((PercentText) parent.getContent()).setPercentage(sum / count); // average
			updateParentNodes(parent.getParent()); // recursive
		}
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		context.save();
		context.setFont("14px monospace");
		context.setFillStyle("#000000");

		String text = content.getText();
		int px = x;
		y -= percentage.getOffsetHeight() + 5;

		if (collapsed && !text.startsWith("+")) {
			text = "+" + text;
			px -= context.measureText("+").getWidth();
		}
		context.fillText(text, px, y);

		y += 5;

		context.setFillStyle(CssColor.make("#08C"));
		context.fillRect(x, y,
				percentage.getPercent() / 100.0 * percentage.getOffsetWidth(),
				percentage.getOffsetHeight());

		context.setFillStyle("#FFFFFF");
		context.fillText(String.valueOf(percentage.getPercent()) + "%", x, y
				+ percentage.getOffsetHeight());

		context.restore();
	}

	@Override
	public void setCollapsed(boolean collapsed) {
		if (collapsed) {
			content.setText("+ " + node.getContent().toString());
		} else {
			content.setText(node.getContent().toString());
		}
		this.collapsed = collapsed;
	}

}
