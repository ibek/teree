package org.teree.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.tree.SchemeChange;

@Remote
public interface SecuredSchemeService {
    
    public Scheme getScheme(String oid);
    
    public String insertScheme(Scheme s);
    
    public void updateScheme(Scheme s);
    
    public void updateSchemePermissions(Scheme s);
    
    public boolean removeScheme(String oid);
    
    /**
     * Inform cooperated users about change.
     * @param change info
     */
    public void schemeChanged(SchemeChange change);
    
}