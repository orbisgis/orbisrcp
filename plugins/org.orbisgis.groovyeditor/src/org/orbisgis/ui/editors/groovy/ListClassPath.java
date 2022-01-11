package org.orbisgis.ui.editors.groovy;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

public class ListClassPath extends TitleAreaDialog {
    Shell shell = null;

    protected ListClassPath(Shell parentShell) {
        super(parentShell);
        this.shell = parentShell;
    }

    @Override
    public Shell getShell() {
        return shell;
    }

    public static void listClassPaths() {
        System.out.println("Classloader : "
                + Thread.currentThread().getContextClassLoader());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        //System.out.println("((URLClassLoader) cl).getURLs() : " + ((URLClassLoader) cl).getURLs());
        /*
        URL[] urls = ((URLClassLoader) cl).getURLs();
        for (URL url: urls) {
            System.out.println(url.getFile());
        }
         */
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
/*
        JOptionPane pane = swing.optionPane();
        JDialog dialog = pane.createDialog(frame, 'Classpath');
        dialog.setSize(800, 600)
        dialog.resizable = true
        dialog.visible = true
 */
        // create a dialog with ok and cancel buttons and a question icon
        //MessageDialog.openConfirm(getShell(), "Confirm", "Please confirm");
    }

}
