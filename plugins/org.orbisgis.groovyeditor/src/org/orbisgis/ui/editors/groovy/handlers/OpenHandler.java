package org.orbisgis.ui.editors.groovy.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
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
			IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
			final IFile newScript = GroovyUtils.createNewScript(coreWorkspace);
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
