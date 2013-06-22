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
package org.teree.client.io;

public class IOFactory {

	public static ISchemeExport getExporter(IOType type) {
		switch (type) {
			case FreeMind: {
				return new FreeMind();
			}
			case JSON: {
				return new Json();
			}
		}
		return null;
	}
	
	public static ISchemeImport getImporter(IOType type) {
		switch (type) {
			case FreeMind: {
				return new FreeMind();
			}
			case JSON: {
				return new Json();
			}
		}
		return null;
	}
	
}
