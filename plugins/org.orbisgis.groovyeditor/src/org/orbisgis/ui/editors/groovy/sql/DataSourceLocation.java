package org.orbisgis.ui.editors.groovy.sql;

import org.jkiss.dbeaver.model.DBPDataSourceFolder;
import org.orbisgis.datamanagerapi.datasource.IDataSourceLocation;
import java.io.File;

public class DataSourceLocation implements IDataSourceLocation {

    private DBPDataSourceFolder folder;

    public DataSourceLocation(DBPDataSourceFolder folder){
        this.folder = folder;
    }

    @Override
    public <T> T asType(Class<T> type) {
        if(type.isAssignableFrom(String.class)){
            return (T) folder.getFolderPath();
        }
        else if(type.isAssignableFrom(File.class)){
            return (T) new File(folder.getFolderPath());
        }
        else {
            return null;
        }
    }
}
