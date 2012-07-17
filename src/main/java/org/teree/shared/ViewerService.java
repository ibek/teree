package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
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
    
}