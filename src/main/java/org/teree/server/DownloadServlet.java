package org.teree.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class DownloadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8731564925278774979L;
	
	private static final long MAX_FILE_SIZE = 5*(1024*1024);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		DiskFileItemFactory fif = new DiskFileItemFactory();
		fif.setSizeThreshold((int)MAX_FILE_SIZE);
		ServletFileUpload upload = new ServletFileUpload(fif);
		upload.setSizeMax(MAX_FILE_SIZE);

        try{
            Iterator iter = upload.parseRequest(req).iterator();

            String fileName = "teree.png";
            String fileType = "image/png";
            while (iter.hasNext()) {
                FileItem item = (FileItem)iter.next();
                if (item.getFieldName().compareTo("fileName") == 0) {
                	fileName = item.getString();
                } else if (item.getFieldName().compareTo("fileType") == 0) {
                	fileType = item.getString();
                } else if (item.getFieldName().compareTo("file") == 0) {
	                
	                resp.setContentType("application/octet-stream");
	                
	                if (fileType.contains("png")) {
	                	fileName += ".png";
	                } else if (fileType.contains("freemind")) {
	                	fileName += ".mm";
	                } else if (fileType.contains("json")) {
	                	fileName += ".json";
	                }
	                
	        		resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
	        		
	        		byte[] data = item.get();
	        		if (Base64.isArrayByteBase64(data)) {
	        			data = Base64.decodeBase64(data);
                	}
	        		int contentLength = data.length;
	                resp.setContentLength(contentLength);
	                
	                ServletOutputStream out = resp.getOutputStream();
	                
	                IOUtils.copy(new ByteArrayInputStream(data), out);
	                out.flush();
	                out.close();
	                break;
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
	}

}
