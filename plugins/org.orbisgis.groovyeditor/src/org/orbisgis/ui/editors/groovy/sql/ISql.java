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
