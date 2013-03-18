package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.server.controller.SchemeServiceImpl
*/
@Remote
public interface NodeCategoryService {

	public List<NodeCategory> selectAll();
	
	public boolean insertNodeCategory(NodeCategory category);
	
	public void updateNodeCategory(NodeCategory category);
	
	public void removeNodeCategory(String oid);
    
}