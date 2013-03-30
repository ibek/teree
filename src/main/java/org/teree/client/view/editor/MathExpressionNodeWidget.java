package org.teree.client.view.editor;

import org.teree.client.Settings;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.MathExpressionTools;
import org.teree.shared.data.common.MathExpression;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;

public class MathExpressionNodeWidget extends NodeWidget {

	private HTML content;
	private TextArea editContent;

	private MathExpression nodeContent;

	public MathExpressionNodeWidget(Node node) {
		super(node);

		nodeContent = (MathExpression) node.getContent();

		view();

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

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					editContent.setFocus(true);
				}
			});

			editContent.setStylePrimaryName(resources.css().node());
			editContent.addStyleName(resources.css().nodeEdit());

		}

		editContent.setText(nodeContent.getExpression());

		if (getOffsetWidth() > Settings.NODE_DEFAULT_WIDTH) {
			editContent.setWidth(Settings.NODE_DEFAULT_WIDTH + "px");
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
		getParent().fireEvent(new SelectNode<MathExpressionNodeWidget>(null));

	}

	public void view() {
		if (content == null) {

			content = new HTML();

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
		String newexpr = editContent.getText();

		boolean needUpdate = false;
		if (newexpr.compareTo(nodeContent.getExpression()) != 0) {
			nodeContent.setExpression(newexpr);
			needUpdate = true;
		}

		view();
		if (needUpdate) {
			Timer t = new Timer() {
				@Override
				public void run() {
					getParent().fireEvent(new NodeChanged(null)); // null because nothing was inserted
				}
			};
			t.schedule(1000);
		}

		getParent().fireEvent(new SelectNode<MathExpressionNodeWidget>(this));
	}

	@Override
	public void update() {
		super.update();
		String expr = nodeContent.getExpression();
		if (expr.isEmpty()) {
			expr = "[empty]";
		}
		content.getElement().setInnerHTML(
				SafeHtmlUtils.htmlEscape("\\[" + expr + "\\]"));
		MathExpressionTools.renderLatexResult(content.getElement());
	}

	@Override
	public void draw(Context2d context, int x, int y) {
		NodeList<Element> nl = content.getElement().getElementsByTagName("div");
		if (nl.getLength() > 0) {
			Element e = nl.getItem(0);
			NodePainter.drawSingleLine(context, x, y, e.getInnerText(),
					getOffsetWidth(), getOffsetHeight(), node.getCategory());
		}
	}

}
