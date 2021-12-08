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

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.core.service.definition.GroovyGrab;
import org.orbisgis.ui.editors.groovy.logger.GroovyLogger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.transform.ThreadInterrupt;
import groovy.util.GroovyScriptEngine;

public class GroovyJob extends Job {

    private static final Logger LOGGER = new Logger(GroovyJob.class);

    private String script;
    private GroovyShell shell = null;
    private GroovyScriptEngine engine;
    private Binding binding;
    private String name;
    private Thread t;

    public GroovyJob(String name, String script){
        super(name);
        this.script = script;
        this.name = name;

        binding = new Binding();
        binding.setProperty("logger", new GroovyLogger(GroovyShell.class));

        CompilerConfiguration configuratorConfig = new CompilerConfiguration();
        configuratorConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
       /*
        try {
            String root = CoreActivator.getInstance().getCoreWorkspace().getFolder("Groovy").getLocation().toString() + File.separator;
            System.out.println("root : " + root);
            engine = new GroovyScriptEngine(root);
            engine.setConfig(configuratorConfig);
        } catch (IOException e) {
            LOGGER.warn("Unable to create the groovy engine, use GroovyShell instead.");
            shell = new GroovyShell(this.getClass().getClassLoader(), binding, configuratorConfig);
        }
       */
        
        //String root = CoreActivator.getInstance().getCoreWorkspace().getFolder("Groovy").getLocation().toString() + File.separator + name;

        shell = new GroovyShell(this.getClass().getClassLoader(), binding, configuratorConfig);
        Bundle plugin = Platform.getBundle("org.orbisgis.demat");
        System.out.println("plugin : " + plugin);
        //shell.evaluate("groovy.grape.Grape.grab(group:'org.orbisgis',module:'demat', version:'0.0.7-SNAPSHOT')");

    }

    @Override
    protected IStatus run(IProgressMonitor iProgressMonitor) {
        GroovyRunnable run = new GroovyRunnable(engine, shell, name, script, binding);
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
        if(status == IStatus.OK) {
            LOGGER.info(message);
        }
        return new Status(IStatus.OK, GroovyJob.class.getName(), message);
    }

    @Override
    protected void canceling() {
        super.canceling();
        if(t.isAlive()) {
            t.interrupt();
        }
    }

    private class GroovyRunnable implements Runnable{

        private Object result = null;
        private GroovyShell shell;
        private String script;
        private GroovyScriptEngine engine;
        private Binding binding;
        private String name;
        private int status;
        private BundleContext context;

        public GroovyRunnable(GroovyScriptEngine engine, GroovyShell shell, String name, String script, Binding binding){
            this.shell = shell;
            this.script = script;
            this.engine = engine;
            this.binding = binding;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                if(engine != null){
                    result = engine.run(name, binding);
                }
                else {
                	//Collection<?> references = this.context.getServiceReferences(this.getClass(), null);
                	BundleContext ctx = FrameworkUtil.getBundle(getClass()).getBundleContext();
                	ServiceReference<?>[] refs = ctx.getServiceReferences(GroovyGrab.class.getName(), null);                 
                	System.out.println("\n\n********\n references !!! : " + refs + "\n\n********");
                	GroovyGrab provider = null;
                	for (ServiceReference ref : refs) {
            	        provider = (GroovyGrab) ctx.getService(ref);
            	        System.out.println("\n\n********\n provider !!! : " + provider + "\n\n********");
            	        shell.evaluate("groovy.grape.Grape.addResolver(name:'" + provider.getResolverName() + "',root:'" + provider.getResolverRoot() + "')");
            	        shell.evaluate("groovy.grape.Grape.grab(group:'" + provider.getGrabGroup() + "',module:'" + provider.getGrabModule() + "', version:'" + provider.getGrabVersion() + "')");
                	}
                    System.out.println("\n\n********\n provider.getResolverName() in groovyJob !!! : " + provider.getResolverName() + "\n\n\n********");
                    
                    for(String s : script.split("\n")) {
                    	/*
                    	System.out.println("string before if : " + s);
                    	if(s.contains("@Grab") && !s.contains("//") && !s.contains("/*")) {
                    		String[] parts = s.split("'");
                    		s = "groovy.grape.Grape.grab(group:'" + parts[1] + "',module:'" + parts[3] + "', version:'" + parts[5] + "')" ;
                    		System.out.println("string AFTER Grab Change : " + s);
                    	}
                    	*/
                    	shell.evaluate(s);
                    }
                }
                status = IStatus.OK;
            } catch (MissingPropertyException e){
                String property = e.getProperty();
                LOGGER.error("Error while execution the Groovy script.", e);
                status = IStatus.ERROR;
            } catch(Exception e){
                LOGGER.error("Error while executing the groovy script.", e);
                status = IStatus.ERROR;
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
