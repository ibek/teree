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
import javax.inject.Inject;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.teree.shared.data.UserInfo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
public class OAuthIdentifierFetcher {

	private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

	@Inject
	OAuthFetcher fetcher;

	public OAuthIdentifierFetcher() {

	}

	public String fetch(Token accessToken) {
		String googleid = null;

		OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_URL);
		Response response = fetcher.fetch(request, accessToken);

		if (response != null) {
			String body = response.getBody();
			JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
			googleid = obj.get("id").getAsString();
		}

		return googleid;
	}

	public UserInfo fetchUserInfo(Token accessToken) {
		UserInfo ui = new UserInfo();

		OAuthRequest request = new OAuthRequest(Verb.GET, USERINFO_URL);
		Response response = fetcher.fetch(request, accessToken);

		if (response != null) {
			String body = response.getBody();
			JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
			ui.setName(obj.get("name").getAsString());
			ui.setEmail(obj.get("email").getAsString());
		}

		return ui;
	}

}
