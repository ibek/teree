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
import org.teree.shared.data.NodeChange;
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
        _log.log(Level.INFO, "insertMap("+root.getContent()+")");
        return _nm.insert(root);
    }

	@Override
	public void updateMap(String oid, Node root) {
        _log.log(Level.INFO, "updateMap("+oid+", "+root.getContent()+")");
		_nm.update(oid, root);
	}

    @Override
    public void mapChanged(NodeChange change) {
        _log.log(Level.INFO, "mapChanged("+change.getOid()+")");
        MessageBuilder.createMessage()
        .toSubject(change.getOid())
        .with("coop-change", change)
        .noErrorHandling().sendGlobalWith(_dispatcher);
    }
    
}
