package org.orbisgis.ui.editors.groovy;

import org.orbisgis.core.workspace.ICoreWorkspaceExtension;

import java.util.ArrayList;
import java.util.List;

public class GroovyWorkspaceExtension implements ICoreWorkspaceExtension {

    public static final String GROOVY_ROOT = ROOT + "Groovy";

    @Override
    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        paths.add(GROOVY_ROOT);
        return paths;
    }
}
