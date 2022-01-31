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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

import groovy.lang.GroovySystem;

/**
 * Methods to create and handle a dialog showing a table of urls.
 *
 * @author Adrien Bessy, CNRS
 */
public class ScrollableDialog extends TitleAreaDialog {
    private String title;
    private List<URL> urls; 
    private int countFile = 0;

    public ScrollableDialog(Shell parentShell, String title, List<URL> urls) {
        super(parentShell);
        this.title = title;
        this.urls = urls;
    }

	/**
	 * Create a dialog displaying the modules, its paths and the class number for each module loaded in groovyShell.
	 *
	 * @param parent the parent composite
	 */
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
        String[] titles = { "Name", "Path", "Class file number" };
        
        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            TableColumn column = new TableColumn(table, SWT.NULL);
            column.setText(titles[loopIndex]);
         }

        for (int i = 0; i < urls.size(); i++) {
        	URL url = urls.get(i);
        	String path = url.getPath();
            TableItem item = new TableItem(table, SWT.NULL);
            if (path.toString() != "") {
                String[] parts = path.toString().split("/");
            	int length = parts.length;
            	
            	int fileNumber = countClassFile(path);
            	countFile = 0;
                
            	item.setText(0, parts[length-1].replace("!", ""));
            	item.setText(1, path.toString());
            	item.setText(2, "" + fileNumber);
            }
        }

        for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
            table.getColumn(loopIndex).pack();
          }
        
        table.setLayoutData(gridData);
 
        return composite;
    }
    
	/**
	 * Count class files in jar or directory.
	 *
	 * @param path the path of a directory or a jar
	 */
    private int countClassFile(String path) { 
    	if(path.endsWith("!")){
    		String jarPath = path.replace("!", "");
	    	try (JarFile jarFile = new JarFile(jarPath)){
				final Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
		            final JarEntry entry = entries.nextElement();
		            if (entry.getName().contains(".")) {
		                if(entry.getName().endsWith(".class")){
			        		countFile++; 
			        	}
		            }
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else {
	        File f = new File(path); 
	        File[] files = f.listFiles(); 
	     
	        if (files != null) { 
		        for (int i = 0; i < files.length; i++) { 
		        	File file = files[i]; 
		        	if(file.getName().endsWith(".class")){
		        		countFile++; 
		        	}
		            if (file.isDirectory()) {    
		            	countClassFile(file.getAbsolutePath());  
		            } 
		        } 
	        }
    	}
        return countFile;
    } 

    @Override
    public void create() {
        super.create();
        // This is not necessary; the dialog will become bigger as the text grows but at the same time,
        // the user will be able to see all (or at least more) of the error message at once
        //getShell ().setSize (300, 300);
        setTitle(title);
        setMessage("Groovy version : " + GroovySystem.getVersion());
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