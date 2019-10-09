package org.orbisgis.ui.editors.groovy;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.orbisgis.core.CoreActivator;
import org.osgi.framework.BundleContext;

public class GroovyActivator extends AbstractUIPlugin {

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        CoreActivator.getInstance().getCoreWorkspace().extend(new GroovyWorkspaceExtension());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }
}
