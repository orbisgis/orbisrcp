package org.orbisgis.ui.editors.groovy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.orbisgis.core.workspace.ICoreWorkspace;

import java.io.ByteArrayInputStream;

public class GroovyUtils {

    public static final String SCRIPT_FILE_EXTENSION = "groovy";

    public static IFile createNewScript(ICoreWorkspace workspace) throws CoreException {
        final IFolder folder = workspace.getFolder(GroovyWorkspaceExtension.GROOVY_SCRIPT);
        // Make new script file
        IFile tempFile = getUniqueFile(folder, "Script", SCRIPT_FILE_EXTENSION);
        tempFile.create(new ByteArrayInputStream(new byte[]{}), true, new NullProgressMonitor());

        //TODO create the groovy data and store

        return tempFile;
    }

    public static IFile getUniqueFile(IFolder folder, String fileName, String fileExt) {
        IFile file = folder.getFile(fileName + "." + fileExt);
        int index = 1;
        while (file.exists()) {
            file = folder.getFile(fileName + "-" + index + "." + fileExt);
            index++;
        }
        return file;
    }
}
