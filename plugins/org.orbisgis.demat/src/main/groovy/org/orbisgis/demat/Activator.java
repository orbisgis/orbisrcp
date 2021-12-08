package org.orbisgis.demat;

import java.util.Hashtable;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.orbisgis.core.service.definition.GroovyGrab;
import org.orbisgis.demat.service.implementation.GroovyGrabImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin implements BundleActivator{

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("********"
				+ "\n\n"
				+ "start method in org.orbisgis.demat.Activator"
				+ "\n\n"
				+ "*****************");
		context.registerService(GroovyGrab.class, new GroovyGrabImpl(), new Hashtable<String, String>());
		System.out.println("********"
				+ "\n\n"
				+ "context variable in org.orbisgis.demat.Activator : " + context
				+ "\n\n"
				+ "*****************");
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/*
	public class GroovyGrabImpl implements GroovyGrab{
		
		String resolverName = "ossrh";
	    String resolverRoot = "https://oss.sonatype.org/content/repositories/snapshots/";
	    String grabGroup = "org.orbisgis";
	    String grabModule = "demat";
	    String grabVersion = "0.0.7-SNAPSHOT";

		@Override
		public String getResolverName() {
			return this.resolverName;
		}

		@Override
		public String getResolverRoot() {
			return this.resolverRoot;
		}

		@Override
		public String getGrabGroup() {
			return this.grabGroup;
		}

		@Override
		public String getGrabModule() {
			return this.grabModule;
		}

		@Override
		public String getGrabVersion() {
			return this.grabVersion;
		}
		
	}
	*/
	
	

}
