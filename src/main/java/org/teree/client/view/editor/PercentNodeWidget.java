package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.editor.event.CheckNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.common.Node.NodeType;
import org.teree.shared.data.common.PercentText;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
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
	protected InlineLabel group;

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
		container.insert(editContent, container.getWidgetIndex(group));

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
			content.getElement().getStyle()
			.setPaddingRight(Settings.ICON_WIDTH, Unit.PX);

			group = new InlineLabel();
			group.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					int group = nodeContent.getGroup();
					group++;
					if (group > 9) {
						group = -1;
					}
					nodeContent.setGroup(group);
					update();
				}
			});
			container.add(group, 0, 0);
			Style gstyle = group.getElement().getStyle();
			gstyle.setZIndex(100);
			gstyle.clearLeft();
			gstyle.setRight(0, Unit.PX);
			gstyle.setTop(0, Unit.PX);
			gstyle.setProperty("borderLeft", "2px #08C solid");
			gstyle.setProperty("borderBottom", "2px #08C solid");
			gstyle.setPaddingLeft(3.0, Unit.PX);
			gstyle.setMarginTop(3.0, Unit.PX);
			gstyle.setCursor(Cursor.POINTER);
			
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
						updateGroupNodes(node);
						getParent().fireEvent(new CheckNode());
					}
				}
			}, ClickEvent.getType());
			Style pstyle = percentage.getElement().getStyle();
			pstyle.setMargin(0.0, Unit.PX);
			pstyle.setMarginBottom(2.0, Unit.PX);
			percentage.setHeight("10px");
			pstyle.setProperty("lineHeight", "10px");
			Style sliderStyle = percentage.getWidget(0).getElement().getStyle();
			sliderStyle.setColor("black");
			container.add(percentage);
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					updateParentNodes(node.getParent());
					getParent().fireEvent(new CheckNode());
				}
			});
		}

		if (editContent != null) {
			container.remove(editContent);
		}

		container.insert(content, container.getWidgetIndex(group));
		update();

	}

	private boolean nodeContainsPercentChild(Node node) {
		if (node.getChildNodes() == null) {
			return false;
		}
		for (Node childNode : node.getChildNodes()) {
			if (childNode.getType() == NodeType.Percent && ((PercentText) childNode.getContent()).getGroup() < 0) {
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
		super.update();
		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = "[empty]";
		}
		content.setText(text);
		percentage.setPercent((int)nodeContent.getPercentage());
		percentage.setText(String.valueOf((int)nodeContent.getPercentage()) + "%");
		int g = nodeContent.getGroup();
		String gs = "";
		if (g > -1) {
			gs += g;
		} else {
			gs += "–";
		}
		group.setText(gs);
	}

	private void updateParentNodes(Node parent) {
		if (parent != null && parent.getType() == NodeType.Percent) {
			double sum = 0;
			int count = 0;
			for (Node childNode : parent.getChildNodes()) {
				if (childNode.getType() == NodeType.Percent && ((PercentText) childNode.getContent()).getGroup() < 0) {
					count++;
					sum += ((PercentText) childNode.getContent())
							.getPercentage();
				}
			}
			PercentText pt = (PercentText) parent.getContent();
			if (count > 0) {
				pt.setPercentage(sum / count); // average
			}
			updateParentNodes(parent.getParent()); // recursive
			updateGroupNodes(parent); // recursive for groups
		}
	}
	
	private void updateGroupNodes(Node changed) {
		PercentText changedContent = (PercentText)changed.getContent();
		int group = changedContent.getGroup();
		if (group >= 0) {
			double diff = 100 - changedContent.getPercentage();
			double sum = 0.0;
			List<PercentText> ptlist = new ArrayList<PercentText>();
			for (Node childNode : changed.getParent().getChildNodes()) {
				if (childNode.getType() == NodeType.Percent && !changed.equals(childNode) && ((PercentText) childNode.getContent()).getGroup() == group) {
					PercentText pt = (PercentText) childNode.getContent();
					ptlist.add(pt);
					sum += pt.getPercentage()/diff;
				}
			}
			for (PercentText pt : ptlist) {
				if (sum > 0.0) {
					pt.setPercentage(pt.getPercentage() / sum);
				} else {
					pt.setPercentage(diff / ptlist.size());
				}
			}
		}
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		NodePainter.drawPercentNode(context, x, y, content.getText(),
				percentage.getPercent(), collapsed,
				percentage.getOffsetWidth(), percentage.getOffsetHeight(),
				getOffsetWidth(), getOffsetHeight(), node.getCategory());
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
