package org.orbisgis.core.workspace;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.orbisgis.core.logger.Logger;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class CoreWorkspace implements ICoreWorkspace {

    private static final Logger LOGGER = new Logger(CoreWorkspace.class);

    private static final IWorkspace eclipseWorkspace = ResourcesPlugin.getWorkspace();

    @Override
    public void extend(ICoreWorkspaceExtension extension){
        for(String path : extension.getPaths()){
            if(!path.contains(".")){
                try {
                    IFolder folder = getFolder(path);
                    if(folder == null){
                        LOGGER.error("The folder path '" + path + "' is invalid");
                        return;
                    }
                    if(!folder.exists()) {
                        folder.create(true, true, null);
                    }
                } catch (CoreException e) {
                    LOGGER.error("Unable to create the folder '"+path+"'.", e);
                }
            }
            else{
                try {
                    IFile file = getFile(path);
                    if(file == null){
                        LOGGER.error("The file path '" + path + "' is invalid");
                        return;
                    }
                    if(!file.exists()) {
                        file.create(new ByteArrayInputStream(new byte[]{}), true, null);
                    }
                } catch (CoreException e) {
                    LOGGER.error("Unable to create the file '"+path+"'.", e);
                }
            }
        }
    }

    @Override
    public IProject getOpenProject(){
        if(eclipseWorkspace == null){
            LOGGER.error("No Eclipse workspace found.");
            return null;
        }
        if(eclipseWorkspace.getRoot() == null){
            LOGGER.error("No Eclipse workspace root found.");
            return null;
        }
        if(eclipseWorkspace.getRoot().getProjects() == null){
            LOGGER.error("No Eclipse workspace projects.");
            return null;
        }
        if(eclipseWorkspace.getRoot().getProjects().length == 0){
            LOGGER.error("No Eclipse workspace projects found.");
            return null;
        }
        for(IProject project : eclipseWorkspace.getRoot().getProjects()){
            if(project.isOpen()){
                return project;
            }
        }
        LOGGER.error("No open project found.");
        return null;
    }

    @Override
    public IFolder getFolder(String path){
        IProject project = getOpenProject();
        if(project != null){
            return project.getFolder(path);
        }
        LOGGER.error("No open project found");
        return null;
    }

    @Override
    public IFile getFile(String path){
        IProject project = getOpenProject();
        if(project != null){
            project.getFile(path);
        }
        LOGGER.error("No open project found");
        return null;
    }
}
