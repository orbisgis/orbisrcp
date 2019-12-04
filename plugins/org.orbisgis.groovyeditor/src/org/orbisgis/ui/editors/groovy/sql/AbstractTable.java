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

import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCResultSet;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.orbisdata.datamanager.api.dataset.IJdbcTable;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public abstract class AbstractTable implements IJdbcTable {

    DBCResultSet resultSet = null;

    protected abstract JDBCResultSet getResultSet() throws DBCException;
    protected abstract JDBCResultSet getResultSet(int rows) throws DBCException;
    protected abstract Logger getLogger();

    void invalidateResultSet(){
        resultSet.close();
        resultSet = null;
    }

    @Override
    public ResultSetMetaData getMetaData(){
        try {
            return getResultSet().getMetaData();
        } catch (DBCException|SQLException e) {
            getLogger().error("Unable to get the ResultSet metadata.", e);
        }
        return null;
    }

    @Override
    public boolean next() throws SQLException {
        try {
            return getResultSet().nextRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            getResultSet().close();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean wasNull() throws SQLException {
        try {
            return getResultSet().wasNull();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String getString(int i) throws SQLException {
        try {
            return getResultSet().getString(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        try {
            return getResultSet().getBoolean(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public byte getByte(int i) throws SQLException {
        try {
            return getResultSet().getByte(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public short getShort(int i) throws SQLException {
        try {
            return getResultSet().getShort(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getInt(int i) throws SQLException {
        try {
            return getResultSet().getInt(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public long getLong(int i) throws SQLException {
        try {
            return getResultSet().getLong(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public float getFloat(int i) throws SQLException {
        try {
            return getResultSet().getFloat(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public double getDouble(int i) throws SQLException {
        try {
            return getResultSet().getDouble(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        try {
            return getResultSet().getBigDecimal(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        try {
            return getResultSet().getBytes(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Date getDate(int i) throws SQLException {
        try {
            return getResultSet().getDate(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Time getTime(int i) throws SQLException {
        try {
            return getResultSet().getTime(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        try {
            return getResultSet().getTimestamp(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getAsciiStream(int i) throws SQLException {
        try {
            return getResultSet().getAsciiStream(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getUnicodeStream(int i) throws SQLException {
        try {
            return getResultSet().getUnicodeStream(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getBinaryStream(int i) throws SQLException {
        try {
            return getResultSet().getBinaryStream(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String getString(String s) throws SQLException {
        try {
            return getResultSet().getString(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean getBoolean(String s) throws SQLException {
        try {
            return getResultSet().getBoolean(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public byte getByte(String s) throws SQLException {
        try {
            return getResultSet().getByte(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public short getShort(String s) throws SQLException {
        try {
            return getResultSet().getShort(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getInt(String s) throws SQLException {
        try {
            return getResultSet().getInt(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public long getLong(String s) throws SQLException {
        try {
            return getResultSet().getLong(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public float getFloat(String s) throws SQLException {
        try {
            return getResultSet().getFloat(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public double getDouble(String s) throws SQLException {
        try {
            return getResultSet().getDouble(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String s, int i) throws SQLException {
        try {
            return getResultSet().getBigDecimal(s, i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public byte[] getBytes(String s) throws SQLException {
        try {
            return getResultSet().getBytes(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Date getDate(String s) throws SQLException {
        try {
            return getResultSet().getDate(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Time getTime(String s) throws SQLException {
        try {
            return getResultSet().getTime(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String s) throws SQLException {
        try {
            return getResultSet().getTimestamp(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getAsciiStream(String s) throws SQLException {
        try {
            return getResultSet().getAsciiStream(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getUnicodeStream(String s) throws SQLException {
        try {
            return getResultSet().getUnicodeStream(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public InputStream getBinaryStream(String s) throws SQLException {
        try {
            return getResultSet().getBinaryStream(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return getResultSet().getWarnings();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            getResultSet().clearWarnings();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String getCursorName() throws SQLException {
        try {
            return getResultSet().getCursorName();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object getObject(int i) throws SQLException {
        try {
            return getResultSet().getObject(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object getObject(String s) throws SQLException {
        try {
            return getResultSet().getObject(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int findColumn(String s) throws SQLException {
        try {
            return getResultSet().findColumn(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        try {
            return getResultSet().getCharacterStream(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Reader getCharacterStream(String s) throws SQLException {
        try {
            return getResultSet().getCharacterStream(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        try {
            return getResultSet().getBigDecimal(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String s) throws SQLException {
        try {
            return getResultSet().getBigDecimal(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        try {
            return getResultSet().isBeforeFirst();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        try {
            return getResultSet().isAfterLast();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isFirst() throws SQLException {
        try {
            return getResultSet().isFirst();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isLast() throws SQLException {
        try {
            return getResultSet().isLast();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        try {
            getResultSet().beforeFirst();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void afterLast() throws SQLException {
        try {
            getResultSet().afterLast();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean first() throws SQLException {
        try {
            return getResultSet().first();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean last() throws SQLException {
        try {
            return getResultSet().last();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getRow() throws SQLException {
        try {
            return getResultSet().getRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean absolute(int i) throws SQLException {
        try {
            getResultSet().absolute(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
        return true;
    }

    @Override
    public boolean relative(int i) throws SQLException {
        try {
            getResultSet().relative(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
        return true;
    }

    @Override
    public boolean previous() throws SQLException {
        try {
            getResultSet().previous();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
        return true;
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {
        try {
            getResultSet().setFetchDirection(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return getResultSet().getFetchDirection();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setFetchSize(int i) throws SQLException {
        try {
            getResultSet().setFetchSize(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        try {
            return getResultSet().getFetchSize();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getType() throws SQLException {
        try {
            return getResultSet().getType();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getConcurrency() throws SQLException {
        try {
            return getResultSet().getConcurrency();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        try {
            return getResultSet().rowUpdated();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean rowInserted() throws SQLException {
        try {
            return getResultSet().rowInserted();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        try {
            return getResultSet().rowDeleted();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNull(int i) throws SQLException {
        try {
            getResultSet().updateNull(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBoolean(int i, boolean b) throws SQLException {
        try {
            getResultSet().updateBoolean(i, b);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateByte(int i, byte b) throws SQLException {
        try {
            getResultSet().updateByte(i, b);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateShort(int i, short i1) throws SQLException {
        try {
            getResultSet().updateShort(i, i1);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateInt(int i, int i1) throws SQLException {
        try {
            getResultSet().updateInt(i, i1);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateLong(int i, long l) throws SQLException {
        try {
            getResultSet().updateLong(i, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateFloat(int i, float v) throws SQLException {
        try {
            getResultSet().updateFloat(i, v);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateDouble(int i, double v) throws SQLException {
        try {
            getResultSet().updateDouble(i, v);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
        try {
            getResultSet().updateBigDecimal(i, bigDecimal);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateString(int i, String s) throws SQLException {
        try {
            getResultSet().updateString(i, s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBytes(int i, byte[] bytes) throws SQLException {
        try {
            getResultSet().updateBytes(i, bytes);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateDate(int i, Date date) throws SQLException {
        try {
            getResultSet().updateDate(i, date);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateTime(int i, Time time) throws SQLException {
        try {
            getResultSet().updateTime(i, time);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
        try {
            getResultSet().updateTimestamp(i, timestamp);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
        try {
            getResultSet().updateAsciiStream(i, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
        try {
            getResultSet().updateBinaryStream(i, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {
        try {
            getResultSet().updateCharacterStream(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateObject(int i, Object o, int i1) throws SQLException {
        try {
            getResultSet().updateObject(i, o, i1);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateObject(int i, Object o) throws SQLException {
        try {
            getResultSet().updateObject(i, o);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNull(String s) throws SQLException {
        try {
            getResultSet().updateNull(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBoolean(String s, boolean b) throws SQLException {
        try {
            getResultSet().updateBoolean(s, b);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateByte(String s, byte b) throws SQLException {
        try {
            getResultSet().updateByte(s, b);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateShort(String s, short i) throws SQLException {
        try {
            getResultSet().updateShort(i, i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateInt(String s, int i) throws SQLException {
        try {
            getResultSet().updateInt(s, i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateLong(String s, long l) throws SQLException {
        try {
            getResultSet().updateLong(s, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateFloat(String s, float v) throws SQLException {
        try {
            getResultSet().updateFloat(s, v);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateDouble(String s, double v) throws SQLException {
        try {
            getResultSet().updateDouble(s, v);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
        try {
            getResultSet().updateBigDecimal(s, bigDecimal);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateString(String s, String s1) throws SQLException {
        try {
            getResultSet().updateString(s, s1);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBytes(String s, byte[] bytes) throws SQLException {
        try {
            getResultSet().updateBytes(s, bytes);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateDate(String s, Date date) throws SQLException {
        try {
            getResultSet().updateDate(s, date);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateTime(String s, Time time) throws SQLException {
        try {
            getResultSet().updateTime(s, time);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
        try {
            getResultSet().updateTimestamp(s, timestamp);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
        try {
            getResultSet().updateAsciiStream(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
        try {
            getResultSet().updateBinaryStream(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
        try {
            getResultSet().updateCharacterStream(s, reader, i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateObject(String s, Object o, int i) throws SQLException {
        try {
            getResultSet().updateObject(s, o, i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateObject(String s, Object o) throws SQLException {
        try {
            getResultSet().updateObject(s, o);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void insertRow() throws SQLException {
        try {
            getResultSet().insertRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRow() throws SQLException {
        try {
            getResultSet().updateRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteRow() throws SQLException {
        try {
            getResultSet().deleteRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void refreshRow() throws SQLException {
        try {
            getResultSet().refreshRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        try {
            getResultSet().cancelRowUpdates();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        try {
            getResultSet().moveToInsertRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        try {
            getResultSet().moveToCurrentRow();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        try {
            return getResultSet().getStatement();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        try {
            return getResultSet().getObject(i, map);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        try {
            return getResultSet().getRef(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        try {
            return getResultSet().getBlob(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        try {
            return getResultSet().getClob(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Array getArray(int i) throws SQLException {
        try {
            return getResultSet().getArray(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
        try {
            return getResultSet().getObject(s, map);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Ref getRef(String s) throws SQLException {
        try {
            return getResultSet().getRef(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Blob getBlob(String s) throws SQLException {
        try {
            return getResultSet().getBlob(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Clob getClob(String s) throws SQLException {
        try {
            return getResultSet().getClob(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Array getArray(String s) throws SQLException {
        try {
            return getResultSet().getArray(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Date getDate(int i, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getDate(i, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Date getDate(String s, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getDate(s, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Time getTime(int i, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getTime(i, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Time getTime(String s, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getTime(s, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getTimestamp(i, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
        try {
            return getResultSet().getTimestamp(s, calendar);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public URL getURL(int i) throws SQLException {
        try {
            return getResultSet().getURL(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public URL getURL(String s) throws SQLException {
        try {
            return getResultSet().getURL(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRef(int i, Ref ref) throws SQLException {
        try {
            getResultSet().updateRef(i, ref);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRef(String s, Ref ref) throws SQLException {
        try {
            getResultSet().updateRef(s, ref);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(int i, Blob blob) throws SQLException {
        try {
            getResultSet().updateBlob(i, blob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(String s, Blob blob) throws SQLException {
        try {
            getResultSet().updateBlob(s, blob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(int i, Clob clob) throws SQLException {
        try {
            getResultSet().updateClob(i, clob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(String s, Clob clob) throws SQLException {
        try {
            getResultSet().updateClob(s, clob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateArray(int i, Array array) throws SQLException {
        try {
            getResultSet().updateArray(i, array);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateArray(String s, Array array) throws SQLException {
        try {
            getResultSet().updateArray(s, array);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        try {
            return getResultSet().getRowId(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public RowId getRowId(String s) throws SQLException {
        try {
            return getResultSet().getRowId(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRowId(int i, RowId rowId) throws SQLException {
        try {
            getResultSet().updateRowId(i, rowId);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRowId(String s, RowId rowId) throws SQLException {
        try {
            getResultSet().updateRowId(s, rowId);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return getResultSet().getHoldability();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return getResultSet().isClosed();
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNString(int i, String s) throws SQLException {
        try {
            getResultSet().updateNString(i, s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNString(String s, String s1) throws SQLException {
        try {
            getResultSet().updateNString(s, s1);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(int i, NClob nClob) throws SQLException {
        try {
            getResultSet().updateNClob(i, nClob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(String s, NClob nClob) throws SQLException {
        try {
            getResultSet().updateNClob(s, nClob);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        try {
            return getResultSet().getNClob(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public NClob getNClob(String s) throws SQLException {
        try {
            return getResultSet().getNClob(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        try {
            return getResultSet().getSQLXML(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public SQLXML getSQLXML(String s) throws SQLException {
        try {
            return getResultSet().getSQLXML(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
        try {
            getResultSet().updateSQLXML(i, sqlxml);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {
        try {
            getResultSet().updateSQLXML(s, sqlxml);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String getNString(int i) throws SQLException {
        try {
            return getResultSet().getNString(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String getNString(String s) throws SQLException {
        try {
            return getResultSet().getNString(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        try {
            return getResultSet().getNCharacterStream(i);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Reader getNCharacterStream(String s) throws SQLException {
        try {
            return getResultSet().getNCharacterStream(s);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateNCharacterStream(i, reader, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateNCharacterStream(s, reader, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateAsciiStream(i, inputStream, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateBinaryStream(i, inputStream, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateCharacterStream(i, reader, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateAsciiStream(s, inputStream, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateBinaryStream(s, inputStream, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateCharacterStream(s, reader, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateBlob(i, inputStream, l);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {
        try {
            getResultSet().updateBlob(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(int i, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateClob(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(String s, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateClob(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(int i, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateNClob(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(String s, Reader reader, long l) throws SQLException {
        try {
            getResultSet().updateNClob(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader) throws SQLException {
        try {
            getResultSet().updateNCharacterStream(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNCharacterStream(String s, Reader reader) throws SQLException {
        try {
            getResultSet().updateNCharacterStream(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateAsciiStream(i, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateBinaryStream(i, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(int i, Reader reader) throws SQLException {
        try {
            getResultSet().updateCharacterStream(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateAsciiStream(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateBinaryStream(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCharacterStream(String s, Reader reader) throws SQLException {
        try {
            getResultSet().updateCharacterStream(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(int i, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateBlob(i, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateBlob(String s, InputStream inputStream) throws SQLException {
        try {
            getResultSet().updateBlob(s, inputStream);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(int i, Reader reader) throws SQLException {
        try {
            getResultSet().updateClob(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateClob(String s, Reader reader) throws SQLException {
        try {
            getResultSet().updateClob(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(int i, Reader reader) throws SQLException {
        try {
            getResultSet().updateNClob(i, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateNClob(String s, Reader reader) throws SQLException {
        try {
            getResultSet().updateNClob(s, reader);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T getObject(int i, Class<T> aClass) throws SQLException {
        try {
            return getResultSet().getObject(i, aClass);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T getObject(String s, Class<T> aClass) throws SQLException {
        try {
            return getResultSet().getObject(s, aClass);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        try {
            return getResultSet().unwrap(aClass);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        try {
            return getResultSet().isWrapperFor(aClass);
        } catch (DBCException e) {
            throw new SQLException(e);
        }
    }
}
