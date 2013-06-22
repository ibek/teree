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

import org.teree.client.presenter.Presenter;

public class CurrentPresenter {

	private static CurrentPresenter instance;
	
	private Presenter presenter;
	
	private CurrentPresenter() {
		
	}
	
	public static CurrentPresenter getInstance() {
		if (instance == null) {
			instance = new CurrentPresenter();
		}
		return instance;
	}

	public Presenter getPresenter() {
		return presenter;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
}
