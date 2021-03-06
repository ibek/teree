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


import javax.ejb.Stateless;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

@Stateless
public class OAuthFetcher {
	
	protected static final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
	
	public OAuthFetcher() {
		
	}

	public Response fetch(OAuthRequest request, Token accessToken) {

		OAuthService service = new ServiceBuilder()
				.provider(GoogleApi.class)
				.apiKey("www.teree.org")
				.apiSecret("SgYJtCunHJHLy3z79apifsv8")
				.scope(SCOPES).build();

		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		Response response = request.send();

		return response;
	}

}
