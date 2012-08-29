package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.SchemeChange;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.controller.GeneralServiceImpl
*/
@Remote
public interface GeneralService {
	
	public List<Scheme> getAll();
    
    public Scheme getScheme(String oid);
    
    public String insertScheme(Scheme s);
    
    public void updateScheme(Scheme s);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void schemeChanged(SchemeChange change);
    
}