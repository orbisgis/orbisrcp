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
package org.orbisgis.core.ui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Toolbar extends Canvas {

    private static final int HORIZONTAL_SPACING = 0;
    private static final int VERTICAL_SPACING = 0;
    private static final int MARGIN_HEIGHT = 0;
    private static final int MARGIN_WIDTH = 2;

    public Toolbar(Composite parent, int style) {
        super(parent, style);

        GridLayout gl = new GridLayout(1, true);
        gl.horizontalSpacing = HORIZONTAL_SPACING;
        gl.verticalSpacing = VERTICAL_SPACING;
        gl.marginHeight = MARGIN_HEIGHT;
        gl.marginWidth = MARGIN_WIDTH;
        this.setLayout(gl);
    }
}
