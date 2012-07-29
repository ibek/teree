package org.teree.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.dao.NodeManager;
import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;

@ApplicationScoped
@Service
public class ViewerServiceImpl implements ViewerService {

    @Inject
    private Logger log;
    
    @Inject
    private NodeManager nm;

    @Override
    public Node getMap(String oid) {
        log.log(Level.INFO, "getMap("+oid+")");
        return nm.select(oid);
    }

    @Override
    public String insertMap(Node root) {
        return nm.insert(root);
    }
    
}
