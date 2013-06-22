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
package org.teree.client.visualization.utils;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class Shapes {

	public static void drawEllipse(Context2d ctx, int x, int y, int w, int h) {
		double kappa = .5522848;
		double ox = (w / 2) * kappa, // control point offset horizontal
		oy = (h / 2) * kappa, // control point offset vertical
		xe = x + w, // x-end
		ye = y + h, // y-end
		xm = x + w / 2, // x-middle
		ym = y + h / 2; // y-middle

		ctx.setLineWidth(2.0);
		ctx.beginPath();
		ctx.moveTo(x, ym);
		ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		ctx.closePath();
		ctx.setLineWidth(2.0);
		ctx.setStrokeStyle(CssColor.make("#08C"));
		ctx.fill();
		ctx.stroke();
	}

	public static void drawCurve(Context2d context, int x1, int y1, int x2, int y2,
			int curveness) {
		context.moveTo(x1, y1);
		context.bezierCurveTo(x1 + curveness, y1, x2 - curveness, y2, x2, y2);
	}

	public static void drawLine(Context2d context, int x1, int y1, int x2, int y2) {
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
	}
	
}
