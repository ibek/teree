package org.teree.client.viewer.ui;

import org.teree.shared.ViewerService;
import org.teree.shared.data.Node;
import org.teree.shared.data.NodeContent;
import org.teree.shared.data.Node.NodeLocation;
import org.jboss.errai.ioc.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ViewerUI extends Composite {

  private static ViewerUiBinder uiBinder = GWT.create(ViewerUiBinder.class);

  interface ViewerUiBinder extends
      UiBinder<Widget, ViewerUI> {
  }

  private final Caller<ViewerService> _service;

  @UiField(provided = true)
  Scene _scene;

  public ViewerUI(Caller<ViewerService> service) {
    Window.enableScrolling(false);
    _service = service;
    _scene = new Scene(TestMap.complex());
    initWidget(uiBinder.createAndBindUi(this));
    
  }

}
