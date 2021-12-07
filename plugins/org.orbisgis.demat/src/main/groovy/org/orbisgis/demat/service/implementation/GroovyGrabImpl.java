package org.orbisgis.demat.service.implementation;

import java.util.Hashtable;

import org.orbisgis.core.service.definition.GroovyGrab;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;

@Component(service = GroovyGrab.class)
public class GroovyGrabImpl implements GroovyGrab, BundleActivator{
	
    private ServiceReference<GroovyGrab> reference;
    private ServiceRegistration<GroovyGrab> registration;
    
    String resolverName = "ossrh";
    String resolverRoot = "https://oss.sonatype.org/content/repositories/snapshots/";
    String grabGroup = "org.orbisgis";
    String grabModule = "demat";
    String grabVersion = "0.0.7-SNAPSHOT";

    @Override 
    public void start(BundleContext context) throws Exception {
        System.out.println("Registering service.");
        registration = context.registerService(GroovyGrab.class, new GroovyGrabImpl(), new Hashtable<String, String>());
        reference = registration.getReference();
    }

    @Override 
    public void stop(BundleContext context) throws Exception {
        System.out.println("Unregistering service.");
        registration.unregister();
    }
    
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
