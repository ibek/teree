package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.SchemeFilter;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.server.controller.SchemeServiceImpl
*/
@Remote
public interface SchemeService {

	public List<Scheme> selectFrom(String fromOid, SchemeFilter filter, int limit);
	
	public List<Scheme> selectTo(String toOid, SchemeFilter filter, int limit);
    
    public Scheme getScheme(String oid);
    
    public String exportJSON(String oid);
    
    public Scheme importJSON(String json);
    
}