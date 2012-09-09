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

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Login extends Composite implements LoginPage.Display {

	private static LoginBinder uiBinder = GWT.create(LoginBinder.class);

    interface LoginBinder extends UiBinder<Widget, Login> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
	
	@UiField
	FlowPanel panel;
	
	@UiField
	Header header;
    
    @UiField
    WellForm form;
    
    @UiField
    TextBox username;
    
    @UiField
    PasswordTextBox password;

    @UiField
    SubmitButton signin;
    
    //@UiField
    //Button btnGoogle;

	private Alert alert = null;
	
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
	        .with(SecurityParts.Password, password.getText()) // TODO: encode password
	        .done().sendNowWith(ErraiBus.get());
    }
	
	@Override
	public HasClickHandlers getCreateLink() {
		return header.getCreateLink();
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return header.getExploreLink();
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return header.getHelpLink();
	}

	/**@Override
	public HasClickHandlers getGoogleButton() {
		return btnGoogle;
	}*/
	
	/**
	 * TODO: move the style into pageStyle
	 */
	@Override
	public void fail() {
		if (alert == null) {
			alert = new Alert();
			alert.setType(AlertType.ERROR);
			alert.setText("Login failed ... wrong username and/or password."); // TODO: change to TEXT.LANG...
			Style style = alert.getElement().getStyle();
			style.setPosition(Position.ABSOLUTE);
			style.setRight(5, Unit.PX);
		}
		panel.insert(alert, panel.getWidgetIndex(header)+1);
		Timer t = new Timer() {
            @Override
            public void run() {
                panel.remove(alert);
            }
        };
        t.schedule(5000);
	}

}
