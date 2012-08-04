package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.MapChange;
import org.teree.shared.data.Node;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.controller.ViewerServiceImpl
*/
@Remote
public interface ViewerService {
    
    public Node getMap(String id);
    
    public String insertMap(Node root);
    
    public void update(String oid, Node root);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void mapChanged(MapChange change);
    
}