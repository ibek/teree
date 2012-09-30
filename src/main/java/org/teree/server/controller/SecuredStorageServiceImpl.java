package org.teree.server.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.teree.server.auth.RequireAuthentication;
import org.teree.server.dao.StorageManager;
import org.teree.shared.SecuredStorageService;
import org.teree.shared.UserService;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.storage.ImageInfo;

@ApplicationScoped
@Service
@RequireAuthentication
public class SecuredStorageServiceImpl implements SecuredStorageService {

    @Inject
    private Logger _log;
    
    @Inject
    private UserService _us;
    
    @Inject
    private StorageManager _sm;

	@Override
	public List<ImageInfo> getImages(String prefix) {
		return _sm.getImages(prefix, _us.getUserInfo());
	}

	@Override
	public List<ImageInfo> getPublicImages(String prefix) {
		return _sm.getPublicImages(prefix);
	}

	@Override
	public void deleteImage(String path) {
		_sm.deleteImage(path, _us.getUserInfo());
	}

	/**
	 * Upload is performed by UploadServet.
	 * @param path
	 * @param input
	 * @throws Exception 
	 */
	public void uploadImage(String path, byte[] data) throws Exception {
		UserInfo ui = _us.getUserInfo();
		_sm.uploadImage(path, data, ui);
		_us.update(ui);
	}
    
}
