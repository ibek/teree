package org.teree.client.browser;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.teree.shared.BrowserService;

import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Browser {

    @Inject
    private Caller<BrowserService> browserService;
    
  /**
   * Builds the UI and populates the member list by making an RPC call to the server.
   * <p>
   * Note that because this method performs an RPC call to the server, it is annotated
   * with AfterInitialization rather than PostConstruct: the contract of PostConstruct
   * only guarantees that all of <em>this</em> bean's dependencies have been injected,
   * but it does not guarantee that the entire runtime environment has completed its
   * bootstrapping routine. Methods annotated with the Errai-specific AfterInitialization
   * are only called once everything is up and running, including the communication
   * channel to the server.
   */
  @AfterInitialization
  public void createUI() {
      RootPanel.get("loader").removeFromParent();
      System.out.println("cau");
  }
  
  @PostConstruct
  public void onModuleLoad(){
      
  }

}
