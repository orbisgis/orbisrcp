package org.orbisgis.ui.editors.groovy;

import org.orbisgis.core.workspace.ICoreWorkspaceExtension;

import java.util.ArrayList;
import java.util.List;

public class GroovyWorkspaceExtension implements ICoreWorkspaceExtension {

    public static final String GROOVY_ROOT = ROOT + "groovy";
    public static final String GROOVY_SCRIPT = GROOVY_ROOT + SEPARATOR + "script";
    public static final String GROOVY_DATA = GROOVY_ROOT + SEPARATOR + "data";

    @Override
    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        paths.add(GROOVY_ROOT);
        //paths.add(GROOVY_DATA);
        //paths.add(GROOVY_SCRIPT);
        return paths;
    }
}
