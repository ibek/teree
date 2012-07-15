package org.teree.client.viewer;

import javax.validation.Validator;
import javax.validation.groups.Default;

import org.teree.shared.data.Node;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

/**
 * TODO: in future, replace with errai validate annotations
 */
public class TereeValidatorFactory extends AbstractGwtValidatorFactory{
    
    @GwtValidation(value = Node.class, groups = {Default.class})
    public interface GwtValidator extends Validator {
    }

    @Override
    public AbstractGwtValidator createValidator() {
      return GWT.create(GwtValidator.class);
    }
    
}
