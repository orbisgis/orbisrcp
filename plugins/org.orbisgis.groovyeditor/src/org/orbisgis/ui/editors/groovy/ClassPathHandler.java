package org.orbisgis.ui.editors.groovy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.RootPaneContainer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.orbisgis.ui.editors.groovy.ui.ScrollableDialog;


public class ClassPathHandler{

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
        System.out.println("urls : " + Arrays.toString(result));

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ScrollableDialog dialog = new ScrollableDialog(shell, "List of class paths", "", Arrays.toString(result));
        dialog.open();
    }
	

	public static void addClasspathDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		//Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (fc.showDialog(null, "Add") == JFileChooser.APPROVE_OPTION) {
			//shell.getClassLoader().addURL(fc.getSelectedFile().toURL());
		}
	
	}

}