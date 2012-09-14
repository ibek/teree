package org.teree.server.auth;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.teree.server.controller.RedirectException;
import org.teree.shared.data.UserInfo;

import com.google.gson.JsonElement;
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
			System.out.println(body);
			JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
			googleid = obj.get("id").getAsString();
		}

		return googleid;
	}

}
