package org.teree.server.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.SchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.scheme.Scheme;

@ApplicationScoped
@Service
public class SchemeServiceImpl implements SchemeService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;

    @Inject
    private UserService _us;
    
	@Override
	public List<Scheme> getAllFrom(String from_oid, int limit) {
		return _sm.allFrom(from_oid, limit, _us.getUserInfo());
	}
	
	@Override
	public List<Scheme> getAllTo(String to_oid, int limit) {
		return _sm.allTo(to_oid, limit, _us.getUserInfo());
	}

	@Override
	public List<Scheme> getAllFromUser(String from_oid, int limit, String userid) {
		return _sm.allFromUser(from_oid, limit, userid, _us.getUserInfo());
	}

	@Override
	public List<Scheme> getAllToUser(String to_oid, int limit, String userid) {
		return _sm.allToUser(to_oid, limit, userid, _us.getUserInfo());
	}

    @Override
    public Scheme getScheme(String oid) {
        return _sm.select(oid, _us.getUserInfo());
    }

	@Override
	public String exportJSON(String oid) {
		return _sm.exportJSON(oid, _us.getUserInfo());
	}

	@Override
	public Scheme importJSON(String json) {
		return _sm.importJSON(json);
	}

	@Override
	public List<Scheme> searchFrom(String from_oid, String text, int limit) {
		return _sm.searchFrom(from_oid, text, limit, _us.getUserInfo());
	}

	@Override
	public List<Scheme> searchTo(String to_oid, String text, int limit) {
		return _sm.searchTo(to_oid, text, limit, _us.getUserInfo());
	}
    
}
