package org.teree.client.view.viewer.format;

import java.util.List;

import org.teree.shared.data.scheme.ImageLink;
import org.teree.shared.data.scheme.Link;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.NodeStyle;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
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
				case String: {
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
	
	public Node importScheme(String data) {
		Node root = new Node();
		// TODO: import scheme from freemind
		return root;
	}
	
}
