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
