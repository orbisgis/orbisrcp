package org.orbisgis.ui.editors.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.ThreadInterrupt;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.ui.editors.groovy.logger.GroovyLogger;
import org.orbisgis.ui.editors.groovy.sql.DataSource;

import java.util.List;
import java.util.Map;

public class GroovyJob extends Job {

    private static final Logger LOGGER = new Logger(GroovyJob.class);

    private String script;
    private GroovyShell shell;
    private Thread t;

    public GroovyJob(String name, String script, Map<String, DataSource> dataSources, List<String> closedDatasources){
        super(name);
        this.script = script;
        Binding binding = new Binding();
        dataSources.forEach(binding::setProperty);
        binding.setProperty("logger", new GroovyLogger(GroovyShell.class));
        binding.setProperty(GroovyScript.CLOSED_DATA_SOURCES, closedDatasources);

        CompilerConfiguration configuratorConfig = new CompilerConfiguration();
        configuratorConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        configuratorConfig.setScriptBaseClass("org.orbisgis.ui.editors.groovy.GroovyScript");

        shell = new GroovyShell(this.getClass().getClassLoader(), binding, configuratorConfig);
    }

    @Override
    protected IStatus run(IProgressMonitor iProgressMonitor) {
        final Object[] obj = new Object[1];
        t = new Thread(() -> obj[0] = shell.evaluate(script));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to execute the Groovy script thread.", e);
        } catch (Exception e){
            LOGGER.error("Error while execution the Groovy script.", e);
        }
        String result = "";
        if(obj[0] != null) {
            LOGGER.info(obj[0].toString());
            result = obj[0].toString();
        }
        String message =  "Groovy script successfully executed";
        if(result != null){
            message += " with result '" + result + "'";
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
}
