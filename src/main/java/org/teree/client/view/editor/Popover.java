package org.teree.client.view.editor;


import com.github.gwtbootstrap.client.ui.base.HoverBase;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class Popover extends HoverBase {

	private Widget content;
	private String heading;

	/**
	 * Creates an empty Popover.
	 */
	public Popover() {
		super();
		placement = Placement.RIGHT;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContent(Widget content) {
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getText() {
		return "";
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}
	
	public String getHeading() {
		return this.heading;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reconfigure() {
		
		removeDataIfExists();
		
		setDataAttribute(getWidget().getElement(), "original-title", heading);
		
		setDataAttribute(getWidget().getElement(), "content", content.getElement().getId());
		
		configure(getWidget().getElement(), getAnimation(), getPlacement().get(),
				getTrigger().get(), getShowDelay(), getHideDelay());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void changeVisibility(VisibilityChange visibilityChange) {
		changeVisibility(getWidget().getElement(), visibilityChange.get());
	}

	private native void configure(Element element, boolean animated,
			String placement, String trigger, int showDelay, int hideDelay) /*-{
		$wnd.jQuery(element).popover({
			animation : animated,
			placement : placement,
			trigger : trigger,
			delay : {
				show : showDelay,
				hide : hideDelay
			}
		});
	}-*/;

	private native void changeVisibility(Element e, String visibility) /*-{
		$wnd.jQuery(e).popover(visibility);
	}-*/;

	@Override
	protected String getDataName() {
		return "popover";
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
}
