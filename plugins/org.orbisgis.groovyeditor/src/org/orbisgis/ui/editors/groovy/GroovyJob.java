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
 */
package org.orbisgis.ui.editors.groovy;

import java.io.*;
import java.net.URLClassLoader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.GroovyOutputConsole.GroovyConsoleContent;
import org.orbisgis.ui.editors.groovy.logger.GroovyLogger;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.transform.ThreadInterrupt;

public class GroovyJob extends Job {

    private static final Logger LOGGER = new Logger(GroovyJob.class);

    private String script;
    private GroovyShell shell = null;
    private Binding binding;
    private String name;
    private Thread t;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    PrintWriter outputPrintWriter = null;

    public GroovyJob(String name, String script) {
        super(name);
        this.script = script;
        this.name = name;
        binding = new Binding();
        binding.setProperty("logger", new GroovyLogger(GroovyShell.class));
        binding.setProperty("out", new StringWriter());
        
        CompilerConfiguration configuratorConfig = new CompilerConfiguration(System.getProperties());
        configuratorConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));

        File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + name + "_output.txt");
        closeOutputPrintWriter();
        createOutputPrintWriter(outputFile);

        try {
        	URLClassLoader classLoader = new URLClassLoader( ClassPathHandler.getUrlsInArray(), Thread.currentThread().getContextClassLoader() );
            //shell = new GroovyShell(Thread.currentThread().getContextClassLoader(), binding, configuratorConfig);
        	shell = new GroovyShell(classLoader, binding, configuratorConfig);
        }  catch (Exception e) {
            LOGGER.warn("Unable to create GroovyShell instead.");
        }
    }

    /**
     * Create a PrintWriter for a given file.
     *
     * @param outputFile The given file
     */
    void createOutputPrintWriter(File outputFile) {
        try {
            outputPrintWriter = new PrintWriter(new FileOutputStream(
                    outputFile,
                    true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close a PrintWriter.
     *
     */
    void closeOutputPrintWriter() {
        if (outputPrintWriter != null) {
            outputPrintWriter.close();
            outputPrintWriter = null;
        }
    }

    @Override
    protected IStatus run(IProgressMonitor iProgressMonitor) {
        GroovyRunnable run = new GroovyRunnable(shell, name, script, binding);
        t = new Thread(run);
        t.start();
        int status = IStatus.ERROR;
        try {
            t.join();
            status = run.getStatus();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to execute the Groovy script thread.", e);
        } catch (Exception e){
            LOGGER.error("Error while execution the Groovy script.", e);
        }
        Object result = run.getResult();
        String message =  "Groovy script successfully executed.";
        if(result != null){
            message = result.toString();
        }
        return new Status(IStatus.OK, GroovyJob.class.getName(), message);
    }

    @Override
    protected void canceling() {
        super.canceling();
        if(t.isAlive()) {
            t.interrupt();
        }
        closeOutputPrintWriter();
    }

    private class GroovyRunnable implements Runnable{

        private Object result = null;
        private GroovyShell shell;
        private String script;
        private Binding binding;
        private String name;
        private int status;
        private StringWriter sw = new StringWriter();
        private PrintWriter pw = new PrintWriter(sw);

        public GroovyRunnable(GroovyShell shell, String name, String script, Binding binding){
            this.shell = shell;
            this.script = script;
            this.binding = binding;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                shell.run(script,name, new String[] {});
                String outputStream = shell.getProperty("out").toString();
                GroovyConsoleContent.writeIntoConsole(outputStream, true);
                GroovyConsoleContent.writeIntoConsole("END");
                status = IStatus.OK;
                closeOutputPrintWriter();
            } catch (MissingPropertyException e){
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                GroovyConsoleContent.writeIntoConsole(sStackTrace);
                LocalDateTime now = LocalDateTime.now();
                outputPrintWriter.write("\n" + dtf.format(now) + "\n" + sStackTrace);
                GroovyConsoleContent.writeIntoConsole("BAD_END");   
                status = IStatus.ERROR;
                closeOutputPrintWriter();
            } catch(Exception e){
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                GroovyConsoleContent.writeIntoConsole(sStackTrace);
                LocalDateTime now = LocalDateTime.now();
                outputPrintWriter.write("\n" + dtf.format(now) + "\n" + sStackTrace);
                GroovyConsoleContent.writeIntoConsole("BAD_END");   
                status = IStatus.ERROR;
                closeOutputPrintWriter();
            }
        }

        public Object getResult(){
            return result;
        }

        public int getStatus(){
            return status;
        }
    }

}
