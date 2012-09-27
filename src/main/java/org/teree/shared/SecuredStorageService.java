package org.teree.shared;

import java.util.List;

import org.jboss.errai.bus.server.annotations.Remote;
import org.teree.shared.data.storage.ImageInfo;

@Remote
public interface SecuredStorageService {

	public List<ImageInfo> getImages(String prefix);
	
	public void uploadImage(String path, byte[] data);
    
}