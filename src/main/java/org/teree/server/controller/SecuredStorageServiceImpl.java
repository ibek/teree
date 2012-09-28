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

	/**
	 * Upload is performed by UploadServet.
	 * @param path
	 * @param input
	 */
	public void uploadImage(String path, byte[] data) {
		_sm.uploadImage(path, data, _us.getUserInfo());
	}
    
}
