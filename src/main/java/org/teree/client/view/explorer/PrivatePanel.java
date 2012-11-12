package org.teree.client.view.explorer;

import org.teree.client.io.FreeMind;
import org.teree.client.text.PrivateHome;
import org.teree.client.view.explorer.event.ImportSchemeHandler;
import org.teree.shared.data.scheme.Node;
import org.teree.shared.data.scheme.Scheme;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class PrivatePanel extends Composite {

	private HorizontalPanel container;

	private DropdownButton importScheme;
	private Button importFreeMind;

	private FileUploadExt file;
	private FileReader reader;
	
	private ImportSchemeHandler handler;
	
	private PrivateHome TEXT = PrivateHome.LANG;

	public PrivatePanel() {

		reader = new FileReader();

		container = new HorizontalPanel();

		file = new FileUploadExt(false);
		file.setVisible(false);

		container.add(file);

		importScheme = new DropdownButton(TEXT.import_from());

		importFreeMind = new Button("FreeMind");
		importScheme.add(importFreeMind);

		container.add(importScheme);

		initWidget(container);

		bind();

	}

	private void bind() {
		importFreeMind.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				file.click();
			}
		});

		reader.addLoadEndHandler(new LoadEndHandler() {
			@Override
			public void onLoadEnd(LoadEndEvent event) {
				if (reader.getError() == null) {
					String data = reader.getStringResult();
					Node root = new FreeMind().importScheme(data);
					Scheme scheme = new Scheme();
					scheme.setRoot(root);
					handler.importScheme(scheme);
				}
			}
		});
		
		reader.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				
			}
		});

		file.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				reader.readAsText(file.getFiles().getItem(0));
			}
		});
	}
	
	public FileReader getReader() {
		return reader;
	}
	
	public void setImportSchemeHandler(ImportSchemeHandler handler) {
		this.handler = handler;
	}

}
