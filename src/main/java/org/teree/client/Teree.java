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
package org.teree.client;

import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class Teree {

    @Inject
    private TereeController tc;

    @AfterInitialization
    public void startApp() {
        tc.go(RootPanel.get());
    }

    /**@Produces
    @Named(value="msgBus")
    private MessageBus produceMessageBus() {
        return msgBus;
    }*/

}
