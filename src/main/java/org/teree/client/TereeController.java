package org.teree.client;

import javax.inject.Inject;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.teree.client.presenter.Presenter;
 
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class TereeController implements Presenter, ValueChangeHandler<String>{
    
    @Inject
    private IOCBeanManager manager;

    @Inject
    private HandlerManager eventBus;

    private HasWidgets container;

    /**
     * Bind handlers to eventBus.
     */
    public void bind() {
        
    }
    
    /**
     * @see org.teree.client.presenter.Presenter
     */
    @Override
    public void go(HasWidgets container) {
        this.container = container;
        bind();

        if ("".equals(History.getToken())) {
          History.newItem("home");
        } else {
          History.fireCurrentHistoryState();
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        if (token != null) {
          Presenter presenter = null;

          if (token.equals("home")) {
            /**IOCBeanDef<ContactsPresenter> bean = manager.lookupBean(ContactsPresenter.class);
            if (bean != null) {
              presenter = bean.getInstance();
            }*/
          }

          if (presenter != null) {
            presenter.go(container);
          }
        }
    }
    
}
