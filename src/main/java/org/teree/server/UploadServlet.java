package org.teree.server;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.server.HttpSessionProvider;
import org.jboss.errai.bus.server.api.RpcContext;
import org.teree.server.controller.SecuredStorageServiceImpl;

public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8731564925278774979L;
	
	private static final long MAX_FILE_SIZE = 1024*1024;
	
	@Inject
	SecuredStorageServiceImpl storage;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// workaround for @RequireAuthentication in SecuredStorageServiceImpl that requires to have the session set in RpcContext
		RpcContext.set(MessageBuilder.createMessage().toSubject("").getMessage().setResource("Session", new HttpSessionProvider().createOrGetSession(req.getSession(), "???")));
		
		DiskFileItemFactory fif = new DiskFileItemFactory();
		fif.setSizeThreshold((int)MAX_FILE_SIZE);
		ServletFileUpload upload = new ServletFileUpload(fif);
		upload.setSizeMax(MAX_FILE_SIZE);

        try{
            Iterator iter = upload.parseRequest(req).iterator();

            while (iter.hasNext()) {
                FileItem item = (FileItem)iter.next();

                String name = item.getName();
                byte[] data = item.get();
                
                storage.uploadImage(name, data);
                
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
	}

}
