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
