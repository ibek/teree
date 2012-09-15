package org.teree.server.auth;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.jboss.errai.bus.client.protocols.SecurityParts;
import org.jboss.errai.bus.server.security.auth.AuthenticationAdapter;
import org.jboss.errai.common.client.protocols.Resources;
import org.scribe.model.Token;
import org.teree.server.dao.UserInfoManager;
import org.teree.shared.data.AuthType;
import org.teree.shared.data.UserInfo;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

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
    @Override
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
	        	
	        	Token accessToken = getAuthenticationToken(message);
	        	if (accessToken == null) {
	        		MessageBuilder.createConversation(message)
		                .subjectProvided()
		                .command(SecurityCommands.FailedAuth)
		                .noErrorHandling().sendNowWith(bus);
	        		return;
	        	}
	        	String googleid = uif.fetch(accessToken);
	        	addAuthenticationCredentials(message, googleid);
	        	UserInfo ui = uim.selectByGoogleId(googleid);
	        	
	        	MessageBuilder.createConversation(message)
                .subjectProvided()
                .command(SecurityCommands.SuccessfulAuth)
                .with(UserInfo.PART, ui).getMessage()
                .sendNowWith(bus);
	        	
	        }
        }
    }

    private void addAuthenticationCredentials(Message message, String username, String password) {
        QueueSession queueSession = message.getResource(QueueSession.class, Resources.Session.name());
        final HttpSession session = queueSession.getAttribute(HttpSession.class, HttpSession.class.getName());
        session.setAttribute("auth", AuthType.Database.name());
        session.setAttribute("username", username);
        session.setAttribute("password", password);
    }

    private void addAuthenticationCredentials(Message message, String googleid) {
        QueueSession queueSession = message.getResource(QueueSession.class, Resources.Session.name());
        final HttpSession session = queueSession.getAttribute(HttpSession.class, HttpSession.class.getName());
        session.setAttribute("auth", AuthType.OAuth.name());
        session.setAttribute("googleid", googleid);
    }
    
    private Token getAuthenticationToken(Message message) {
    	QueueSession queueSession = message.getResource(QueueSession.class, Resources.Session.name());
        final HttpSession session = queueSession.getAttribute(HttpSession.class, HttpSession.class.getName());
        return (Token)session.getAttribute("token");
    }

    @Override
    public boolean isAuthenticated(Message message) {
    	System.out.println("check Auth");
    	QueueSession queueSession = message.getResource(QueueSession.class, Resources.Session.name());
        final HttpSession session = (queueSession==null)?null:queueSession.getAttribute(HttpSession.class, HttpSession.class.getName());
        return session != null && session.getAttribute("auth")!=null && 
        		((session.getAttribute("username")!=null && session.getAttribute("password")!=null) || 
        		 (session.getAttribute("token")!=null && session.getAttribute("googleid")!=null));
    }

    @Override
    public boolean endSession(Message message) {
        boolean sessionEnded = isAuthenticated(message);
        if (sessionEnded) {
        	QueueSession queueSession = message.getResource(QueueSession.class, Resources.Session.name());
            final HttpSession session = (queueSession==null)?null:queueSession.getAttribute(HttpSession.class, HttpSession.class.getName());
            session.removeAttribute("auth");
            session.removeAttribute("username");
            session.removeAttribute("password");
            session.removeAttribute("token");
            session.removeAttribute("googleid");
            return true;
        } else {
            return false;
        }
    }

}

