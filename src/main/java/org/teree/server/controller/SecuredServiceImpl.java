package org.teree.server.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.annotations.security.RequireAuthentication;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.SecuredService;
import org.teree.shared.UserService;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.SchemeChange;

@ApplicationScoped
@Service
@RequireAuthentication
public class SecuredServiceImpl implements SecuredService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;
    
    @Inject
    private UserService _us;
    
    private RequestDispatcher _dispatcher = ErraiBus.getDispatcher();

	@Override
	public List<Scheme> getPrivateSchemesFrom(String from_oid, int limit) {
        _log.log(Level.INFO, "getPrivateSchemesFrom()");
		return _sm.allPrivateFrom(_us.getUserInfo(), from_oid, limit);
	}

	@Override
	public List<Scheme> getPrivateSchemesTo(String to_oid, int limit) {
		_log.log(Level.INFO, "getPrivateSchemesTo()");
		return _sm.allPrivateTo(_us.getUserInfo(), to_oid, limit);
	}

    @Override
	public Scheme getPrivateScheme(String oid) {
        _log.log(Level.INFO, "getPrivateScheme("+oid+")");
        return _sm.selectPrivate(oid, _us.getUserInfo());
    }

    @Override
	public String insertScheme(Scheme s) {
        _log.log(Level.INFO, "insertPrivateScheme("+s.getRoot().getContent()+")");
        return _sm.insertPrivate(s, _us.getUserInfo());
    }

	@Override
	public void updateScheme(Scheme s) {
        _log.log(Level.INFO, "updatePrivateScheme("+s.getOid()+", "+s.getRoot().getContent()+")");
		_sm.updatePrivate(s, _us.getUserInfo());
	}
	
	@Override
	public void publishScheme(Scheme s) {
		// TODO: publish scheme
	}

    @Override
    public void schemeChanged(SchemeChange change) {
        _log.log(Level.INFO, "schemeChanged("+change.getOid()+")");
        MessageBuilder.createMessage()
        .toSubject(change.getOid())
        .with("coop-change", change)
        .noErrorHandling().sendGlobalWith(_dispatcher);
    }
    
}
