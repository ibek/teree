package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.SettingsPage;
import org.teree.client.view.resource.PageStyle;
import org.teree.client.view.validate.FormValidator;
import org.teree.shared.data.UserInfo;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.github.gwtbootstrap.client.ui.TabPanel;
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

public class Settings extends TemplateScene implements SettingsPage.Display {

	private static SettingsBinder uiBinder = GWT.create(SettingsBinder.class);

    interface SettingsBinder extends UiBinder<Widget, Settings> {
    }
    
	static {
		PageStyle.INSTANCE.css().ensureInjected(); 
	}
	
	@UiField
	FlowPanel panel;
	
	@UiField
	TabPanel tabPanel;
	
	private FormValidator validator;
	
	@PostConstruct
    public void init() {
		validator = new FormValidator();
        initWidget(uiBinder.createAndBindUi(this));
    }
	
	@Override
	public void setCurrentUser(UserInfo user) {
		super.setCurrentUser(user);
		
		onResetPForm(null);
    	
    	// e.g. Google users cannot change the password
    	changePasswordForm.setVisible(user.getUsername() != null);
    	
	}
	
	// Profile tab
    
    @UiField
    WellForm profileForm;
    
    @UiField
    ControlGroup nameControlGroup;
    
    @UiField
    ControlGroup emailControlGroup;
    
    @UiField
    TextBox name;
    
    @UiField
    HelpInline nameHelpInline;
    
    @UiField
    TextBox email;
    
    @UiField
    HelpInline emailHelpInline;

    @UiField
    SubmitButton updateProfile;
    
    @UiField
    Button resetPForm;
	
	@UiHandler("resetPForm")
    void onResetPForm(ClickEvent event) {
		UserInfo user = header.getCurrentUser();
    	name.setText(user.getName());
    	email.setText(user.getEmail());
    }
	
	private boolean validateProfileDetails() {
		boolean hasError = false;
		
		hasError = hasError || validator.validateName(this.name.getText(), nameControlGroup, nameHelpInline);
		hasError = hasError || validator.validateEmail(this.email.getText(), emailControlGroup, emailHelpInline);
		
		return hasError;
	}

	@Override
	public UserInfo getProfileDetails() {
		if (validateProfileDetails()) {
			return null;
		}
		UserInfo ui = header.getCurrentUser();
		ui.setEmail(email.getText());
		ui.setName(name.getText());
		return ui;
	}

	@Override
	public HasClickHandlers getProfileUpdateButton() {
		return updateProfile;
	}
    
    // Account Settings tab
    
    @UiField
    WellForm changePasswordForm;
    
    @UiField
    PasswordTextBox oldPassword;
    
    @UiField
    HelpInline oldPasswordHelpInline;
    
    @UiField
    PasswordTextBox password;
    
    @UiField
    HelpInline passwordHelpInline;
    
    @UiField
    PasswordTextBox confirmPassword;
    
    @UiField
    HelpInline confirmPasswordHelpInline;
    
    @UiField
    ControlGroup passwordControlGroup;
    
    @UiField
    Button resetCPForm;
    
    @UiField
    SubmitButton changePassword;
    
    @UiField
    Button deleteAccount;

	@Override
	public boolean validateChangePassword() {
		boolean hasError = false;
		
		hasError = hasError || validator.validatePassword(this.oldPassword.getText(), passwordControlGroup, oldPasswordHelpInline);
		hasError = hasError || validator.validatePassword(this.password.getText(), this.confirmPassword.getText(), 
				passwordControlGroup, passwordHelpInline, confirmPasswordHelpInline);
		
		return hasError;
	}

	@Override
	public String getOldPassword() {
		return oldPassword.getText();
	}

	@Override
	public String getNewPassword() {
		return password.getText();
	}

	@Override
	public HasClickHandlers getChangePasswordButton() {
		return changePassword;
	}
	
	@UiHandler("resetCPForm")
    void onResetCPForm(ClickEvent event) {
		changePasswordForm.reset();
    }

	@Override
	public HasClickHandlers getDeleteAccountButton() {
		return deleteAccount;
	}

}
