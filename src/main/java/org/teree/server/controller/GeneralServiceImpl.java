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
	public List<Scheme> getAll() {
        _log.log(Level.INFO, "getAll()");
		return _sm.allPublic();
	}

    @Override
    public Scheme getScheme(String oid) {
        _log.log(Level.INFO, "getScheme("+oid+")");
        return _sm.select(oid);
    }
    
}
