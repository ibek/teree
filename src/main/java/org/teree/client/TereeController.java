package org.teree.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.teree.client.event.NodeReceived;
import org.teree.client.presenter.MapEditor;
import org.teree.client.presenter.MapView;
import org.teree.client.presenter.Presenter;
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

	@Inject
	private HandlerManager eventBus;
	
	@Inject
	private Caller<MapService> mapService;

	private HasWidgets container;

	private static final String HOME_LINK = "home";
	private static final String VIEW_LINK = "view/oid=";
	private static final String CREATE_LINK = "create";
	private static final String EDIT_LINK = "edit/oid=";

	/**
	 * Bind handlers to eventBus.
	 */
	public void bind() {
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
			History.newItem(CREATE_LINK);
		} else {
			History.fireCurrentHistoryState();
		}
		System.out.println("ahoj");
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		System.out.println(token);
		if (token != null) {
			Presenter presenter = null;

			if (token.equals(HOME_LINK)) {
				/**IOCBeanDef<MapView> bean = manager.lookupBean(MapView.class);
				if (bean != null) {
					presenter = bean.getInstance();

				}*/
			} else if (token.startsWith(VIEW_LINK)) {
				IOCBeanDef<MapView> bean = manager.lookupBean(MapView.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadMap(token.substring(VIEW_LINK.length()));
				}
			} else if (token.startsWith(CREATE_LINK)) {
				System.out.println("creating");
				IOCBeanDef<MapEditor> bean = manager.lookupBean(MapEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					createMap();
					System.out.println("created");
				}
			} else if (token.startsWith(EDIT_LINK)) {
				IOCBeanDef<MapEditor> bean = manager.lookupBean(MapEditor.class);
				if (bean != null) {
					presenter = bean.getInstance();
					loadMap(token.substring(EDIT_LINK.length()));
				}
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
	
	/**
	 * TODO: create map from templates (even user's)
	 */
	private void createMap() {
		Node root = new Node();
		root.setContent("root");
		Node left = new Node();
		left.setContent("left");
		left.setLocation(NodeLocation.LEFT);
		Node right = new Node();
		right.setContent("right");
		right.setLocation(NodeLocation.LEFT);
		root.addChild(left);
		root.addChild(right);
		eventBus.fireEvent(new NodeReceived(root));
	}
	
	private void loadMap(String oid) {
		mapService.call(new RemoteCallback<Node>() {
            @Override
            public void callback(Node response) {
                eventBus.fireEvent(new NodeReceived(response));
            }
        }, new ErrorCallback() {
			@Override
			public boolean error(Message message, Throwable throwable) {
				// TODO Auto-generated method stub
				return false;
			}
		}).getMap(oid);
	}

}
