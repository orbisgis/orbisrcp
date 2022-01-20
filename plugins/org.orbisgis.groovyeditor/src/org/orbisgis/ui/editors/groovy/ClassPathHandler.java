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
package org.orbisgis.ui.editors.groovy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.ui.editors.groovy.ui.ScrollableDialog;

public class ClassPathHandler{
	
	static List<URL> urls = new ArrayList<URL>();

	/**
	 * List the classpaths that are used by the groovy shell.
	 *
	 */
	public static void listClassPaths() {
        String urlsToString = urls.toString().replace("[","").replace("]","").replace(", ", "\n").replace("file:", "");
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "", urlsToString);
        dialog.open();
    }
	
	/**
	 * Add a classpath from a directory choosen by an user.
	 *
	 */
	public static void addClasspathDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		if (fc.showDialog(null, "Add") == JFileChooser.APPROVE_OPTION) {
			try {
				urls.add(fc.getSelectedFile().toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get the list of classPath urls in URL[].
	 *
	 */
	static URL[] getUrlsInArray() throws MalformedURLException{
		URL[] result;
		if (urls != null) {
	        result = urls.toArray( new URL[urls.size()] );
	    }
	    else{
	        result = new URL[0];
	    }
	    return result;
	}

}