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
package org.orbisgis.ui.editors.groovy.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.GroovyEditor;
import org.orbisgis.ui.editors.groovy.GroovyUtils;

public class OpenHandler extends AbstractHandler {

	private static final Logger LOGGER = new Logger(OpenHandler.class);

	@Override
	public Object execute(ExecutionEvent event) {
		try {
			IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
			final IFile newScript = GroovyUtils.createNewScript();
			openResource(newScript, workbenchWindow);
		} catch (Exception e) {
			LOGGER.error("Unable to create a new groovy script.", e);
		}
		return null;
	}

	private void openResource(IFile resource, IWorkbenchWindow window) {
		try {
			window.getActivePage().openEditor(new FileEditorInput(resource), GroovyEditor.class.getName());
		} catch (PartInitException e) {
			LOGGER.error("Unable to open a new editor.", e);
		}
	}
}
