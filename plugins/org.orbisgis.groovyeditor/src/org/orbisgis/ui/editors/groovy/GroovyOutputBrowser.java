/*
 * Groovy Editor (GE) is a library that brings a groovy console to the Eclipse RCP.
 * GE is developed by CNRS http://www.cnrs.fr/.
 *
 * GE is part of the OrbisGIS project. GE is free software;
 * you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation;
 * version 3.0 of the License.
 *
 * GE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details http://www.gnu.org/licenses.
 *
 *
 *For more information, please consult: http://www.orbisgis.org
 *or contact directly: info_at_orbisgis.org
 *
 */
package org.orbisgis.ui.editors.groovy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.jkiss.dbeaver.ui.UIUtils;

/**
 */
public class GroovyOutputBrowser extends ViewPart {
	private static Browser browser = null;
	public static BrowserHandler browserHandler = new BrowserHandler();

	@Override
	public void createPartControl(Composite parent) {
		Composite group = UIUtils.createPlaceholder(parent, 1);
		group.setLayout(new FillLayout());
		browser = new Browser(group, SWT.V_SCROLL | SWT.H_SCROLL);
		browser.setText("Nothing to show.");
	}

	@Override
	public void setFocus() {
	}

	private static class BrowserHandler {
		public void setText(String html) {
			Display.getDefault().asyncExec(()->{
				browser.setText(html);
			});
		}
	}
}
