/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.server.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.SchemeService;
import org.teree.shared.UserService;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;

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
	public List<Scheme> selectFrom(String fromOid, SchemeFilter schemeFilter,
			int limit) {
		return _sm.selectFrom(fromOid, schemeFilter, limit, _us.getUserInfo());
	}

	@Override
	public List<Scheme> selectTo(String toOid, SchemeFilter schemeFilter, int limit) {
		return _sm.selectTo(toOid, schemeFilter, limit, _us.getUserInfo());
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
    
}
