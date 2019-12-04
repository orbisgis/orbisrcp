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

import org.orbisgis.core.workspace.ICoreWorkspaceExtension;

import java.util.ArrayList;
import java.util.List;

public class GroovyWorkspaceExtension implements ICoreWorkspaceExtension {

    public static final String GROOVY_ROOT = ROOT + "Groovy";

    @Override
    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        paths.add(GROOVY_ROOT);
        return paths;
    }
}
