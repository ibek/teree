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
	
	protected static final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";
	
	public OAuthFetcher() {
		
	}

	public Response fetch(OAuthRequest request, Token accessToken) {

		OAuthService service = new ServiceBuilder().provider(GoogleApi.class)
				.apiKey("anonymous").apiSecret("anonymous")
				.scope(SCOPES).build();

		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		Response response = request.send();

		return response;
	}

}
