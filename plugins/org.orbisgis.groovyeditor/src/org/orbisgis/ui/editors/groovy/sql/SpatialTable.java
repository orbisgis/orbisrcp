package org.orbisgis.ui.editors.groovy.sql;

import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.orbisdata.datamanager.api.dataset.IRaster;
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpatialTable extends Table implements ISpatialTable {

    private int firstGeometryFieldIndex = -1;

    public SpatialTable(String tableName, DBPDataSourceContainer container) {
        super(tableName, container);
    }

    public SpatialTable(String tableName, String schema, DBPDataSourceContainer container) {
        super(tableName, schema, container);
    }

    public SpatialTable(String tableName, String catalog, String schema, DBPDataSourceContainer container) {
        super(tableName, catalog, schema, container);
    }

    @Override
    public Geometry getGeometry(int columnIndex) {
        try {
            Object field = getObject(columnIndex);
            if (field == null) {
                return (Geometry) field;
            } else if (field instanceof Geometry) {
                return (Geometry) field;
            } else {
                throw new SQLException("The column " + this.getMetaData().getColumnName(columnIndex) + " is not a Geometry");
            }
        } catch (SQLException e) {
            getLogger().error("Unable to get the geometry at index '" + columnIndex + "'.", e);
        }
        return null;
    }

    @Override
    public Geometry getGeometry(String columnLabel) {
        try {
            Object field = this.getObject(columnLabel);
            if (field == null) {
                return (Geometry)field;
            } else if (field instanceof Geometry) {
                return (Geometry)field;
            } else {
                throw new SQLException("The column " + columnLabel + " is not a Geometry");
            }
        } catch (SQLException e) {
            getLogger().error("Unable to get the geometry with label '" + columnLabel + "'.", e);
        }
        return null;
    }

    @Override
    public Geometry getGeometry() {
        try {
            return getGeometry(getFirstGeometryFieldIndex());
        } catch (SQLException e) {
            getLogger().error("Unable to get the geometry.", e);
        }
        return null;
    }

    @Override
    public IRaster getRaster(int i) {
        return null;
    }

    @Override
    public IRaster getRaster(String s) {
        return null;
    }

    @Override
    public IRaster getRaster() {
        return null;
    }

    @Override
    public List<String> getSpatialColumns() {
        return getGeometricColumns();
    }

    @Override
    public List<String> getRasterColumns() {
        return null;
    }

    private int getFirstGeometryFieldIndex() throws SQLException {
        if (firstGeometryFieldIndex == -1) {
            for(int idColumn = 1; idColumn <= getColumnCount(); ++idColumn) {
                if (getMetaData().getColumnTypeName(idColumn).equalsIgnoreCase("geometry")) {
                    firstGeometryFieldIndex = idColumn;
                    break;
                }
            }
        }
        return firstGeometryFieldIndex;
    }

    @Override
    public List<String> getGeometricColumns(){
        List<String> list = new ArrayList<>();
        getColumnsTypes().forEach((name, type) -> {
            if(type.equalsIgnoreCase("geometry")){
                list.add(name);
            }
        });
        return list;
    }

    @Override
    public Envelope getExtend() {
        try {
            List<String> names = getGeometricColumns();
            if(names.isEmpty()){
                getLogger().error("There is no geometric field.");
                return null;
            }
            ResultSet rs = executeQuery("SELECT ST_Extent(" + names.get(0) + ") ext FROM " + location, 1);
            if (rs.next()) {
                return ((Geometry)rs.getObject(1)).getEnvelopeInternal();
            } else {
                throw new SQLException("Unable to get the table extent it may be empty");
            }
        } catch (SQLException|DBCException e) {
            getLogger().error("Unable to get the table extend.", e);
        }
        finally {
            resultSet.close();
        }
        return null;
    }

    @Override
    public Geometry getEstimatedExtend() {
        try {
            List<String> names = getGeometricColumns();
            if(names.isEmpty()){
                getLogger().error("There is no geometric field.");
                return null;
            }

            StringBuilder query = new StringBuilder("SELECT  ESTIMATED_ENVELOPE('");
            query.append(location).append("','").append(names.get(0)).append("')");
            ResultSet rs = executeQuery(query.toString(), 1);
            if (rs.next()) {
                Geometry result = (Geometry)rs.getObject(1);
                if (result != null) {
                    return result;
                }

                query = new StringBuilder("SELECT  ENVELOPE(");
                query.append(names.get(0)).append(") FROM ").append(location);
                rs = executeQuery(query.toString(), 1);
                if (rs.next()) {
                    result = (Geometry)rs.getObject(1);
                    if (result != null) {
                        return result;
                    }
                }
            }
        } catch (SQLException|DBCException e) {
            getLogger().error("Unable to get the table estimated extend.", e);
        }
        return null;
    }

    @Override
    public int getSrid() {
        return getGeometry().getSRID();
    }

    //TODO
    @Override
    public Map<String, String> getGeometryTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSpatial(){
        return true;
    }
}
