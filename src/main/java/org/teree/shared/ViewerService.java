package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.controller.ViewerServiceImpl
*/
@Remote
public interface ViewerService {
  
    public void expand();
    
}