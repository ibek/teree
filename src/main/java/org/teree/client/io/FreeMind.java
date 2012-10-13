package org.teree.client.io;

import java.util.List;

import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;
import org.teree.shared.data.scheme.Node.NodeLocation;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class FreeMind {

	private static final String VERSION = "0.9.0";
	
	public String exportScheme(Node root) {
		Document doc = XMLParser.createDocument();
		Element map = doc.createElement("map");
		map.setAttribute("version", VERSION);
		
		Element rn = doc.createElement("node");
		setElementStyle(root, doc, rn);
		rn.setAttribute("TEXT", root.getContent().toString());
		map.appendChild(rn);
		createElements(root, doc, rn);
		
		doc.appendChild(map);
		
		return doc.toString();
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
				case IconText: {
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
	
	private void setElementStyle(Node node, Document doc, Element enode) {
		NodeStyle ns = node.getStyle();
		if (ns != null) {
			if (ns.isBold()) {
				Element font = doc.createElement("font");
				font.setAttribute("BOLD", "true");
				font.setAttribute("NAME", "SansSerif");
				font.setAttribute("SIZE", "12");
				enode.appendChild(font);
			}
		}
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
	
	public Node importScheme(String data) {
		Node root = new Node();
		try {
			Document doc = XMLParser.parse(data);
			com.google.gwt.xml.client.Node r = getFirstNode(doc.getChildNodes().item(0).getChildNodes());
			if (r != null) {
				String content = r.getAttributes().getNamedItem("TEXT").getNodeValue();
				root.setContent(content);
				insertChildNodes(root, r.getChildNodes());
			}
		} catch(Exception ex) {
			root = null;
		}
		return root;
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
					cn.setContent(content.getNodeValue());
				} else {
					cn.setContent("unidentified");
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
