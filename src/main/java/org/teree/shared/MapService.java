package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.Map;
import org.teree.shared.data.NodeChange;
import org.teree.shared.data.Node;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.controller.MapServiceImpl
*/
@Remote
public interface MapService {
	
	public List<Map> getAll();
    
    public Node getMap(String oid);
    
    public String insertMap(Node root);
    
    public void updateMap(String oid, Node root);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void mapChanged(NodeChange change);
    
}