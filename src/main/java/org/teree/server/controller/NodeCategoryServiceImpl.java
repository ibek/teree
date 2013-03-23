package org.teree.server.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.auth.RequireAuthentication;
import org.teree.server.dao.NodeCategoryManager;
import org.teree.shared.NodeCategoryService;
import org.teree.shared.UserService;
import org.teree.shared.data.common.NodeCategory;

@ApplicationScoped
@Service
@RequireAuthentication
public class NodeCategoryServiceImpl implements NodeCategoryService {

    @Inject
    private Logger _log;
    
    @Inject
    private NodeCategoryManager _ncm;

    @Inject
    private UserService _us;

	@Override
	public List<NodeCategory> selectAll() {
		return _ncm.selectAll(_us.getUserInfo());
	}

	@Override
	public String insertNodeCategory(NodeCategory category) {
		category.setOwner(_us.getUserInfo());
		return _ncm.insert(category);
	}

	@Override
	public void updateNodeCategory(NodeCategory category) {
		_ncm.update(category, _us.getUserInfo());
	}

	@Override
	public void removeNodeCategory(String oid) {
		_ncm.remove(oid, _us.getUserInfo());
	}

    
}
