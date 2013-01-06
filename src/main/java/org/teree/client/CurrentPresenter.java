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
