package org.teree.client.viewer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.teree.client.viewer.ui.ViewerUI;
import org.teree.shared.ViewerService;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Viewer {

    /**
     * This is the client-side proxy to the Errai service implemented by
     * MemberServiceImpl. The proxy is generated at build time, and injected
     * into this field when the page loads. You can create additional Errai
     * services by following this same pattern; just be sure that the
     * client-side class you inject the Caller into is an injectable class
     * (client-side injectable classes are annotated with {@code @EntryPoint},
     * {@code @ApplicationScoped}, or {@code @Singleton}).
     */
    @Inject
    private Caller<ViewerService> viewerService;

    private ViewerUI viewerUi;

    /**
     * Builds the UI and populates the member list by making an RPC call to the
     * server.
     * <p>
     * Note that because this method performs an RPC call to the server, it is
     * annotated with AfterInitialization rather than PostConstruct: the
     * contract of PostConstruct only guarantees that all of <em>this</em>
     * bean's dependencies have been injected, but it does not guarantee that
     * the entire runtime environment has completed its bootstrapping routine.
     * Methods annotated with the Errai-specific AfterInitialization are only
     * called once everything is up and running, including the communication
     * channel to the server.
     */
    @AfterInitialization
    public void createUI() {
        viewerUi = new ViewerUI(viewerService);
        DOM.getElementById("loader").removeFromParent();
        RootPanel.get().add(viewerUi);
    }

    @PostConstruct
    public void onModuleLoad() {
        
    }

}
