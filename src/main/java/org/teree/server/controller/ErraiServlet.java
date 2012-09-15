package org.teree.server.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.errai.bus.server.servlet.DefaultBlockingServlet;

public class ErraiServlet extends DefaultBlockingServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	HttpProvider provider;
	
	@Override
	protected void doGet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		setProvider(httpServletRequest, httpServletResponse);
		try {
			super.doGet(httpServletRequest, httpServletResponse);
		} catch(RedirectException re) {
			System.out.println("redirect exception");
			//((HttpServletResponse)resp).sendRedirect(re.getUrl());
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		setProvider(httpServletRequest, httpServletResponse);
		try {
			super.doPost(httpServletRequest, httpServletResponse);
		} catch(RedirectException re) {
			System.out.println("redirect exception");
			//((HttpServletResponse)resp).sendRedirect(re.getUrl());
		}
	}
	
	protected void setProvider(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		provider.setRequest(req);
		provider.setResponse(resp);
	}

}
