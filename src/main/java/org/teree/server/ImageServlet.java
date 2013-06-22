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
package org.teree.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 8986265394903799751L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getHeader("host").equals("127.0.0.1:8080") ||
				request.getHeader("host").equals("www.teree.org")) {
			
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("image/png");
			try {
				URL url = new URL(request.getParameter("url"));
				URLConnection conn = url.openConnection();
			    InputStream input = conn.getInputStream();
				byte[] buffer = new byte[16384];
			    int bytesRead;
			    OutputStream output = response.getOutputStream();
			    while ((bytesRead = input.read(buffer)) != -1)
			    {
			        output.write(buffer, 0, bytesRead);
			    }
			    output.flush();
			} catch (Exception ex) {
				
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}
