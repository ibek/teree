package org.teree.client.view.editor;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.common.NodeCategoryStyle;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Node.NodeLocation;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.dom.client.Context2d;
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
public class TextNodeWidget extends NodeWidget {

	protected HTML content;
	protected TextArea editContent;

	protected Text nodeContent;

	public TextNodeWidget(Node node) {
		super(node);

		if (node.getContent() instanceof Text) {
			nodeContent = (Text) node.getContent();
			view();
		}

	}

	@Override
	public void edit() {
		if (editContent == null) {
			editContent = new TextArea();

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

		if (getOffsetWidth() > Settings.NODE_DEFAULT_WIDTH) {
			editContent.setWidth((getOffsetWidth() + 4) + "px");
		} else {
			if (node.getLocation() == NodeLocation.LEFT) {
				container
						.getElement()
						.getStyle()
						.setMarginLeft(
								getOffsetWidth() - Settings.NODE_DEFAULT_WIDTH,
								Unit.PX);
			}
			editContent.setWidth(Settings.NODE_DEFAULT_WIDTH + "px");
			container.setWidth(Settings.NODE_DEFAULT_WIDTH + "px");
		}
		if (getOffsetHeight() > Settings.NODE_DEFAULT_HEIGHT) {
			editContent.setHeight(getOffsetHeight() + "px");
		} else {
			editContent.setHeight(Settings.NODE_DEFAULT_HEIGHT + "px");
		}

		if (node.getCategory().getIconType() != null) {
			editContent.getElement().getStyle()
					.setPaddingLeft(Settings.ICON_WIDTH, Unit.PX);
		} else {
			editContent.getElement().getStyle().setPaddingLeft(0.0, Unit.PX);
		}

		// to ensure that the editContent will be focused after all events (key F2)
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				editContent.setFocus(true);
			}
		});

		container.remove(content);
		container.add(editContent);

		// only for better look
		getParent().fireEvent(new SelectNode<TextNodeWidget>(null));

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

		if (editContent != null) {
			container.remove(editContent);
		}

		container.add(content);

		update();

	}

	private void confirmChanges() {
		String newtext = editContent.getText();
		String oldtext = nodeContent.getText();

		if (oldtext == null || newtext.compareTo(oldtext) != 0) {
			nodeContent.setText(newtext);
			getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
		}

		view();
		getParent().fireEvent(new SelectNode<TextNodeWidget>(this));
	}

	@Override
	public void update() {
		super.update();
		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = "[empty]";
		}
		content.setText(text);
		setCollapsed(collapsed);
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		NodePainter.drawTextNode(context, x, y, content.getText(), collapsed, getOffsetWidth(),
				node.getCategory());
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
