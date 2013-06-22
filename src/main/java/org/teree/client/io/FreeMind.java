/*******************************************************************************
 * Copyright (c) 2013 ibek.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     ibek - initial API and implementation
 ******************************************************************************/
package org.teree.client.io;

import java.util.List;

import org.teree.client.CurrentPresenter;
import org.teree.shared.data.common.Text;
import org.teree.shared.data.common.ImageLink;
import org.teree.shared.data.common.Link;
import org.teree.shared.data.common.Node;
import org.teree.shared.data.common.Node.NodeLocation;
import org.teree.shared.data.common.NodeCategory;
import org.teree.shared.data.common.Scheme;
import org.teree.shared.data.common.StructureType;
import org.teree.shared.data.tree.Tree;
import org.teree.shared.data.tree.TreeType;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class FreeMind implements ISchemeImport, ISchemeExport {

	private static final String VERSION = "0.9.0";

	@Override
	public void exportScheme(Scheme scheme) {
		Node root = null;
		if (scheme.getStructure() == StructureType.Tree) {
			root = ((Tree)scheme).getRoot();
		} else {
			return;
		}
		Document doc = XMLParser.createDocument();
		Element map = doc.createElement("map");
		map.setAttribute("version", VERSION);
		
		Element rn = doc.createElement("node");
		setElementStyle(root, doc, rn);
		rn.setAttribute("TEXT", root.getContent().toString());
		map.appendChild(rn);
		createElements(root, doc, rn);
		
		doc.appendChild(map);
		
		CurrentPresenter.getInstance()
			.getPresenter()
			.getTemplate()
			.sendDownloadRequest(scheme.toString(), "freemind", doc.toString());
	}

	@Override
	public void importScheme(String data) throws Exception {
		Node root = new Node();
		Document doc = XMLParser.parse(data);
		com.google.gwt.xml.client.Node r = getFirstNode(doc.getChildNodes().item(0).getChildNodes());
		if (r != null) {
			String content = r.getAttributes().getNamedItem("TEXT").getNodeValue();
			Text it = new Text();
			it.setText(content);
			root.setContent(it);
			insertChildNodes(root, r.getChildNodes());
		}
		Tree scheme = new Tree();
		scheme.setRoot(root);
		CurrentPresenter.getInstance().getPresenter().importScheme(scheme);
	}
	
	private void createElements(Node root, Document doc, Element parent) {
		List<Node> childNodes = root.getChildNodes();
		if (childNodes == null) {
			return;
		}
		for (Node node : childNodes) {
			Element en = doc.createElement("node");
			en.setAttribute("POSITION", node.getLocation().name().toLowerCase());
			setElementStyle(node, doc, en);
			switch (node.getType()) {
				case IconText:
				case MathExpression:
				case Connector:
				case Percent: {
					en.setAttribute("TEXT", node.getContent().toString());
					parent.appendChild(en);
					break;
				}
				case ImageLink: {
					ImageLink il = (ImageLink)node.getContent();
					en.setAttribute("TEXT", il.getUrl());
					en.setAttribute("LINK", il.getUrl());
					parent.appendChild(en);
					break;
				}
				case Link: {
					Link l = (Link)node.getContent();
					en.setAttribute("TEXT", l.getUrl());
					en.setAttribute("LINK", l.getUrl());
					parent.appendChild(en);
					break;
				}
			}
			createElements(node, doc, en);
		}
	}
	
	/**
	 * TODO style for FreeMind - from the category
	 * @param node
	 * @param doc
	 * @param enode
	 */
	private void setElementStyle(Node node, Document doc, Element enode) {
		/**NodeStyle ns = node.getStyle();
		if (ns != null) {
			if (ns.isBold()) {
				Element font = doc.createElement("font");
				font.setAttribute("BOLD", "true");
				font.setAttribute("NAME", "SansSerif");
				font.setAttribute("SIZE", "12");
				enode.appendChild(font);
			}
		}*/
	}
	
	private com.google.gwt.xml.client.Node getFirstNode(NodeList list) {
		com.google.gwt.xml.client.Node n = null;
		for(int i=0; i<list.getLength(); ++i) {
			com.google.gwt.xml.client.Node nl = list.item(i);
			if (nl.getNodeName().compareTo("node") == 0) {
				return nl;
			}
		}
		return n;
	}
	
	private void insertChildNodes(Node root, NodeList nl) {
		for (int i=0; i<nl.getLength(); i++) {
			com.google.gwt.xml.client.Node n = nl.item(i);
			if (n.getNodeName().compareTo("node") == 0) {
				NamedNodeMap attr = n.getAttributes();
				Node cn = new Node();
				
				com.google.gwt.xml.client.Node content;
				if ((content = attr.getNamedItem("LINK")) != null) {
					Link link = new Link();
					link.setUrl(content.getNodeValue());
					if ((content = attr.getNamedItem("TEXT")) != null) {
						link.setText(content.getNodeValue());
					}
					cn.setContent(link);
				} else if ((content = attr.getNamedItem("TEXT")) != null) {
					Text it = new Text();
					it.setText(content.getNodeValue());
					cn.setContent(it);
				} else {
					Text it = new Text();
					it.setText("unidentified");
					cn.setContent(it);
				}
				
				if ((content = attr.getNamedItem("POSITION")) != null && content.getNodeValue().compareTo("left") == 0) {
					cn.setLocation(NodeLocation.LEFT);
				} else {
					cn.setLocation(NodeLocation.RIGHT);
				}
				
				root.addChild(cn);
				insertChildNodes(cn, n.getChildNodes());
				
			}
			
		}
	}
	
}
