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
	
	public String insertNodeCategory(NodeCategory category);
	
	public void updateNodeCategory(NodeCategory category);
	
	public void removeNodeCategory(String oid);
    
}
