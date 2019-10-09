package org.orbisgis.ui.editors.groovy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class GroovyEditorControl extends Composite {

    private final GroovyEditor editor;

    public GroovyEditorControl(Composite parent, GroovyEditor editor)
    {
        super(parent, SWT.NONE);
        this.editor = editor;
        setLayout(new FillLayout());
    }

    public GroovyEditor getEditor()
    {
        return editor;
    }
}
