package org.teree.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.teree.server.dao.SchemeManager;
import org.teree.shared.data.common.Scheme;

public class ShareSchemeServlet extends HttpServlet {

	private static final long serialVersionUID = 2962737780588132803L;

	@Inject
	SchemeManager _sm;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String oid = request.getParameter("oid");
			Scheme s = _sm.select(oid, null);
			if (s != null) {
				String base64data = s.getSchemePicture();
				base64data = base64data.substring(base64data.lastIndexOf(',')+1);
				byte[] data = Base64.decodeBase64(base64data.getBytes());
				int contentLength = data.length;
                response.setContentLength(contentLength);
				response.setContentType("image/png");
                
                ServletOutputStream out = response.getOutputStream();
                
                IOUtils.copy(new ByteArrayInputStream(data), out);
                out.flush();
                out.close();
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}
