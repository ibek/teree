package org.teree.server.auth;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.jboss.errai.bus.client.protocols.SecurityParts;
import org.jboss.errai.bus.server.security.auth.AuthenticationAdapter;
import org.jboss.errai.bus.server.service.ErraiService;
import org.jboss.errai.bus.server.util.SessionContext;
import org.jboss.errai.common.client.protocols.Resources;
import org.scribe.model.Token;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;

import javax.inject.Inject;

public class TAuthAdapter implements AuthenticationAdapter {

    private MessageBus bus;
    
    @Inject
    UserInfoManager uim;
    
    @Inject
    OAuthIdentifierFetcher uif;

    @Inject
    public TAuthAdapter(MessageBus bus) {
        this.bus = bus;
    }

    /**
     * Send a challenge to the authentication system.
     *
     * @param message
     */
    public void challenge(final Message message) {
        
        final AuthType at = message.get(AuthType.class, AuthType.PART);
        
        switch(at) {
	        case Database: {
	            
	            final String username = message.get(String.class, SecurityParts.Name);
	            final String password = message.get(String.class, SecurityParts.Password);
	            
	            UserInfo ui = uim.select(username, password);
	            
	            if (ui == null) { // authentication failed
	            	MessageBuilder.createConversation(message)
	                .subjectProvided()
	                .command(SecurityCommands.FailedAuth)
	                .with(SecurityParts.Name, username)
	                .noErrorHandling().sendNowWith(bus);
	            } else {
	            	
	            	addAuthenticationCredentials(message, username, password);
	            	
	    	        MessageBuilder.createConversation(message)
	    	                .subjectProvided()
	    	                .command(SecurityCommands.SuccessfulAuth)
	    	                .with(UserInfo.PART, ui).getMessage()
	    	                .sendNowWith(bus);
	            }
	            
	            break;
	        }
	        case OAuth: {
	        	
	        	//Token accessToken = message.get(Token.class, OAuthServlet.ACCESS_TOKEN_PART);
	        	Token accessToken = getAuthenticationToken(message);
	        	if (accessToken == null) {
	        		MessageBuilder.createConversation(message)
		                .subjectProvided()
		                .command(SecurityCommands.FailedAuth)
		                .noErrorHandling().sendNowWith(bus);
	        		return;
	        	}
	        	String googleid = uif.fetch(accessToken);
	        	UserInfo ui = uim.selectByGoogleId(googleid);
	        	
	        	MessageBuilder.createConversation(message)
                .subjectProvided()
                .command(SecurityCommands.SuccessfulAuth)
                .with(UserInfo.PART, ui).getMessage()
                .sendNowWith(bus);
	        	
	        }
        }
    }

    // TODO: add the authentication into session
    private void addAuthenticationCredentials(Message message, String username, String password) {
        QueueSession session = message.getResource(QueueSession.class, Resources.Session.name());
        session.setAttribute("username", username);
        session.setAttribute("password", password);
    }

    // TODO: add the authentication into session
    private void addAuthenticationToken(Message message, Token accessToken) {
        QueueSession session = message.getResource(QueueSession.class, "Session");
        session.setAttribute(ErraiService.SESSION_AUTH_DATA, accessToken);
    }
    
    private Token getAuthenticationToken(Message message) {
    	SessionContext ctx = SessionContext.get(message);
        return ctx.getSession().getAttribute(Token.class, ErraiService.SESSION_AUTH_DATA);
    }

    public boolean isAuthenticated(Message message) {
        QueueSession session = message.getResource(QueueSession.class, "Session");
        return session != null && session.hasAttribute(ErraiService.SESSION_AUTH_DATA);
    }

    public boolean endSession(Message message) {
        boolean sessionEnded = isAuthenticated(message);
        if (sessionEnded) {
            message.getResource(QueueSession.class, "Session").removeAttribute(ErraiService.SESSION_AUTH_DATA);
            return true;
        } else {
            return false;
        }
    }

}

