package org.teree.shared.data.common;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class MathExpression {

    private String expression;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return expression;
	}
    
}
