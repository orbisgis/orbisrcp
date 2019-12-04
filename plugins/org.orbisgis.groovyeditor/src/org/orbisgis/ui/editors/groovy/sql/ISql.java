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
import groovy.sql.GroovyRowResult;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.jkiss.dbeaver.model.exec.DBCException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISql {

    /* *** EachRow *** */

    void eachRow(String sql,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(String sql, int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(String sql,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql, List<Object> params,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql, Map map,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(Map map, String sql,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql, List<Object> params,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql, Map params,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(Map params, String sql,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(String sql, List<Object> params,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(String sql, Map params,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(Map params, String sql,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(String sql, List<Object> params, int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(String sql, Map params, int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(Map params, String sql, int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(GString gstring,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(GString gstring,
                 @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure,
                 int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure rowClosure)
            throws SQLException, DBCException;

    void eachRow(GString gstring, int offset, int maxRows,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    void eachRow(GString gstring,
                 @ClosureParams(value = SimpleType.class, options = {"groovy.sql.GroovyResultSet"}) Closure closure)
            throws SQLException, DBCException;

    /* *** Rows *** */

    List<GroovyRowResult> rows(String sql) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List rows(String sql, int offset, int maxRows,
              @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, List<Object> params) throws SQLException, DBCException;

    List<GroovyRowResult> rows(Map params, String sql) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, List<Object> params, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, Map params, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(Map params, String sql, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, Object[] params) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, Object[] params, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, List<Object> params,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, Map params,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(Map params, String sql,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, List<Object> params, int offset, int maxRows,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(String sql, Map params, int offset, int maxRows,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(Map params, String sql, int offset, int maxRows,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(GString sql, int offset, int maxRows) throws SQLException, DBCException;

    List<GroovyRowResult> rows(GString gstring) throws SQLException, DBCException;

    List<GroovyRowResult> rows(GString gstring,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;

    List<GroovyRowResult> rows(GString gstring, int offset, int maxRows,
                               @ClosureParams(value = SimpleType.class, options = {"java.sql.ResultSetMetaData"}) Closure metaClosure)
            throws SQLException, DBCException;


    GroovyRowResult firstRow(String sql) throws SQLException, DBCException;

    GroovyRowResult firstRow(GString gstring) throws SQLException, DBCException;

    GroovyRowResult firstRow(String sql, List<Object> params) throws SQLException, DBCException;

    GroovyRowResult firstRow(Map params, String sql) throws SQLException, DBCException;

    GroovyRowResult firstRow(String sql, Object[] params) throws SQLException, DBCException;


    boolean execute(String sql) throws SQLException, DBCException;

    void execute(String sql, Closure processResults) throws SQLException, DBCException;

    boolean execute(String sql, List<Object> params) throws SQLException, DBCException;

    void execute(String sql, List<Object> params, Closure processResults) throws SQLException, DBCException;

    boolean execute(Map params, String sql) throws SQLException, DBCException;

    void execute(Map params, String sql, Closure processResults) throws SQLException, DBCException;

    boolean execute(String sql, Object[] params) throws SQLException, DBCException;

    void execute(String sql, Object[] params, Closure processResults) throws SQLException, DBCException;

    boolean execute(GString gstring) throws SQLException, DBCException;

    void execute(GString gstring, Closure processResults) throws SQLException, DBCException;


    int call(String sql) throws Exception;

    int call(GString gstring) throws Exception;

    int call(String sql, List<Object> params) throws Exception;

    int call(String sql, Object[] params) throws Exception;

    void call(String sql, List<Object> params, Closure closure) throws Exception;

    void call(GString gstring, Closure closure) throws Exception;


    int[] withBatch(Closure closure) throws SQLException, DBCException;

    int[] withBatch(int batchSize, Closure closure) throws SQLException, DBCException;

    int[] withBatch(String sql, Closure closure) throws SQLException, DBCException;

    int[] withBatch(int batchSize, String sql, Closure closure) throws SQLException, DBCException;
}
