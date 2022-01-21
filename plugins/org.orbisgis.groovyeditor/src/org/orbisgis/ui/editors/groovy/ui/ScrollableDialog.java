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
 */
package org.orbisgis.ui.editors.groovy.ui;

import java.net.URL;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Methods to create and handle a scrollable dialog.
 *
 * @author Adrien Bessy, CNRS
 */
public class ScrollableDialog extends TitleAreaDialog {
    private String title;
    private String text;
    private List<URL> urls;

    public ScrollableDialog(Shell parentShell, String title, String text, List<URL> urls) {
        super(parentShell);
        this.title = title;
        this.text = text;
        this.urls = urls;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea (parent); // Let the dialog create the parent composite

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true; // Layout vertically, too! 
        gridData.verticalAlignment = GridData.FILL;

        Table table = new Table(composite, SWT.BORDER | SWT.V_SCROLL);
        table.setHeaderVisible(true);
        String[] titles = { "Name", "Path" };
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(table, SWT.NULL);
            column.setText(titles[loopIndex]);
         }
        /*
        String[]lines = scrollableText.split(System.getProperty("line.separator"));
        for(String path : lines){
            TableItem item = new TableItem(table, SWT.NULL);
            if (path != "") {
                String[] parts = path.split("/");
            	int length = parts.length;
            	item.setText(0, parts[length-1]);
            	item.setText(1, path);
            }
        }
        */

        System.out.println("urls : " + urls);
        //List<URL> urlList = Arrays.asList(scrollableText);
        for (int i = 0; i < urls.size(); i++) {
        	System.out.println("i : " + i);
        	URL url = urls.get(i);
        	System.out.println("url : " + url);
        	String path = url.getPath();
        	System.out.println("path : " + path);
            TableItem item = new TableItem(table, SWT.NULL);
            if (path.toString() != "") {
                String[] parts = path.toString().split("/");
                System.out.println("parts : " + parts);
            	int length = parts.length;
            	item.setText(0, parts[length-1]);
            	item.setText(1, path.toString());
            }
        }

        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            table.getColumn(loopIndex).pack();
          }
        
        table.setLayoutData(gridData);
 
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