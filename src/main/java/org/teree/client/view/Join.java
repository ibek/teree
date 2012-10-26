package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.JoinPage;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.view.validate.FormValidator;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class Join extends TemplateScene implements JoinPage.Display {

	private static JoinBinder uiBinder = GWT.create(JoinBinder.class);

    interface JoinBinder extends UiBinder<Widget, Join> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
	
	@UiField
	FlowPanel panel;
    
    @UiField
    WellForm form;
    
    @UiField
    ControlGroup usernameControlGroup;
    
    @UiField
    ControlGroup nameControlGroup;
    
    @UiField
    ControlGroup passwordControlGroup;
    
    @UiField
    ControlGroup emailControlGroup;
    
    @UiField
    TextBox username;
    
    @UiField
    HelpInline usernameHelpInline;
    
    @UiField
    TextBox name;
    
    @UiField
    HelpInline nameHelpInline;
    
    @UiField
    PasswordTextBox password;
    
    @UiField
    HelpInline passwordHelpInline;
    
    @UiField
    PasswordTextBox confirmPassword;
    
    @UiField
    HelpInline confirmPasswordHelpInline;
    
    @UiField
    TextBox email;
    
    @UiField
    HelpInline emailHelpInline;

    @UiField
    SubmitButton register;
    
    @UiField
    Button cancel;
    
    @UiField
    Button btnGoogle;
    
    private FormValidator validator;
	
	@PostConstruct
    public void init() {
		validator = new FormValidator();
        initWidget(uiBinder.createAndBindUi(this));
    }
	
	@Override
	public boolean validate() {
		boolean hasError = false;
		
		hasError = hasError || validator.validateUsername(this.username.getText(), usernameControlGroup, usernameHelpInline);
		hasError = hasError || validator.validateName(this.name.getText(), nameControlGroup, nameHelpInline);
		hasError = hasError || validator.validatePassword(this.password.getText(), this.confirmPassword.getText(),
				passwordControlGroup, passwordHelpInline, confirmPasswordHelpInline);
		hasError = hasError || validator.validateEmail(this.email.getText(), emailControlGroup, emailHelpInline);
		
		return !hasError;
	}
	
	@Override
	public HasClickHandlers getRegisterButton() {
		return register;
	}
	
	@UiHandler("cancel")
    void onCancel(ClickEvent event) {
		form.reset();
    }

	@Override
	public HasClickHandlers getGoogleButton() {
		return btnGoogle;
	}

	@Override
	public UserInfo getUserInfo() {
		UserInfo ui = new UserInfo();
		ui.setUsername(username.getText());
		ui.setEmail(email.getText());
		ui.setName(name.getText());
		return ui;
	}

	@Override
	public String getPassword() {
		return password.getText();
	}

}
