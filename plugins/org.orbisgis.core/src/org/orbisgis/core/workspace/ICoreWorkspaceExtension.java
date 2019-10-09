package org.orbisgis.core.workspace;

import java.util.List;

public interface ICoreWorkspaceExtension {

    String SEPARATOR = "/";
    String ROOT = SEPARATOR;

    List<String> getPaths();
}
