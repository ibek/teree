package org.teree.client.view.validate;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;

public class FormValidator {
	
	private org.teree.client.text.FormValidator TEXT = org.teree.client.text.FormValidator.LANG;
	
	public boolean validateUsername(String username, ControlGroup cg, HelpInline hi) {
		boolean hasError = false;
		if (username == null || username.length() < 4) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.username_short());
			hasError = true;
		} else if (!username.matches("^[A-Za-z]+.*")) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.username_noletter());
			hasError = true;
		} else if (!username.matches("^[A-Za-z]+[A-Za-z0-9]*")) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.username_bad_chars()); 
			hasError = true;
		} else {
			cg.setType(ControlGroupType.NONE);
			hi.setText("");
		}
		return hasError;
	}

	public boolean validateName(String name, ControlGroup cg, HelpInline hi) {
		boolean hasError = false;
		if (name == null || name.length() < 4) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.name_short());
			hasError = true;
		} else if (!name.matches("^[A-Za-z]+([ ]?[A-Za-z]+)*")) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.name_bad_chars());
			hasError = true;
		} else {
			cg.setType(ControlGroupType.NONE);
			hi.setText("");
		}
		return hasError;
	}
	
	public boolean validateEmail(String email, ControlGroup cg, HelpInline hi) {
		boolean hasError = false;
		if (email == null || !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.email_not_valid());
			hasError = true;
		} else {
			cg.setType(ControlGroupType.NONE);
			hi.setText("");
		}
		return hasError;
	}
	
	public boolean validatePassword(String password, ControlGroup cg, HelpInline hi) {
		boolean hasError = false;
		if (password == null || password.length() < 6) {
			cg.setType(ControlGroupType.ERROR);
			hi.setText(TEXT.password_short());
			hasError = true;
		} else {
			cg.setType(ControlGroupType.NONE);
			hi.setText("");
		}
		return hasError;
	}
	
	public boolean validatePassword(String password, String confirmPassword, ControlGroup cg, HelpInline hi, HelpInline confirmHi) {
		boolean hasError = validatePassword(password, cg, hi);
		if (confirmPassword == null || password.compareTo(confirmPassword) != 0) {
			cg.setType(ControlGroupType.ERROR);
			confirmHi.setText(TEXT.password_confirmation()); // passwords should be equal
			hasError = true;
		} else {
			if (!hasError) {
				cg.setType(ControlGroupType.NONE);
			}
			confirmHi.setText("");
		}
		return hasError;
	}
	
	
	
}
