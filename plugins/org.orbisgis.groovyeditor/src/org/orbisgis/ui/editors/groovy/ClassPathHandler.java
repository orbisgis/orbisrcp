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
	
	static URL url;
	static List<URL> urls = new ArrayList<URL>();

	public static void listClassPaths() {
		/*
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
        */     
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
        /*
        List<URL> urls = new ArrayList<URL>();
        if (url != null) {
	        urls.add( url );
			System.out.println("urls : " + urls);
	    }
	    */
        String urlsToString = urls.toString().replace("[","").replace("]","").replace(", ", "\n").replace("file:", "");
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "", urlsToString);
        dialog.open();
    }
	

	public static void addClasspathDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		if (fc.showDialog(null, "Add") == JFileChooser.APPROVE_OPTION) {
			try {
				//url = fc.getSelectedFile().toURI().toURL();
				urls.add(fc.getSelectedFile().toURI().toURL());
				System.out.println("urls in addClasspathDir : " + urls);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	static URL[] getExtraJarUrls() throws MalformedURLException{
		URL[] result;
		if (urls != null) {
			System.out.println("urls : " + urls);
	        result = urls.toArray( new URL[urls.size()] );
	    }
	    else{
	        result = new URL[0];
	    }
	    return result;
	}

}