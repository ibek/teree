package org.teree.client.view.common;

import org.teree.shared.data.common.Node;

public interface NodeWidgetFactory<T> {

	public T create(Node node);
	
}
