package org.teree.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;

@ApplicationScoped
@Service
public class ViewerServiceImpl implements ViewerService {

    @Inject
    private Logger log;

    @Override
    public Node getMap(String id) {
        log.log(Level.INFO, "getMap("+id+")");
        Node root = new Node();
        root.addChild(new Node());
        root.addChild(new Node());
        return root;
    }
    
}
