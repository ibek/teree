package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.scheme.Scheme;

/**
* Errai RPC interface that specifies which methods the client can invoke on the
* server-side service.
*
* @see org.teree.server.controller.SchemeServiceImpl
*/
@Remote
public interface SchemeService {

	public List<Scheme> getAllFrom(String fromOid, int limit);
	
	public List<Scheme> getAllTo(String toOid, int limit);

	public List<Scheme> getAllFromUser(String fromOid, int limit, String userid);
	
	public List<Scheme> getAllToUser(String toOid, int limit, String userid);

	public List<Scheme> searchFrom(String fromOid, String text, int limit);
	
	public List<Scheme> searchTo(String toOid, String text, int limit);
    
    public Scheme getScheme(String oid);
    
    public String exportJSON(String oid);
    
    public Scheme importJSON(String json);
    
}