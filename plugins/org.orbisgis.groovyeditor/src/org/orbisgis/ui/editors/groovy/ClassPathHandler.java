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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.ui.ScrollableDialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Methods to handle the urls that are taken into account as classPaths in the groovyShell runtime.
 *
 * @author Adrien Bessy, CNRS
 */
public class ClassPathHandler{

	private static final Logger LOGGER = new Logger(ClassPathHandler.class);
	private static HashSet<URL> urls = new HashSet<>();

	/**
	 * Open a dialog showing the list of urls.
	 *
	 */
	public static void showClassPaths() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", urls);
        dialog.open();
    }
	
	/**
	 * Add a directory path, choosen by the user, to the list of urls.
	 *
	 */
	public static void addDirPathToPathList() {
	    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    DirectoryDialog dialog = new DirectoryDialog(shell);
	    String result = dialog.open();
		addPathToUrlList(result, false);
	}
	
	/**
	 * Add a jar path, choosen by the user, to the list of urls.
	 *
	 */
	public static void addJarPathToPathList() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FileDialog dialog = new FileDialog( shell, SWT.SINGLE );
		String[] filterExt = { "*.jar" };
		dialog.setFilterExtensions(filterExt);
	    String result = dialog.open();
		addPathToUrlList(result, true);
	}

	/**
	 * Add a path to the list of urls.
	 * @param result the file name of the file choosen by the user
	 * @param jar true if it is a jar file
	 */
	public static void addPathToUrlList(String result, boolean jar) {
		try {
			if (result != null) {
				if(jar) {
					result = result + "!/";
				}
				else{
					result = "///d:" + result;
				}
				urls.add(new File(result).toURI().toURL());
			}
		} catch (MalformedURLException e) {
			LOGGER.error("MalformedURLException", e);
		}
	}

	/**
	 * Get Urls list.
	 * @return A Set of URLs
	 *
	 */
	public static HashSet<URL> getUrls(){
		return urls;
	}
	
	/**
	 * Transform the ArrayList of urls to URL[] and retrieve it.
	 * @return A list of URLs
	 *
	 */
	public static URL[] getUrlsInArray() {
		URL[] result;
		if (urls != null) {
	        result = urls.toArray( new URL[0] );
	    }
	    else{
	        result = new URL[0];
	    }
	    return result;
	}
	
	/**
	 * Delete one URL in the urls list.
	 * @param index the index of the urls list to remove
	 *
	 */
	public static void deleteOneURL(int index) throws MalformedURLException{
		if (index <= urls.size()) {
	        urls.remove(index);
	    }
	}

	/**
	 * This method check the /.groovy folder to load all jars and expose them
	 * to the groovy classloader
	 */
	public static void initGroovyGrabFolder(){
		File grabFolder = new File(System.getProperty("user.home") + File.separator + "/.groovy");
		if(grabFolder.exists()&& grabFolder.isDirectory()){
			try (Stream<Path> walk = Files.walk(grabFolder.toPath())) {
				HashSet<URL> groovyJars = new HashSet<>();
					walk
							.filter(p -> !Files.isDirectory(p))   // not a directory
							.forEach(p -> {
								String name = p.getFileName().toString();
								if (name.endsWith("jar")) {
									try {
										groovyJars.add(p.toUri().toURL());
									} catch (MalformedURLException e) {
										//eat
									}
								}
							});

				if(urls!=null){
					groovyJars.addAll(urls);
					urls = groovyJars;
				}


			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}