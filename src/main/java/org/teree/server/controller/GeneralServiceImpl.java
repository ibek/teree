package org.teree.server.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.bus.server.annotations.security.RequireAuthentication;
import org.teree.server.auth.OAuthIdentifierFetcher;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.GeneralService;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.SchemeChange;

@ApplicationScoped
@Service
//@RequireAuthentication
public class GeneralServiceImpl implements GeneralService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;
    
    @Inject
    private OAuthIdentifierFetcher _userinfo;
    
    private RequestDispatcher _dispatcher = ErraiBus.getDispatcher();

	@Override
	public List<Scheme> getAll() {
        _log.log(Level.INFO, "getAll()");
		return _sm.all();
	}

    @Override
    public Scheme getScheme(String oid) {
        _log.log(Level.INFO, "getScheme("+oid+")");
        return _sm.select(oid);
    }

    @Override
    public String insertScheme(Scheme s) {
        _log.log(Level.INFO, "insertScheme("+s.getRoot().getContent()+")");
        return _sm.insert(s);
    }

	@Override
	public void updateScheme(Scheme s) {
        _log.log(Level.INFO, "updateScheme("+s.getOid()+", "+s.getRoot().getContent()+")");
		_sm.update(s);
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
