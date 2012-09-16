package org.teree.client.view.explorer;

import java.util.List;

import org.teree.client.view.SchemeWidget;
import org.teree.client.view.explorer.event.HasPublishSchemeHandlers;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.shared.data.Scheme;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Scene extends Composite implements HasPublishSchemeHandlers {

	private HorizontalPanel container;
	private FlowPanel schemeContainer;
	private Button next;
	private Button previous;
	
	private HandlerManager publishManager;
	
	private boolean enablePublish = false;
	
	/**
	 * To prevent unnecessary calls for next and previous page.
	 */
	private int page = 0;
	private boolean lastPage = false;
	
	public Scene() {
		
		container = new HorizontalPanel();
		schemeContainer = new FlowPanel();
		schemeContainer.getElement().getStyle().setPaddingLeft(5, Unit.PX);
		schemeContainer.getElement().getStyle().setPaddingRight(5, Unit.PX);
		
		publishManager = new HandlerManager(schemeContainer);
		
		next = new Button("", IconType.CHEVRON_RIGHT);
		next.setHeight("100%");
		previous = new Button("", IconType.CHEVRON_LEFT);
		previous.setHeight("100%");
		
		container.add(previous);
		container.add(schemeContainer);
		container.add(next);
		
		initWidget(container);
	}
	
	public void setData(List<Scheme> slist) {
		if (slist.size() == 0) {
			lastPage = true;
			// TODO: inform user that it's last page
			return;
		}
		schemeContainer.clear();
		for(int i=0; i<slist.size(); ++i) {
			Scheme s = slist.get(i);
			final SchemeWidget sw = new SchemeWidget();
			sw.enablePublish(enablePublish);
			sw.setScheme(s);
			sw.getPublishButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					publishManager.fireEvent(new PublishScheme(sw.getScheme()));
				}
			});
			schemeContainer.add(sw);
		}
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
		return next;
	}

	public HasClickHandlers getPreviousButton() {
		return previous;
	}
	
	public void enablePublish(boolean state){
		this.enablePublish = state;
	}

	@Override
	public HandlerRegistration addPublishHandler(PublishSchemeHandler handler) {
		return publishManager.addHandler(PublishScheme.TYPE, handler);
	}
	
}
