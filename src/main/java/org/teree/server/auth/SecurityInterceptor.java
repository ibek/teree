package org.teree.server.auth;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpSession;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.jboss.errai.bus.client.protocols.SecurityParts;
import org.jboss.errai.bus.server.api.RpcContext;

@RequireAuthentication
@Interceptor
public class SecurityInterceptor implements Serializable {

	private static final long serialVersionUID = -6545213208008101417L;
	
	@Inject
    MessageBus bus;

	public SecurityInterceptor() {
    }

    @AroundInvoke
    public Object isAuthenticated(InvocationContext invocationContext)
        throws Exception {
    	
        HttpSession session = RpcContext.getHttpSession();
        if (session != null && session.getAttribute("auth")!=null && 
        		((session.getAttribute("username")!=null && session.getAttribute("password")!=null) || 
        		 (session.getAttribute("token")!=null && session.getAttribute("googleid")!=null))) {
        	return invocationContext.proceed();
        } else {
        	MessageBuilder.createMessage()
        		.toSubject("LoginClient")
        		.command(SecurityCommands.SecurityChallenge)
        		.getMessage().sendNowWith(bus);
        	return null;
        }
    }
	
}
