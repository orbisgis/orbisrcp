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
 */
package org.orbisgis.ui.browser;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.equinox.internal.app.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.orbisgis.core.CoreActivator;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.core.workspace.ICoreWorkspace;
import org.orbisgis.ui.editors.groovy.GroovyEditor;
import org.orbisgis.ui.editors.groovy.GroovyUtils;

public class OpenHandler extends AbstractHandler {

	private static final Logger LOGGER = new Logger(OpenHandler.class);

	private static ICoreWorkspace coreWorkspace;
	public String resource = "http://www.groovy-lang.org";

	static {
		CoreActivator instance = CoreActivator.getInstance();
		try {
			coreWorkspace = instance == null ? null : instance.getCoreWorkspace();
		} catch (Throwable e) {
			coreWorkspace = null;
		}
	}

	@Override
	public Object execute(ExecutionEvent event) {
		try {
			OpenResource(resource);
		} catch (Exception e) {
			LOGGER.error("Unable to open the resource.", e);
		}
		return null;
	}

	public static void OpenResource(String resource) {
		try {
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = null;
			try {
				browser = support.createBrowser("someId");
			} catch (PartInitException e) {
				LOGGER.error("Unable to open a new browser.", e);
			}
			try {
				//browser.openURL(new URL("http://www.eclipse.org"));
				browser.openURL(new URL(resource));
			} catch (PartInitException e) {
				LOGGER.error("Unable to open a new browser.", e);
			} catch (MalformedURLException e) {
				LOGGER.error("Unable to open a new browser.", e);
			}
		} catch (SWTError e) {
			LOGGER.error("Unable to open a new browser.", e);
		}
	}
}
