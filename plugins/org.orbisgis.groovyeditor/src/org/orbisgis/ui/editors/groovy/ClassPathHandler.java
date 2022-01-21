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
package org.orbisgis.ui.editors.groovy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.ui.editors.groovy.ui.ScrollableDialog;

/**
 * Methods to handle the classPaths that are taken into account in the groovyShell runtime.
 *
 * @author Adrien Bessy, CNRS
 */
public class ClassPathHandler{
	
	static List<URL> urls = new ArrayList<URL>();

	/**
	 * List the classpaths that are used by the groovy shell.
	 *
	 */
	public static void showClassPaths() {
        //String urlsToString = urls.toString().replace("[","").replace("]","").replace(", ", "\n").replace("file:", "");
		URLClassLoader classLoader = GroovyJob.getURLClassLoader();
		URL[] GroovyUrls = null;
		if (classLoader == null) {
			GroovyUrls = GroovyJob.getUrls();
		}
		else {
			GroovyUrls = classLoader.getURLs();
		}
		System.out.println("GroovyUrls : " + GroovyUrls);
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "",GroovyUrls);
        dialog.open();
    }
	
	/**
	 * Add a classpath from a directory choosen by an user.
	 *
	 */
	public static void addClasspathDir(GroovyEditor groovyEditor) {
		Display display = Display.getCurrent();
	    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    shell.open();
	    DirectoryDialog dialog = new DirectoryDialog(shell);
	    String result = dialog.open();
		try {
			GroovyJob job = groovyEditor.getRunningJob();
			if(job!=null) {
				job.setURls(urls);
			}
			urls.add(new File(result).toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch())
	            display.sleep();
	    }
	    display.dispose();
	}
	
	/**
	 * Add a jar to classPath.
	 *
	 */
	public static void addJarToClassPath() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FileDialog dialog = new FileDialog( shell, SWT.SINGLE );
		String[] filterExt = { "*.jar" };
		dialog.setFilterExtensions(filterExt);
	    String result = dialog.open();
		try {
			urls.add(new File(result).toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the list of classPath urls in URL[].
	 *
	 */
	static URL[] getUrlsInArray() throws MalformedURLException{
		URL[] result;
		if (urls != null) {
	        result = urls.toArray( new URL[0] );
	    }
	    else{
	        result = new URL[0];
	    }
	    return result;
	}

}