/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
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
