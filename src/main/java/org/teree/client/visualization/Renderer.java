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
package org.teree.client.visualization;

import java.util.List;

import org.teree.client.view.NodeInterface;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * The new nodes won't be here inserted ... it has to be done in Scene 
 * 
 */
public interface Renderer<T extends Widget & NodeInterface> {

    public void render(final Canvas canvas, final List<T> nodes);

    /**
     * 
     * @param canvas
     * @param nodes
     * @return centered x,y coordinates
     */
    public int[] renderPicture(Canvas canvas, final List<T> nodes);

}
