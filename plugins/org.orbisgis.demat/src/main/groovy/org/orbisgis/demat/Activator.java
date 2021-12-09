package org.orbisgis.demat;

import java.util.Hashtable;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.orbisgis.core.service.definition.GroovyGrab;
import org.orbisgis.demat.service.implementation.GroovyGrabImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin implements BundleActivator, IStartup{

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

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		
	}

}
