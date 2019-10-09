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
