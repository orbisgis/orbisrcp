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
package org.orbisgis.ui.editors.groovy.sql;

import org.orbisgis.datamanagerapi.dataset.DataBaseType;
import org.orbisgis.datamanagerapi.dataset.ITableLocation;

public class TableLocation implements ITableLocation {

    private String dataSource;
    private String catalog;
    private String schema;
    private String table;

    public TableLocation(String dataSource, String table){
        this.dataSource = dataSource;
        this.catalog = null;
        this.schema = null;
        this.table = table;
    }

    public TableLocation(String dataSource, String catalog, String table){
        this.dataSource = dataSource;
        this.catalog = catalog;
        this.schema = null;
        this.table = table;
    }

    public TableLocation(String dataSource, String catalog, String schema, String table){
        this.dataSource = dataSource;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public String getDataSource() {
        return dataSource;
    }

    @Override
    public String toString(DataBaseType type) {
        String str = "";
        if(catalog != null){
            str += catalog + ".";
        }
        if(schema != null){
            str += schema + ".";
        }
        return str + table;
    }

    @Override
    public String toString() {
        String str = "";
        if(catalog != null){
            str += catalog + ".";
        }
        if(schema != null){
            str += schema + ".";
        }
        return str + table;
    }
}
