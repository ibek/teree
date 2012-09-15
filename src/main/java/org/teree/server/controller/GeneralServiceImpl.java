package org.teree.server.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.GeneralService;
import org.teree.shared.data.Scheme;

@ApplicationScoped
@Service
public class GeneralServiceImpl implements GeneralService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;

	@Override
	public List<Scheme> getAllFrom(String from_oid, int limit) {
        _log.log(Level.INFO, "getAllFrom()");
		return _sm.allPublicFrom(from_oid, limit);
	}
	
	@Override
	public List<Scheme> getAllTo(String to_oid, int limit) {
        _log.log(Level.INFO, "getAllTo()");
		return _sm.allPublicTo(to_oid, limit);
	}

    @Override
    public Scheme getScheme(String oid) {
        _log.log(Level.INFO, "getScheme("+oid+")");
        return _sm.select(oid);
    }
    
}
