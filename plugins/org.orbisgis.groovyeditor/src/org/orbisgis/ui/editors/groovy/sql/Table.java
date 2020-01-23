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

import groovy.lang.Closure;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.*;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.impl.AbstractExecutionSource;
import org.jkiss.dbeaver.model.runtime.VoidProgressMonitor;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.orbisdata.datamanager.api.dataset.*;
import org.orbisgis.orbisdata.datamanager.api.dsl.IConditionOrOptionBuilder;
import org.orbisgis.orbisdata.datamanager.api.dsl.IOptionBuilder;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Table extends AbstractTable implements IJdbcTable{

    private static final Logger LOGGER = new Logger(Table.class);
    protected ITableLocation location;
    protected DBPDataSourceContainer container;

    public Table(String tableName, DBPDataSourceContainer container){
        location = new TableLocation(container.getName(), tableName);
        this.container = container;
    }

    public Table(String schema, String tableName, DBPDataSourceContainer container){
        location = new TableLocation(container.getName(), schema, tableName);
        this.container = container;
    }

    public Table(String catalog, String schema, String tableName, DBPDataSourceContainer container){
        location = new TableLocation(container.getName(), catalog, schema, tableName);
        this.container = container;
    }

    @Override
    public Logger getLogger(){
        return LOGGER;
    }

    @Override
    public ITableLocation getTableLocation() {
        return location;
    }

    @Override
    public DataBaseType getDbType() {
        switch (container.getDriver().getId().toLowerCase()){
            case "h2 embedded":
            default:
                return DataBaseType.OTHER;
        }
    }

    @Override
    public boolean isSpatial() {
        return false;
    }

    //TODO
    @Override
    public boolean isLinked() {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public boolean isTemporary() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object asType(Class clazz) {
        if(ITable.class.isAssignableFrom(clazz)){
            return this;
        }
        else if(ISpatialTable.class.isAssignableFrom(clazz)){
            if(isSpatial()){
                return this;
            }
        }
        else if(String.class.isAssignableFrom(clazz)){
            return location.getDataSource()+":"+location;
        }
        return null;
    }

    @Override
    public ITable getTable() {
        return (ITable)asType(ITable.class);
    }

    @Override
    public ISpatialTable getSpatialTable() {
        return (ISpatialTable)asType(ISpatialTable.class);
    }

    @Override
    public List<String> getColumns() {
        try {
            return getResultSet()
                    .getMeta()
                    .getAttributes()
                    .stream()
                    .map(DBCAttributeMetaData::getLabel)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (DBCException e) {
            LOGGER.error("Unable to get the ResultSet.", e);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, String> getColumnsTypes() {
        try {
            Map<String, String> map = new HashMap<>();
            for (int i = getColumnCount(); i > 0; i--) {
                map.put(getMetaData().getColumnLabel(i), getMetaData().getColumnTypeName(i));
            }
            return map;
        }
        catch(SQLException e){
            LOGGER.error("Unable to get the columns label or type.", e);
        }
        return null;
    }

    @Override
    public String getColumnType(String columnName) {
        int index = -1;
        try {
            for (int i = getColumnCount(); i > 0; i--) {
                if(getMetaData().getColumnLabel(i).equalsIgnoreCase(columnName)){
                    index = i;
                }
            }
            return getMetaData().getColumnTypeName(index);
        }
        catch(SQLException e){
            LOGGER.error("Unable to get the column type.", e);
        }
        return null;
    }

    @Override
    public boolean hasColumn(String columnName, Class clazz) {
        String type = getColumnsTypes().get(columnName);
        return type != null && type.equalsIgnoreCase(clazz.getTypeName());
    }

    @Override
    public int getRowCount() {
        Exception e = null;
        try {
            JDBCResultSet rs = executeQuery("SELECT COUNT(*) FROM " + location, -1);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | DBCException ex) {
            e = ex;
        } finally {
            if(e != null) {
                LOGGER.error("Unable to get the row count.", e);
            }
            else{
                LOGGER.error("Unable to get the row count.");
            }
        }
        return -1;
    }

    @Override
    public Collection<String> getUniqueValues(String column) {
        List<String> list = new ArrayList<>();
        try {
            JDBCResultSet rs = executeQuery("SELECT DISTINCT " + column + " FROM " + location, -1);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        } catch (SQLException | DBCException e) {
            LOGGER.error("Unable to get the unique values.", e);
            return null;
        }
    }

    //TODO
    @Override
    public boolean save(String filePath, String encoding) {
        return false;
    }

    @Override
    public List<Object> getFirstRow() {
        List<Object> list = new ArrayList<>();
        try {
            getResultSet().nextRow();
            for (String colName : getColumns()) {
                list.add(getResultSet(1).getAttributeValue(colName));
            }
            invalidateResultSet();
        }
        catch (DBCException e){
            LOGGER.error("Error while getting the ResultSet.", e);
        }
        return list;
    }

    @Override
    public IJdbcTable columns(String... columns){
        throw new UnsupportedOperationException();
    }

    @Override
    public IJdbcTable columns(List<String> columns){
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IConditionOrOptionBuilder where(String condition) {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IOptionBuilder groupBy(String... fields) {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IOptionBuilder orderBy(Map<String, Order> orderByMap) {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IOptionBuilder orderBy(String field, Order order) {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IOptionBuilder orderBy(String field) {
        throw new UnsupportedOperationException();
    }

    //TODO
    @Override
    public IOptionBuilder limit(int limitCount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void eachRow(Closure closure){
        this.forEach(closure::call);
        invalidateResultSet();
    }

    @Override
    public IJdbcTableSummary getSummary() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JDBCResultSet getResultSet() throws DBCException {
        return getResultSet(-1);
    }

    @Override
    protected JDBCResultSet getResultSet(int maxRows) throws DBCException {
        return executeQuery("SELECT * FROM " + location, maxRows);
    }

    protected JDBCResultSet executeQuery(String query, int maxRows) throws DBCException {
        DBCExecutionContext context = DBUtils.getDefaultContext(container.getDataSource(), false);
        DBCSession session = context.openSession(new VoidProgressMonitor(), DBCExecutionPurpose.USER, "SQL Query");
        DBCExecutionSource source = new AbstractExecutionSource(null, session.getExecutionContext(), null, query);
        if(resultSet == null) {
            final DBCStatement dbcStatement = DBUtils.makeStatement(source, session, DBCStatementType.QUERY, query, 0, maxRows);
            if (dbcStatement.executeStatement()) {
                resultSet = dbcStatement.openResultSet();
            } else {
                return null;
            }
        }
        return (JDBCResultSet)resultSet;
    }
}
