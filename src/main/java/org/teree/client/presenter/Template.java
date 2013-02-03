package org.teree.client.presenter;

public interface Template extends HeaderTemplate {
	
	public void info(String msg);
	
	public void error(String msg);
	
	public void sendDownloadRequest(String name, String dataUrl);
	
	public void sendDownloadRequest(String name, String type, String data);
	
}
