package org.teree.client.view.explorer;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.Settings;
import org.teree.client.text.Explorer;
import org.teree.client.view.explorer.event.HasSchemeHandlers;
import org.teree.client.view.explorer.event.PublishScheme;
import org.teree.client.view.explorer.event.PublishSchemeHandler;
import org.teree.client.view.explorer.event.RemoveScheme;
import org.teree.client.view.explorer.event.RemoveSchemeHandler;
import org.teree.client.view.explorer.event.UpdateSchemePermissions;
import org.teree.client.view.explorer.event.UpdateSchemePermissionsHandler;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.scheme.Scheme;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Pager;
import com.github.gwtbootstrap.client.ui.Thumbnails;
import com.github.gwtbootstrap.client.ui.Well;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
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
	private MultipleButtonHandler next;
	private MultipleButtonHandler prev;

	private HandlerManager removeManager;
	private HandlerManager updatePermissionsManager;
	
	/**
	 * To prevent unnecessary calls for next and previous page.
	 */
	private int page = 0;
	private boolean lastPage = false;
	
	private Explorer TEXT = Explorer.LANG;
	
	public Scene() {
		
		container = new VerticalPanel();
		schemeContainer = new Thumbnails();
		schemeContainer.getElement().getStyle().setPaddingLeft(35, Unit.PX);
		schemeContainer.getElement().getStyle().setPaddingRight(5, Unit.PX);

		removeManager = new HandlerManager(schemeContainer);
		updatePermissionsManager = new HandlerManager(schemeContainer);
		
		pagerTop = new Pager((char)0xf060+" "+TEXT.back(), TEXT.next()+" "+(char)0xf061);
		pagerTop.getElement().getStyle().setProperty("fontFamily", "FontAwesome");
		pagerTop.setAligned(true);
		
		pagerBottom = new Pager((char)0xf060+" "+TEXT.back(), TEXT.next()+" "+(char)0xf061);
		pagerBottom.getElement().getStyle().setProperty("fontFamily", "FontAwesome");
		pagerBottom.setAligned(true);
		
		next = new MultipleButtonHandler();
		next.addButton(pagerTop.getRight());
		next.addButton(pagerBottom.getRight());
		prev = new MultipleButtonHandler();
		prev.addButton(pagerTop.getLeft());
		prev.addButton(pagerBottom.getLeft());
		
		empty = new Heading(4, TEXT.no_scheme());
		
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
		if (slist == null || slist.size() == 0) {
			if (page == 0) {
				setComponents(false);
			} else {
				page--;
			}
			lastPage = true;
			// TODO: inform user that it's last page
			pagerTop.getRight().setVisible(false);
			pagerBottom.getRight().setVisible(false);
			return;
		}
		schemeContainer.clear();
		for(int i=0; i<slist.size(); ++i) {
			Scheme s = slist.get(i);
			final SchemeWidget sw = new SchemeWidget();
			sw.setScheme(s);
			sw.getRemoveButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					removeManager.fireEvent(new RemoveScheme(sw.getScheme()));
				}
			});
			sw.getUpdatePermissionsButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					if (!sw.getScheme().getPermissions().equals(sw.getPermissions())) {
						sw.getScheme().setPermissions(sw.getPermissions());
						updatePermissionsManager.fireEvent(new UpdateSchemePermissions(sw.getScheme()));
					}
				}
			});
			schemeContainer.add(sw);
		}
		setComponents(true);
	}
	
	private void setComponents(boolean display) {
		pagerTop.setVisible(display);
		pagerBottom.setVisible(display);
		pagerTop.getLeft().setVisible(page != 0);
		pagerBottom.getLeft().setVisible(page != 0);
		pagerTop.getRight().setVisible(!lastPage && schemeContainer.getWidgetCount() == Settings.SCHEME_COUNT_IN_EXPLORER);
		pagerBottom.getRight().setVisible(!lastPage && schemeContainer.getWidgetCount() == Settings.SCHEME_COUNT_IN_EXPLORER);
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
		return next;
	}

	public HasClickHandlers getPreviousButton() {
		return prev;
	}

	@Override
	public HandlerRegistration addRemoveHandler(RemoveSchemeHandler handler) {
		return removeManager.addHandler(RemoveScheme.TYPE, handler);
	}

	@Override
	public HandlerRegistration addUpdatePermissionsHandler(
			UpdateSchemePermissionsHandler handler) {
		return updatePermissionsManager.addHandler(UpdateSchemePermissions.TYPE, handler);
	}
	
	private class MultipleButtonHandler implements HasClickHandlers {
		
		List<HasClickHandlers> handlers = new ArrayList<HasClickHandlers>(); 
		private ClickHandler ch;
		
		@Override
		public void fireEvent(GwtEvent<?> event) {
			for (HasClickHandlers h: handlers) {
				h.fireEvent(event);
			}
		}
		
		public void addButton(HasClickHandlers button) {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (ch != null) {
						ch.onClick(event);
					}
				}
			});
			handlers.add(button);
		}
		
		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			ch = handler;
			return null;
		}
	};
	
}
