package org.teree.client.visualization;

import java.util.List;

import org.teree.client.view.NodeInterface;
import org.teree.shared.data.common.Node;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The new nodes won't be here inserted ... it has to be done in Scene 
 * 
 */
public interface Renderer<T extends Widget & NodeInterface> {

    public void renderEditor(final Canvas canvas, final List<T> nodes);

    public void renderViewer(final Canvas canvas, final List<T> nodes);

    /**
     * 
     * @param canvas
     * @param nodes
     * @return centered x,y coordinates
     */
    public int[] renderPicture(Canvas canvas, final List<T> nodes);

}
