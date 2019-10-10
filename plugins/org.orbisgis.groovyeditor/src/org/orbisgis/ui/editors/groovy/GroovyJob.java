package org.orbisgis.ui.editors.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.transform.ThreadInterrupt;
import groovy.util.GroovyScriptEngine;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.orbisgis.core.CoreActivator;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.logger.GroovyLogger;
import org.orbisgis.ui.editors.groovy.sql.DataSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.File;

public class GroovyJob extends Job {

    private static final Logger LOGGER = new Logger(GroovyJob.class);

    private String script;
    private GroovyShell shell = null;
    private GroovyScriptEngine engine;
    private Binding binding;
    private String name;
    private Thread t;
    private List<String> closedDatasources;

    public GroovyJob(String name, String script, Map<String, DataSource> dataSources, List<String> closedDatasources){
        super(name);
        this.script = script;
        this.closedDatasources = closedDatasources;
        this.name = name;

        binding = new Binding();
        dataSources.forEach(binding::setProperty);
        binding.setProperty("logger", new GroovyLogger(GroovyShell.class));

        CompilerConfiguration configuratorConfig = new CompilerConfiguration();
        configuratorConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));

        try {
            String root = new File(CoreActivator.getInstance().getCoreWorkspace().getFolder("groovy").getFolder("script").getLocationURI()).getAbsolutePath() + File.separator;
            engine = new GroovyScriptEngine(root);
            engine.setConfig(configuratorConfig);
        } catch (IOException e) {
            LOGGER.warn("Unable to create the groovy engine, use GroovyShell instead.");
            shell = new GroovyShell(this.getClass().getClassLoader(), binding, configuratorConfig);
        }
    }

    @Override
    protected IStatus run(IProgressMonitor iProgressMonitor) {
        GroovyRunnable run = new GroovyRunnable(engine, shell, name, script, binding);
        t = new Thread(run);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to execute the Groovy script thread.", e);
        } catch (Exception e){
            LOGGER.error("Error while execution the Groovy script.", e);
        }
        Object result = run.getResult();
        String message =  "Groovy script successfully executed";
        if(result != null){
            message += " with result '" + result.toString() + "'";
        }
        message += ".";
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
                    result = shell.evaluate(script);
                }
            } catch (MissingPropertyException e){
                String property = e.getProperty();
                if(closedDatasources.contains(property)){
                    LOGGER.error("The datasource '"+property+"' is closed.");
                }
                else{
                    LOGGER.error("Error while execution the Groovy script.", e);
                }
            } catch(Exception e){
                LOGGER.error("Error while executing the groovy script.", e);
            }
        }

        public Object getResult(){
            return result;
        }
    }
}
