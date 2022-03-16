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
package org.orbisgis.ui.editors.groovy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import java.io.ByteArrayInputStream;

import static org.orbisgis.core.utils.CoreWorkspaceUtils.getCoreWorkspace;

public class GroovyUtils {

    public static final String SCRIPT_FILE_EXTENSION = "groovy";

    public static IFolder getGroovyFolder() {
        return getCoreWorkspace().getFolder(GroovyWorkspaceExtension.GROOVY_ROOT);
    }

    public static IFile createNewScript() throws CoreException {
        final IFolder folder = getGroovyFolder();
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
