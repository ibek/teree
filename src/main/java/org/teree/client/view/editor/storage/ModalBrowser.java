package org.teree.client.view.editor.storage;

import java.util.List;

import org.teree.client.view.editor.NodeWidget;
import org.teree.client.view.editor.storage.event.BrowserItemDelete;
import org.teree.client.view.editor.storage.event.BrowserItemDeleteHandler;
import org.teree.client.view.editor.storage.event.BrowserItemDeleteRequestHandler;
import org.teree.client.view.editor.storage.event.BrowserItemSelected;
import org.teree.client.view.editor.storage.event.BrowserItemSelectedHandler;
import org.teree.client.view.editor.storage.event.BrowserLoadRequestHandler;
import org.teree.client.view.editor.storage.event.BrowserRefreshRequestHandler;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class ModalBrowser extends Composite {

	private FlowPanel container;
	private Modal browser;

    private Browser activeBrowser;
    private Browser publicBrowser;
    private Browser myBrowser;
    private FileUpload fileUpload;
    private BrowserLoadRequestHandler browserLoadRequestHandler;
    private BrowserItemDeleteRequestHandler browserItemDeleteRequestHandler;

    private ItemType type;
    private NodeWidget edited;
	
	public ModalBrowser() {
		
		container = new FlowPanel();
		browser = new Modal(false);
		publicBrowser = new Browser();
		
		TabPanel tp = new TabPanel();
		
		TabLink ptl = new TabLink();
		ptl.setText("Public storage");
		TabPane publicStorage = new TabPane();
		publicStorage.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		publicStorage.add(publicBrowser);
		ptl.setTablePane(publicStorage);
		ptl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ItemType type = activeBrowser.getType();
				activeBrowser = publicBrowser;
				browserLoadRequestHandler.loadRequest(type, true);
			}
		});

		myBrowser = new Browser();
		TabLink mtl = new TabLink();
		mtl.setText("My storage");
		TabPane myStorage = new TabPane();
		myStorage.setActive(true);
		myStorage.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		myStorage.add(myBrowser);
		mtl.setTablePane(myStorage);
		mtl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ItemType type = activeBrowser.getType();
				activeBrowser = myBrowser;
				browserLoadRequestHandler.loadRequest(type, false);
			}
		});
		
		activeBrowser = myBrowser;
		
		BrowserItemSelectedHandler bish = new BrowserItemSelectedHandler() {
			@Override
			public void selected(BrowserItemSelected event) {
				browser.hide();
				browserItemSelected(event.getItem());
			}
		};
		
		BrowserItemDeleteHandler bidrh = new BrowserItemDeleteHandler() {
			@Override
			public void delete(BrowserItemDelete event) {
				browserItemDeleteRequestHandler.deleteItemRequest(event.getItem());
			}
		};

    	myBrowser.addHandler(bish, BrowserItemSelected.TYPE);
    	publicBrowser.addHandler(bish, BrowserItemSelected.TYPE);
    	myBrowser.addHandler(bidrh, BrowserItemDelete.TYPE);
    	publicBrowser.addHandler(bidrh, BrowserItemDelete.TYPE);
		
		tp.add(mtl);
		tp.add(ptl);
		
		browser.add(tp);
		
		ModalFooter mf = new ModalFooter();
		fileUpload = new FileUpload();
		mf.add(fileUpload);
		browser.add(mf);
		
		container.add(browser);
		
		initWidget(container);
		
		bind();
		
	}
	
	private String getTitle(ItemType type) {
		switch(type) {
			case Image: {
				return "Choose image";
			}
		}
		return "Choose file";
	}
	
	private void bind() {
    	fileUpload.setBrowserRefreshRequestHandler(new BrowserRefreshRequestHandler() {
			@Override
			public void refresh() {
				browserLoadRequestHandler.loadRequest(activeBrowser.getType(), activeBrowser == publicBrowser);
			}
		});
	}
	
	public void show() {
		browser.show();
		browserLoadRequestHandler.loadRequest(type, activeBrowser == publicBrowser);
	}
	
	public void hide() {
		browser.hide();
	}
	
	public void setEditedNodeWidget(ItemType type, NodeWidget edited) {
		this.type = type;
		this.edited = edited;
		browser.setTitle(getTitle(type));
	}
    
    public void setBrowserItems(List<?> items, ItemType type) {
    	activeBrowser.setBrowserItems(items, type);
    }
    
    public void setBrowserLoadRequestHandler(BrowserLoadRequestHandler handler) {
		browserLoadRequestHandler = handler;
	}
    
    public void setBrowserItemDeleteRequestHandler(BrowserItemDeleteRequestHandler handler) {
		browserItemDeleteRequestHandler = handler;
	}
    
    private void browserItemSelected(ItemWidget iw) {
    	edited.setBrowserItem(iw);
    }
	
}
