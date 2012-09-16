package org.teree.client.view;

import javax.annotation.PostConstruct;

import org.teree.client.presenter.JoinPage;
import org.teree.client.view.resource.PageStyle;
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
	
	@PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }
	
	@Override
	public boolean validate() {
		boolean hasError = false;
		boolean hasPasswordError = false;
		
		String username = this.username.getText();
		if (username == null || username.length() < 4) {
			usernameControlGroup.setType(ControlGroupType.ERROR);
			usernameHelpInline.setText("Username should have at least 4 characters.");
			hasError = true;
		} else if (!username.matches("^[A-Za-z]+.*")) {
			usernameControlGroup.setType(ControlGroupType.ERROR);
			usernameHelpInline.setText("Username should start with letter.");
			hasError = true;
		} else if (!username.matches("^[A-Za-z]+[A-Za-z0-9]*")) {
			usernameControlGroup.setType(ControlGroupType.ERROR);
			usernameHelpInline.setText("Allowed characters are letters and numbers only."); 
			hasError = true;
		} else {
			usernameControlGroup.setType(ControlGroupType.NONE);
			usernameHelpInline.setText("");
		}
		
		String name = this.name.getText();
		if (name == null || name.length() < 4) {
			nameControlGroup.setType(ControlGroupType.ERROR);
			nameHelpInline.setText("Name should have at least 4 characters.");
			hasError = true;
		} else if (!name.matches("^[A-Za-z]+")) {
			nameControlGroup.setType(ControlGroupType.ERROR);
			nameHelpInline.setText("Allowed characters are letters only.");
			hasError = true;
		} else {
			nameControlGroup.setType(ControlGroupType.NONE);
			nameHelpInline.setText("");
		}
		
		String password = this.password.getText();
		if (password == null || password.length() < 6) {
			passwordControlGroup.setType(ControlGroupType.ERROR);
			passwordHelpInline.setText("Password should have at least 6 characters.");
			hasError = true;
			hasPasswordError = true;
		} else {
			passwordControlGroup.setType(ControlGroupType.NONE);
			passwordHelpInline.setText("");
		}
		
		String confirmPassword = this.confirmPassword.getText();
		if (confirmPassword == null || password.compareTo(confirmPassword) != 0) {
			passwordControlGroup.setType(ControlGroupType.ERROR);
			confirmPasswordHelpInline.setText("Confirmed password should be same.");
			hasError = true;
		} else {
			if (!hasPasswordError) {
				passwordControlGroup.setType(ControlGroupType.NONE);
			}
			confirmPasswordHelpInline.setText("");
		}
		
		String email = this.email.getText();
		if (email == null || !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			emailControlGroup.setType(ControlGroupType.ERROR);
			emailHelpInline.setText("Email is not valid.");
			hasError = true;
		} else {
			emailControlGroup.setType(ControlGroupType.NONE);
			emailHelpInline.setText("");
		}
		
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
