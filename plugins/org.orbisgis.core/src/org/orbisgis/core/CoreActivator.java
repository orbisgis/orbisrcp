package org.orbisgis.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.orbisgis.core.workspace.CoreWorkspace;
import org.orbisgis.core.workspace.ICoreWorkspace;
import org.osgi.framework.BundleContext;

/**
 * Plugin activator which provides access to eclipse resources.
 */
public class CoreActivator extends AbstractUIPlugin {

    private static CoreActivator instance;
    
    private ICoreWorkspace coreWorkspace;

    public static CoreActivator getInstance()
    {
        return instance;
    }

    public final ICoreWorkspace getCoreWorkspace(){
        return coreWorkspace;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
        this.coreWorkspace = new CoreWorkspace();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        this.coreWorkspace = null;
        instance = null;
        super.stop(context);
    }
}
