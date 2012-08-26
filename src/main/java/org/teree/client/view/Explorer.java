package org.teree.client.view;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.teree.client.presenter.MapExplorer;
import org.teree.client.view.explorer.Scene;
import org.teree.client.view.resource.ViewStyle;
import org.teree.shared.data.Map;

public class Explorer extends Composite implements MapExplorer.Display {

	private static ExplorerBinder uiBinder = GWT.create(ExplorerBinder.class);

    interface ExplorerBinder extends UiBinder<Widget, Explorer> {
    }
    
	static {
		ViewStyle.INSTANCE.css().ensureInjected(); 
	}

    @UiField
    Button btnNew;

    @UiField
    Label linkExplore;

    @UiField
    Label linkHelp;
    
    @UiField
    Scene scene;
    
    @UiField
    UserWidget user;
    
    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

	@Override
	public HasClickHandlers getNewButton() {
		return btnNew;
	}

	@Override
	public HasClickHandlers getExploreLink() {
		return linkExplore;
	}

	@Override
	public HasClickHandlers getHelpLink() {
		return linkHelp;
	}

	@Override
	public void setData(List<Map> maps) {
		scene.setData(maps);
	}

}
