package org.teree.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;
import org.teree.dao.NodeManager;
import org.teree.shared.ViewerService;
import org.teree.shared.data.MapChange;
import org.teree.shared.data.Node;

@ApplicationScoped
@Service
public class ViewerServiceImpl implements ViewerService {

    @Inject
    private Logger _log;
    
    @Inject
    private NodeManager _nm;
    
    private RequestDispatcher _dispatcher = ErraiBus.getDispatcher();

    @Override
    public Node getMap(String oid) {
        _log.log(Level.INFO, "getMap("+oid+")");
        return _nm.select(oid);
    }

    @Override
    public String insertMap(Node root) {
        return _nm.insert(root);
    }

	@Override
	public void update(String oid, Node root) {
		_nm.update(oid, root);
	}

    @Override
    public void mapChanged(MapChange change) {
        MessageBuilder.createMessage()
        .toSubject(change.getOid())
        .with("coop-change", change)
        .noErrorHandling().sendGlobalWith(_dispatcher);
    }
    
}
