package org.orbisgis.core.workspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

public interface ICoreWorkspace {

    void extend(ICoreWorkspaceExtension extension);

    IProject getOpenProject();

    IFolder getFolder(String path);

    IFile getFile(String path);
}
