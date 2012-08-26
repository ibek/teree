package org.teree.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.event.MapReceived;
import org.teree.client.presenter.MapExplorer;
import org.teree.client.presenter.MapEditor;
import org.teree.client.presenter.MapViewer;
import org.teree.client.presenter.Presenter;
import org.teree.shared.MapGenerator;
import org.teree.shared.MapService;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

@ApplicationScoped
public class TereeController implements Presenter, ValueChangeHandler<String> {

	@Inject
	private IOCBeanManager manager;

	@Inject @Named(value="eventBus")
	private HandlerManager eventBus;
	
	@Inject
	private Caller<MapService> mapService;
	
	@Inject
	private Keyboard keyboard;

	private HasWidgets container;
	
	/**
	 * Bind handlers to eventBus.
	 */
	public void bind() {
		keyboard.init();
		History.addValueChangeHandler(this);
	}

	/**
	 * @see org.teree.client.presenter.Presenter
	 */
	@Override
	public void go(HasWidgets container) {
		this.container = container;
		bind();

		if ("".equals(History.getToken())) {
			History.newItem(Settings.CREATE_LINK);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;

			if (token.equals(Settings.HOME_LINK)) {
				/**IOCBeanDef<MapView> bean = manager.lookupBean(MapView.class);
				if (bean != null) {
					presenter = bean.getInstance();

				}*/
			} else if (token.startsWith(Settings.EXPLORE_LINK)) {
				IOCBeanDef<MapExplorer> bean = manager.lookupBean(MapExplorer.class);
				if (bean != null) {
					presenter = bean.getInstance();
				}
			} else if (token.startsWith(Settings.VIEW_LINK)) {
				IOCBeanDef<MapViewer> bean = manager.lookupBean(MapViewer.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadMap(token.substring(Settings.VIEW_LINK.length()));
				}
			} else if (token.startsWith(Settings.CREATE_LINK)) {
				IOCBeanDef<MapEditor> bean = manager.lookupBean(MapEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					presenter.go(container);
					eventBus.fireEvent(new MapReceived(null, MapGenerator.complex())); // TODO: create map from templates (even user's)
					return;
				}
			} else if (token.startsWith(Settings.EDIT_LINK)) {
				IOCBeanDef<MapEditor> bean = manager.lookupBean(MapEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadMap(token.substring(Settings.EDIT_LINK.length()));
				}
			}
			
			if (presenter != null) {
				presenter.go(container);
			}
			
		}
	}
	
	private void loadMap(final String oid) {
		mapService.call(new RemoteCallback<Node>() {
            @Override
            public void callback(Node response) {
                eventBus.fireEvent(new MapReceived(oid, response));
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				// TODO inform user about the error - show 404 page
				return false;
			}
		}).getMap(oid);
	}

}
