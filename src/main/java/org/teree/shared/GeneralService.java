package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.SchemeChange;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.server.controller.GeneralServiceImpl
*/
@Remote
public interface GeneralService {
	
	public List<Scheme> getAll();
    
    public Scheme getScheme(String oid);
    
}