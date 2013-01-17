package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.protocols.SecurityCommands;
import org.jboss.errai.bus.client.protocols.SecurityParts;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.teree.client.presenter.LoginPage;
import org.teree.client.view.resource.PageStyle;
import org.teree.shared.data.AuthType;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class Login extends TemplateScene implements LoginPage.Display {

	private static LoginBinder uiBinder = GWT.create(LoginBinder.class);

    interface LoginBinder extends UiBinder<Widget, Login> {
    }
	
	@UiField
	FlowPanel panel;
    
    @UiField
    WellForm form;
    
    @UiField
    TextBox username;
    
    @UiField
    PasswordTextBox password;

    @UiField
    SubmitButton signin;
    
    @UiField
    Button btnGoogle;
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }
	
	@UiHandler("signin")
    void onClick(ClickEvent event) {
		MessageBuilder.createMessage("AuthenticationService")
	        .command(SecurityCommands.AuthRequest)
	        .with(MessageParts.ReplyTo, "LoginClient")
	        .with(AuthType.PART, AuthType.Database)
	        .with(SecurityParts.Name, username.getText())
	        .with(SecurityParts.Password, password.getText())
	        .done().sendNowWith(ErraiBus.get());
    }

	@Override
	public HasClickHandlers getGoogleButton() {
		return btnGoogle;
	}

}
