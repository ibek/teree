package org.teree.client.io;

import org.teree.client.CurrentPresenter;
import org.teree.shared.data.common.Scheme;

public class Json implements ISchemeImport, ISchemeExport {

	@Override
	public void exportScheme(Scheme scheme) {
		CurrentPresenter.getInstance().getPresenter().exportJSON(scheme.getOid(), scheme.toString());
	}

	@Override
	public void importScheme(String data) {
		CurrentPresenter.getInstance().getPresenter().importJSON(data);
	}

}
