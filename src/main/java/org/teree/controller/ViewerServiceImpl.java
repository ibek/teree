package org.teree.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.shared.ViewerService;

@ApplicationScoped
@Service
public class ViewerServiceImpl implements ViewerService {

    @Inject
    private Logger log;

    @Override
    public void expand() {
        log.log(Level.INFO, "expand");
    }
    
}
