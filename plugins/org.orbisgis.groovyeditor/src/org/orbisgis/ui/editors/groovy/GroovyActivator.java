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

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.orbisgis.core.CoreActivator;
import org.orbisgis.core.service.definition.GroovyGrab;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class GroovyActivator extends AbstractUIPlugin implements BundleActivator, ServiceListener{
	
	private BundleContext context;
	private ServiceReference serviceReference;
	
	private ServiceTracker groovyGrabTracker;

    @Override
    public void start(BundleContext context) throws Exception {
    	this.context = context;
    	try {
            context.addServiceListener(
              this, "(objectclass=" + GroovyGrab.class.getName() + ")");
            
            groovyGrabTracker = new ServiceTracker(context, GroovyGrab.class.getName(), null);
            groovyGrabTracker.open();
            GroovyGrab groovyGrab = (GroovyGrab) groovyGrabTracker.getService();
            
            if (groovyGrab == null) {
    			System.out.println("********"
    					+ "\n\n"
    					+ "groovyGrab service unavailable on GroovyActivator start"
    					+ "\n\n"
    					+ "*****************");
    		} else {
    			System.out.println("********"
    					+ "\n\n"
    					+ "groovyGrab.getResolverName() : " + groovyGrab.getResolverName()
    					+"\n\n"
    					+ "*******************");
    		}
        			
        } catch (InvalidSyntaxException ise) {
            ise.printStackTrace();
        }
    	System.out.println("*******************************************"
    			+ "\n\n\n"
    			+ "in start method in GroovyActivator class"
    			+ "*********************************************************"
    			+ "\n\n\n"
    			+ "****************************************************");
        super.start(context);
        CoreActivator.getInstance().getCoreWorkspace().extend(new GroovyWorkspaceExtension());
        
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    	 if(serviceReference != null) {
             context.ungetService(serviceReference);
         }
        super.stop(context);
    }
    
    @Override
	public void serviceChanged(ServiceEvent serviceEvent) {
        int type = serviceEvent.getType();
        switch (type){
            case(ServiceEvent.REGISTERED):
                System.out.println("Notification of service registered.");
                serviceReference = serviceEvent.getServiceReference();
                System.out.println("\n\n********\n serviceReference : " + serviceReference + "\n\n\n********");
                GroovyGrab service = (GroovyGrab)(context.getService(serviceReference));
                System.out.println("\n\n********\n service.getResolverName() : " + service.getResolverName() + "\n\n\n********");
                break;
            case(ServiceEvent.UNREGISTERING):
                System.out.println("Notification of service unregistered.");
                context.ungetService(serviceEvent.getServiceReference());
                break;
            default:
                break;
        }
    }

    
}
