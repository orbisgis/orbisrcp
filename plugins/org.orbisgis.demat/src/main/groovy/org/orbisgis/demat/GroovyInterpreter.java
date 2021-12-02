package org.orbisgis.demat;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

import groovy.transform.ThreadInterrupt;
import groovy.util.GroovyScriptEngine;

public class GroovyInterpreter {
	
	public GroovyInterpreter(String name, String script){
	Binding binding = new Binding();
	CompilerConfiguration configuratorConfig = new CompilerConfiguration();
	configuratorConfig.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
	GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, configuratorConfig);
	GroovyScriptEngine engine = null;
	script = "IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport()";
	name = "";
	GroovyRunnable run = new GroovyRunnable(engine, shell, name, script, binding);
	Thread t = new Thread(run);
	t.start();
	}
	
	public class GroovyRunnable implements Runnable{
		
		private Object result = null;
		private GroovyShell shell;
		private String script;
		private GroovyScriptEngine engine;
		private Binding binding;
		private String name;
		private int status;

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
				//status = IStatus.OK;
			} catch (MissingPropertyException e){
				//String property = e.getProperty();
				//LOGGER.error("Error while execution the Groovy script.", e);
				//status = IStatus.ERROR;
			} catch(Exception e){
				//LOGGER.error("Error while executing the groovy script.", e);
				//status = IStatus.ERROR;
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
