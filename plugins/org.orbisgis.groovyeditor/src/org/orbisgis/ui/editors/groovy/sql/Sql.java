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
import groovy.lang.GString;
import groovy.sql.GroovyResultSetProxy;
import groovy.sql.GroovyRowResult;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.groovy.sql.extensions.SqlExtensions;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.orbisgis.core.logger.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO implements missing methods according to groovy.Sql

public abstract class Sql implements ISql{

    private static final Logger LOGGER = new Logger(Sql.class);

    protected abstract boolean executeQuery(String query) throws DBCException;
    protected abstract JDBCResultSet executeQueryWithRs(String query) throws DBCException;

    private List<GroovyRowResult> asList(String sql, JDBCResultSet rs) throws SQLException, DBCException {
        return this.asList(sql, rs, null);
    }

    private List<GroovyRowResult> asList(String sql, JDBCResultSet rs,
                                         @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        return this.asList(sql, rs, 0, 0, metaClosure);
    }

    private List<GroovyRowResult> asList(String sql, JDBCResultSet rs, int offset, int maxRows,
                                         @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        ArrayList results = new ArrayList();

        ArrayList list;
        try {
            if (metaClosure != null) {
                metaClosure.call(rs.getMetaData());
            }

            boolean cursorAtRow = moveCursor(rs, offset);
            if (!cursorAtRow) {
                return null;
            }

            int i = 0;

            while((maxRows <= 0 || i++ < maxRows) && rs.next()) {
                results.add(SqlExtensions.toRowResult(rs));
            }

            list = results;
        } catch (SQLException e) {
            LOGGER.warn("Failed to retrieve row from ResultSet for: " + sql + " because: " + e.getMessage());
            throw e;
        }

        return list;
    }

    private boolean moveCursor(JDBCResultSet rs, int offset) throws SQLException, DBCException {
        if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY){
            while(rs.getRow()<offset && rs.nextRow()){}
            if(rs.getRow() == offset){
                return true;
            }
        }
        else {
            return rs.moveTo(offset);
        }
        return false;
    }

    @Override
    public void eachRow(String sql,
                        @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException {
        this.eachRow(sql, (Closure)null, closure);
    }

    @Override
    public void eachRow(String sql, int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException {
        this.eachRow(sql, (Closure)null, offset, maxRows, closure);
    }

    @Override
    public void eachRow(String sql,
                        @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException {
        this.eachRow(sql, metaClosure, 0, 0, rowClosure);
    }

    @Override
    public void eachRow(String sql,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException {
        JDBCResultSet rs = executeQueryWithRs(sql);
        if (metaClosure != null) {
            metaClosure.call(rs.getMeta());
        }

        if (moveCursor(rs, offset)) {
            int i = 0;
            while((maxRows <= 0 || i++ < maxRows) && rs.nextRow()) {
                rowClosure.call(new GroovyResultSetProxy(rs).getImpl());
            }
        }
        else{
            LOGGER.error("Unable to move cursor to offset.");
        }

    }

    @Override
    public void eachRow(String sql, List<Object> params,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, Map map,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(Map map, String sql,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, List<Object> params,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, Map params,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(Map params, String sql,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, List<Object> params,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, Map params,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(Map params, String sql,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, List<Object> params, int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(String sql, Map params, int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }

    @Override
    public void eachRow(Map params, String sql, int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'eachRow'");
        throw e;
    }


    @Override
    public void eachRow(GString gstring,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException {
        this.eachRow(gstring.toString(), metaClosure, rowClosure);
    }

    @Override
    public void eachRow(GString gstring,
                        @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                        int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException {
        this.eachRow(gstring.toString(), metaClosure, offset, maxRows, rowClosure);
    }

    @Override
    public void eachRow(GString gstring, int offset, int maxRows,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException {
        this.eachRow(gstring.toString(), offset, maxRows, closure);
    }

    @Override
    public void eachRow(GString gstring,
                        @ClosureParams(value = SimpleType.class,options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException {
        this.eachRow(gstring, null, closure);
    }


    @Override
    public List<GroovyRowResult> rows(String sql) throws SQLException, DBCException {
        return this.rows(sql, 0, 0, null);
    }

    @Override
    public List<GroovyRowResult> rows(String sql, int offset, int maxRows) throws SQLException, DBCException {
        return this.rows(sql, offset, maxRows, null);
    }

    @Override
    public List<GroovyRowResult> rows(String sql,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        return this.rows(sql, 0, 0, metaClosure);
    }

    @Override
    public List<GroovyRowResult> rows(String sql, int offset, int maxRows,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException, DBCException {
        JDBCResultSet rs = executeQueryWithRs(sql);
        return this.asList(sql, rs, offset, maxRows, metaClosure);
    }

    @Override
    public List<GroovyRowResult> rows(String sql, List<Object> params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(Map params, String sql) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, List<Object> params, int offset, int maxRows) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, Map params, int offset, int maxRows) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(Map params, String sql, int offset, int maxRows) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, Object[] params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, Object[] params, int offset, int maxRows) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, List<Object> params,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, Map params,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(Map params, String sql,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, List<Object> params, int offset, int maxRows,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(String sql, Map params, int offset, int maxRows,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(Map params, String sql, int offset, int maxRows,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'rows'");
        throw e;
    }

    @Override
    public List<GroovyRowResult> rows(GString sql, int offset, int maxRows) throws SQLException, DBCException {
        return this.rows(sql, offset, maxRows, null);
    }

    @Override
    public List<GroovyRowResult> rows(GString gstring) throws SQLException, DBCException {
        return this.rows(gstring, null);
    }

    @Override
    public List<GroovyRowResult> rows(GString gstring,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        return this.rows(gstring.toString(), metaClosure);
    }

    @Override
    public List<GroovyRowResult> rows(GString gstring, int offset, int maxRows,
                                      @ClosureParams(value = SimpleType.class,options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException {
        return this.rows(gstring.toString(), offset, maxRows, metaClosure);
    }



    @Override
    public GroovyRowResult firstRow(String sql) throws SQLException, DBCException {
        List rows = this.rows(sql, 0, 1, null);
        return rows.isEmpty() ? null : (GroovyRowResult)rows.get(0);
    }

    @Override
    public GroovyRowResult firstRow(GString gstring) throws SQLException, DBCException {
        return this.firstRow(gstring.toString());
    }

    @Override
    public GroovyRowResult firstRow(String sql, List<Object> params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'firstRow'");
        throw e;
    }

    @Override
    public GroovyRowResult firstRow(Map params, String sql) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'firstRow'");
        throw e;
    }

    @Override
    public GroovyRowResult firstRow(String sql, Object[] params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'firstRow'");
        throw e;
    }



    @Override
    public boolean execute(String sql) throws SQLException, DBCException {
        return executeQuery(sql);
    }

    @Override
    public void execute(String sql, Closure processResults) throws SQLException, DBCException {
        JDBCResultSet rs = executeQueryWithRs(sql);
        List<GroovyRowResult> rowResult = rs == null ? null : this.asList(sql, rs);
        processResults.call(rowResult);
    }

    @Override
    public boolean execute(String sql, List<Object> params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public void execute(String sql, List<Object> params, Closure processResults) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public boolean execute(Map params, String sql) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public void execute(Map params, String sql, Closure processResults) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public boolean execute(String sql, Object[] params) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public void execute(String sql, Object[] params, Closure processResults) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'execute'");
        throw e;
    }

    @Override
    public boolean execute(GString gstring) throws SQLException, DBCException {
        return this.execute(gstring.toString());
    }

    @Override
    public void execute(GString gstring, Closure processResults) throws SQLException, DBCException {
        this.execute(gstring.toString(), processResults);
    }

    @Override
    public int call(String sql) throws Exception {
        return this.call(sql, new ArrayList<>());
    }

    @Override
    public int call(GString gstring) throws Exception {
        return this.call(gstring.toString());
    }

    @Override
    public int call(String sql, List<Object> params) throws Exception {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }

    @Override
    public int call(String sql, Object[] params) throws Exception {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }

    @Override
    public void call(String sql, List<Object> params, Closure closure) throws Exception {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }

    @Override
    public void call(GString gstring, Closure closure) throws Exception {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }

    @Override
    public int[] withBatch(Closure closure) throws SQLException, DBCException {
        return this.withBatch(0, closure);
    }

    @Override
    public int[] withBatch(int batchSize, Closure closure) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }

    @Override
    public int[] withBatch(String sql, Closure closure) throws SQLException, DBCException {
        return this.withBatch(0, sql, closure);
    }

    @Override
    public int[] withBatch(int batchSize, String sql, Closure closure) throws SQLException, DBCException {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'call'");
        throw e;
    }
}
