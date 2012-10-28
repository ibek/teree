package org.teree.client.view.explorer;

import java.util.List;

import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.client.view.explorer.event.RemoveScheme;
import org.teree.client.view.explorer.event.RemoveSchemeHandler;
import org.teree.shared.data.scheme.Scheme;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Pager;
import com.github.gwtbootstrap.client.ui.Thumbnails;
import com.github.gwtbootstrap.client.ui.Well;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Scene extends Composite implements HasSchemeHandlers {

	private VerticalPanel container;
	private Thumbnails schemeContainer;
	private Heading empty;
	private Pager pagerTop;
	private Pager pagerBottom;

	private HandlerManager publishManager;
	private HandlerManager removeManager;
	
	private boolean enablePublish = false;
	
	/**
	 * To prevent unnecessary calls for next and previous page.
	 */
	private int page = 0;
	private boolean lastPage = false;
	
	public Scene() {
		
		container = new VerticalPanel();
		schemeContainer = new Thumbnails();
		schemeContainer.getElement().getStyle().setPaddingLeft(35, Unit.PX);
		schemeContainer.getElement().getStyle().setPaddingRight(5, Unit.PX);

		publishManager = new HandlerManager(schemeContainer);
		removeManager = new HandlerManager(schemeContainer);
		
		pagerTop = new Pager((char)0xf060+" Back", "Next "+(char)0xf061);
		pagerTop.getElement().getStyle().setProperty("fontFamily", "FontAwesome");
		pagerTop.setAligned(true);
		
		pagerBottom = new Pager((char)0xf060+" Back", "Next "+(char)0xf061);
		pagerBottom.getElement().getStyle().setProperty("fontFamily", "FontAwesome");
		pagerBottom.setAligned(true);
		
		empty = new Heading(4, "No scheme to be displayed.");
		
		setComponents(false);

		container.add(pagerTop);
		container.add(empty);
		container.add(schemeContainer);
		container.add(pagerBottom);

		Well w = new Well();
		w.add(container);
		w.setHeight("100%");
		w.getElement().getStyle().setMargin(20, Unit.PX);
		w.getElement().getStyle().setMarginTop(0, Unit.PX);
		w.getElement().getStyle().setMarginBottom(0, Unit.PX);
		initWidget(w);
	}
	
	public void setData(List<Scheme> slist) {
		schemeContainer.clear();
		if (slist == null || slist.size() == 0) {
			setComponents(false);
			lastPage = true;
			// TODO: inform user that it's last page
			return;
		}
		for(int i=0; i<slist.size(); ++i) {
			Scheme s = slist.get(i);
			final SchemeWidget sw = new SchemeWidget();
			sw.enablePublish(enablePublish);
			sw.setScheme(s);
			sw.getPublishButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					publishManager.fireEvent(new PublishScheme(sw.getScheme()));
				}
			});
			sw.getRemoveButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					removeManager.fireEvent(new RemoveScheme(sw.getScheme()));
				}
			});
			schemeContainer.add(sw);
		}
		setComponents(true);
	}
	
	private void setComponents(boolean display) {
		pagerTop.setVisible(display);
		pagerBottom.setVisible(display);
		empty.setVisible(!display);
	}
	
	public String getFirstOid() {
		String res = null;
		if (schemeContainer.getWidgetCount() > 0 && page > 0) {
			lastPage = false;
			page--;
			SchemeWidget sw = (SchemeWidget)schemeContainer.getWidget(0);
			res = sw.getScheme().getOid();
		}
		return res;
	}
	
	public String getLastOid() {
		String res = null;
		int count = 0;
		if ((count = schemeContainer.getWidgetCount()) > 0 && !lastPage) {
			page++;
			SchemeWidget sw = (SchemeWidget)schemeContainer.getWidget(count-1);
			res = sw.getScheme().getOid();
		}
		return res;
	}

	public HasClickHandlers getNextButton() {
		return pagerBottom.getRight();
	}

	public HasClickHandlers getPreviousButton() {
		return pagerBottom.getLeft();
	}
	
	public void enablePublish(boolean state){
		this.enablePublish = state;
	}

	@Override
	public HandlerRegistration addPublishHandler(PublishSchemeHandler handler) {
		return publishManager.addHandler(PublishScheme.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRemoveHandler(RemoveSchemeHandler handler) {
		return removeManager.addHandler(RemoveScheme.TYPE, handler);
	}
	
}
