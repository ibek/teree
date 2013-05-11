package org.teree.client.view.viewer;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.view.common.NodePainter;
import org.teree.client.view.editor.event.NodeChanged;
import org.teree.client.view.editor.event.SelectNode;
import org.teree.client.view.resource.IconTypeContent;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;
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
import com.google.gwt.dom.client.Style;
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

	protected HTML content;
	protected TextBox editContent;
	protected ProgressBar percentage;

	protected PercentText nodeContent;

	public PercentNodeWidget(Node node) {
		super(node);

		nodeContent = (PercentText) node.getContent();
		view();
		update();
	}

	public void view() {
		if (content == null) {
			content = new HTML(nodeContent.getText());

			content.setStylePrimaryName(resources.css().node());
			content.addStyleName(resources.css().nodeView());

		}
		container.getElement().getStyle().setMarginLeft(0, Unit.PX);
		container.setWidth("auto");

		if (percentage == null) {
			percentage = new ProgressBar();
			percentage.getElement().getStyle().setMargin(0.0, Unit.PX);
			percentage.getElement().getStyle().setMarginBottom(2.0, Unit.PX);
			percentage.setHeight("10px");
			percentage.getElement().getStyle()
					.setProperty("lineHeight", "10px");
			Style sliderStyle = percentage.getWidget(0).getElement().getStyle();
			sliderStyle.setColor("black");
			container.add(percentage);
		}

		String text = nodeContent.getText();
		if (text == null || text.isEmpty()) {
			text = "[empty]";
		}
		content.setText(text);
		percentage.setPercent((int)nodeContent.getPercentage());
		percentage.setText(String.valueOf(nodeContent.getPercentage()) + "%");

		if (editContent != null) {
			container.remove(editContent);
		}

		container.insert(content, 0);

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
