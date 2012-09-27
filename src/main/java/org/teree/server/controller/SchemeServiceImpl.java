package org.teree.server.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.SchemeService;
import org.teree.shared.data.scheme.Scheme;

@ApplicationScoped
@Service
public class SchemeServiceImpl implements SchemeService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;

	@Override
	public List<Scheme> getAllFrom(String from_oid, int limit) {
		return _sm.allPublicFrom(from_oid, limit);
	}
	
	@Override
	public List<Scheme> getAllTo(String to_oid, int limit) {
		return _sm.allPublicTo(to_oid, limit);
	}

    @Override
    public Scheme getScheme(String oid) {
        return _sm.select(oid);
    }
    
}
