package org.teree.server.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.auth.RequireAuthentication;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.SecuredSchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.SchemeChange;

@ApplicationScoped
@Service
@RequireAuthentication
public class SecuredSchemeServiceImpl implements SecuredSchemeService {

    @Inject
    private Logger _log;
    
    @Inject
    private SchemeManager _sm;
    
    @Inject
    private UserService _us;
    
    private RequestDispatcher _dispatcher = ErraiBus.getDispatcher();

    @Override
	public String insertScheme(Scheme s) {
        return _sm.insert(s, _us.getUserInfo());
    }

	@Override
	public void updateScheme(Scheme s) {
		_sm.update(s, _us.getUserInfo());
	}
	
	@Override
	public void updateSchemePermissions(Scheme s) {
		_sm.updatePermissions(s, _us.getUserInfo());
	}
	
	@Override
	public boolean removeScheme(String oid) {
		return _sm.remove(oid, _us.getUserInfo());
	}

    @Override
    public void schemeChanged(SchemeChange change) {
        _log.log(Level.INFO, "schemeChanged("+change.getOid()+")");
        MessageBuilder.createMessage()
        .toSubject(change.getOid())
        .with("coop-change", change)
        .noErrorHandling().sendGlobalWith(_dispatcher);
    }

	@Override
	public Scheme getScheme(String oid) {
		return _sm.selectToEdit(oid, _us.getUserInfo());
	}
    
}
