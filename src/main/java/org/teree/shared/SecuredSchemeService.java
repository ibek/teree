package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.scheme.Scheme;
import org.teree.shared.data.scheme.SchemeChange;

@Remote
public interface SecuredSchemeService {
    
    public String insertScheme(Scheme s);
    
    public void updateScheme(Scheme s);
    
    public boolean removeScheme(String oid);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void schemeChanged(SchemeChange change);
    
}