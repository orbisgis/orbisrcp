package org.orbisgis.ui.editors.groovy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.RootPaneContainer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.ui.editors.groovy.ui.ScrollableDialog;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;


public class ClassPathHandler{
	
	static URL file;

	public static void listClassPaths() {
        String classpath = System.getProperty("java.class.path");
        String[] entries = classpath.split(File.pathSeparator);
        URL[] result = new URL[entries.length];
        for(int i = 0; i < entries.length; i++) {
            try {
                result[i] = Paths.get(entries[i]).toAbsolutePath().toUri().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }        
        
        //List<URL> urls = new ArrayList<>();
        //GroovyShell gs = GroovyJob.getShell();
        //GroovyClassLoader cl = gs.getClassLoader();
        /*
        while (cl instanceof URLClassLoader) {
        	for (URL u : ((URLClassLoader) cl).getURLs()) {
        		urls.add(u);
        	}
        }
        */
        List<URL> urls = new ArrayList<URL>();
        if (file != null) {
	        try {
				urls.add( file.toURI().toURL() );
				System.out.println("urls : " + urls);
			} catch (MalformedURLException | URISyntaxException e) {
				e.printStackTrace();
			}
	    }

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        //ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "", Arrays.toString(result));
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "", urls.toString());
        dialog.open();
    }
	

	public static void addClasspathDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		GroovyShell gs = GroovyJob.getShell();
		if (fc.showDialog(null, "Add") == JFileChooser.APPROVE_OPTION) {
			try {
				file = fc.getSelectedFile().toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	static URL[] getExtraJarUrls() throws MalformedURLException{
		URL[] result;
		if (file != null) {
	        List<URL> urls = new ArrayList<URL>();
	        try {
				urls.add( file.toURI().toURL() );
				System.out.println("urls : " + urls);
			} catch (MalformedURLException | URISyntaxException e) {
				e.printStackTrace();
			}
	        result = urls.toArray( new URL[urls.size()] );
	    }
	    else{
	        result = new URL[0];
	    }
	    return result;
	}

}