package org.teree.client.view.type;

import java.util.List;

import org.teree.client.view.editor.event.SelectedNodeListener;
import org.teree.shared.data.common.Node;

import com.github.gwtbootstrap.client.ui.constants.IconType;

public interface Actions<T> {
	
	/**
	 * 
	 * @param node
	 * @param childNode
	 * @return new node widget identifier
	 */
	public void insertChildNode(Node childNode);
	
	public void insertNodeBefore(Node inserted);
	
	public void insertNodeAfter(Node inserted);
	
	public void editNode();
	
	public void removeNode();
	
	public void update(Node changed);
	
	public void checkAllNodes();
	
	public void selectLeftNode();
	
	public void selectRightNode();
	
	public void selectUpperNode();
	
	public void selectUnderNode();
	
	public void selectNode(T node);
	
	public T getSelectedNode();
	
	public void addSelectedNodeListener(SelectedNodeListener<T> snl);
	
	public void copyNode();
	
	public void cutNode();
	
	public void pasteNode();

	public void setNodeIcon(IconType icon);
	
	public void splitAndConnectNode();
	
	public void mergeConnectorNode();
	
	public void collapseAll(List<T> widgets, boolean collapse);
	
	public void center();
	
	public List<T> getNodeWidgets();
	
}
