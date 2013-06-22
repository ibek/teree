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
package org.teree.server.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.teree.shared.data.AuthType;

import com.google.common.base.Strings;
import com.google.inject.Singleton;

@Singleton
public class OAuthServlet extends HttpServlet {
	
	public static final String ACCESS_TOKEN_PART = "AccessToken";

	private static final long serialVersionUID = -7684638752817245595L;

	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";

	private static final String TAUTH_LOGIN = "/teree.html#tauth";
	private static final String LOGIN = "/teree.html#login";
	
	private static final Map<String, String> tokens = new HashMap<String, String>();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String callback = req.getRequestURL().toString();

		OAuthService service = new ServiceBuilder()
				.provider(GoogleApi.class)
				.apiKey("www.teree.org")
				.apiSecret("SgYJtCunHJHLy3z79apifsv8")
				.scope(OAuthFetcher.SCOPES)
				.callback(callback).build();

		String oAuthToken = req.getParameter("oauth_token");
		String oAuthVerifier = req.getParameter("oauth_verifier");
		String oAuthSecret = tokens.get(oAuthToken);

		if (Strings.isNullOrEmpty(oAuthToken) || Strings.isNullOrEmpty(oAuthSecret)) {

			Token requestToken = service.getRequestToken();
			tokens.put(requestToken.getToken(), requestToken.getSecret());
			resp.sendRedirect(AUTHORIZE_URL + requestToken.getToken());
			return;
		}

		Token accessToken = null;
		try {
			Verifier verifier = new Verifier(oAuthVerifier);
			Token token = new Token(oAuthToken, oAuthSecret);
			accessToken = service.getAccessToken(token, verifier);
		} catch(OAuthException ex) {
			resp.sendRedirect(LOGIN);
			return;
		}
		
		tokens.remove(oAuthToken);

		HttpSession session = req.getSession();
		session.setAttribute("auth", AuthType.OAuth.name());
		session.setAttribute("token", accessToken);
		resp.sendRedirect(TAUTH_LOGIN);
		
	}

}
