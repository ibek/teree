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

	public List<Scheme> getAllFrom(String from_oid, int limit);
	
	public List<Scheme> getAllTo(String to_oid, int limit);

	public List<Scheme> getAllFromUser(String from_oid, int limit, String userid);
	
	public List<Scheme> getAllToUser(String to_oid, int limit, String userid);
    
    public Scheme getScheme(String oid);
    
    public String exportJSON(String oid);
    
    public Scheme importJSON(String json);
    
}