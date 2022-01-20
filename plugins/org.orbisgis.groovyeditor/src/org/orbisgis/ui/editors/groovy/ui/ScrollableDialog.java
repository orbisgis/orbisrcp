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
 *
 * @author Adrien Bessy, CNRS
 */
package org.orbisgis.ui.editors.groovy.ui;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ScrollableDialog extends TitleAreaDialog {
    private String title;
    private String text;
    private String scrollableText;

    public ScrollableDialog(Shell parentShell, String title, String text, String scrollableText) {
        super(parentShell);
        this.title = title;
        this.text = text;
        this.scrollableText = scrollableText;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea (parent); // Let the dialog create the parent composite

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true; // Layout vertically, too! 
        gridData.verticalAlignment = GridData.FILL;

        Text scrollable = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
        scrollable.setLayoutData(gridData);
        scrollable.setText(scrollableText);

        return composite;
    }

    @Override
    public void create() {
        super.create();
        // This is not necessary; the dialog will become bigger as the text grows but at the same time,
        // the user will be able to see all (or at least more) of the error message at once
        //getShell ().setSize (300, 300);
        setTitle(title);
        setMessage(text);

    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button okButton = createButton(parent, OK, "OK", true);
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
    }

    @Override
    protected boolean isResizable() {
        return true; // Allow the user to change the dialog size!
    }
}