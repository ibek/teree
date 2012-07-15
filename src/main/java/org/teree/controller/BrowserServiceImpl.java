package org.teree.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.shared.BrowserService;

@ApplicationScoped
@Service
public class BrowserServiceImpl implements BrowserService {

    @Inject
    private Logger log;

    @Override
    public void show() {
        log.log(Level.INFO, "show");
    }
    
}
