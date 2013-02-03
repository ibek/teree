package org.teree.client.view.explorer;

import org.teree.client.CurrentPresenter;
import org.teree.client.io.IOFactory;
import org.teree.client.io.IOType;
import org.teree.client.text.UIConstants;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class PrivatePanel extends Composite {

	private HorizontalPanel container;

	private DropdownButton importScheme;
	private NavLink importFreeMind;
	private NavLink importJSON;

	private FileUploadExt file;
	private FileReader reader;

	public PrivatePanel() {

		reader = new FileReader();

		container = new HorizontalPanel();

		file = new FileUploadExt(false);
		file.setVisible(false);

		container.add(file);

		importScheme = new DropdownButton(UIConstants.LANG.import_from());

		importJSON = new NavLink("JSON");
		importScheme.add(importJSON);

		importFreeMind = new NavLink("FreeMind");
		importScheme.add(importFreeMind);

		container.add(importScheme);

		initWidget(container);

		bind();

	}

	private IOType it;
	private void bind() {
		
		importJSON.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				it = IOType.JSON;
				file.click();
			}
		});
		
		importFreeMind.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				it = IOType.FreeMind;
				file.click();
			}
		});

		reader.addLoadEndHandler(new LoadEndHandler() {
			@Override
			public void onLoadEnd(LoadEndEvent event) {
				if (reader.getError() == null) {
					String data = reader.getStringResult();
					try {
						IOFactory.getImporter(it).importScheme(data);
					} catch (Exception ex) {
						CurrentPresenter.getInstance().getPresenter().displayError("Cannot import the scheme");
					}
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

}
