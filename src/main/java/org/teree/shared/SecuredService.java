package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.Scheme;
import org.teree.shared.data.SchemeChange;

@Remote
public interface SecuredService {

	public List<Scheme> getPrivateSchemesFrom(String from_oid, int limit);
	
	public List<Scheme> getPrivateSchemesTo(String to_oid, int limit);
    
    public Scheme getPrivateScheme(String oid);
    
    public String insertScheme(Scheme s);
    
    public void updateScheme(Scheme s);
    
    public void publishScheme(String oid);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void schemeChanged(SchemeChange change);
    
    public enum Command {
	
		getAllPrivateSchemes,
	    getPrivateScheme,
	    insertPrivateScheme,
	    updatePrivateScheme;
	
    }
    
}