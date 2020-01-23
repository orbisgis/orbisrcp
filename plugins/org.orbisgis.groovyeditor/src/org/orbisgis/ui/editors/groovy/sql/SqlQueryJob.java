/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2019 Serge Rider (serge@jkiss.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orbisgis.ui.editors.groovy.sql;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.data.DBDDataFilter;
import org.jkiss.dbeaver.model.data.DBDDataReceiver;
import org.jkiss.dbeaver.model.exec.*;
import org.jkiss.dbeaver.model.impl.AbstractExecutionSource;
import org.jkiss.dbeaver.model.impl.local.StatResultSet;
import org.jkiss.dbeaver.model.preferences.DBPPreferenceStore;
import org.jkiss.dbeaver.model.qm.QMUtils;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.sql.*;
import org.jkiss.dbeaver.model.struct.DBSDataContainer;
import org.jkiss.dbeaver.runtime.jobs.DataSourceJob;
import org.jkiss.dbeaver.runtime.sql.SQLResultsConsumer;
import org.jkiss.dbeaver.ui.editors.sql.SQLPreferenceConstants;
import org.jkiss.dbeaver.ui.editors.sql.internal.SQLEditorActivator;
import org.jkiss.dbeaver.utils.RuntimeUtils;
import org.jkiss.utils.CommonUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLQueryJob
 *
 * @author Serge Rider
 */
public class SqlQueryJob extends DataSourceJob
{
    private static final Log log = Log.getLog(SqlQueryJob.class);

    public static final Object STATS_RESULTS = new Object();

    private final DBSDataContainer dataContainer;
    private final SQLScriptElement query;
    private final SQLScriptContext scriptContext;
    private final SQLResultsConsumer resultsConsumer;

    private DBDDataFilter dataFilter;
    private boolean connectionInvalidated = false;

    private boolean fetchResultSets;
    private long rsOffset;
    private long rsMaxRows;

    private DBCStatement curStatement;
    private final List<DBCResultSet> curResultSets = new ArrayList<>();
    private Throwable lastError = null;


    private int fetchSize;

    public SqlQueryJob(
            @NotNull String name,
            @NotNull DBCExecutionContext executionContext,
            @Nullable DBSDataContainer dataContainer,
            @NotNull SQLScriptElement query,
            @NotNull SQLScriptContext scriptContext,
            @Nullable SQLResultsConsumer resultsConsumer)
    {
        super(name, executionContext);
        this.dataContainer = dataContainer;
        this.query = query;
        this.scriptContext = scriptContext;
        this.resultsConsumer = resultsConsumer;

        {
            // Read config form preference store
            DBPPreferenceStore preferenceStore = getDataSourceContainer().getPreferenceStore();
            this.fetchResultSets = true;
            this.rsMaxRows = preferenceStore.getInt("resultset.maxrows");
        }
    }

    public void setFetchResultSets(boolean fetchResultSets)
    {
        this.fetchResultSets = fetchResultSets;
    }

    public DBCStatement getCurrentStatement() {
        return curStatement;
    }

    private boolean hasLimits()
    {
        return rsOffset >= 0 && rsMaxRows > 0;
    }

    public void setResultSetLimit(long offset, long maxRows) {
        this.rsOffset = offset;
        this.rsMaxRows = maxRows;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    @Override
    protected IStatus run(DBRProgressMonitor monitor)
    {
        RuntimeUtils.setThreadName("SQL script execution");
        try {
            DBCExecutionContext context = getExecutionContext();
            DBCExecutionPurpose purpose = DBCExecutionPurpose.USER;
            try (DBCSession session = context.openSession(monitor, DBCExecutionPurpose.USER, "SQL Query")) {
                // Set transaction settings (only if autocommit is off)
                if (session.isLoggingEnabled()) {
                    QMUtils.getDefaultHandler().handleScriptBegin(session);
                }

                executeSingleQuery(session, query);

                if (session.isLoggingEnabled()) {
                    QMUtils.getDefaultHandler().handleScriptEnd(session);
                }

                // Return success
                return new Status(
                        Status.OK,
                        SQLEditorActivator.PLUGIN_ID,
                        "SQL job completed");
            }
        }
        catch (Throwable ex) {
            return new Status(
                    Status.ERROR,
                    SQLEditorActivator.PLUGIN_ID,
                    "Error during SQL job execution: " + ex.getMessage());
        }
    }

    private boolean executeSingleQuery(@NotNull DBCSession session, @NotNull SQLScriptElement element)
    {
        /*if (element instanceof SQLControlCommand) {
            try {
                return executeControlCommand((SQLControlCommand)element);
            } catch (Throwable e) {
                if (!(e instanceof DBException)) {
                    log.error("Unexpected error while processing SQL command", e);
                }
                lastError = e;
                return false;
            }
        }*/
        SQLQuery sqlQuery = (SQLQuery) element;
        lastError = null;

        final DBCExecutionContext executionContext = getExecutionContext();
        final DBPDataSource dataSource = executionContext.getDataSource();

        //final SQLQuery originalQuery = sqlQuery;
        long startTime = System.currentTimeMillis();

        // Modify query (filters + parameters)
        /*String queryText = originalQuery.getText();//.trim();
        if (dataFilter != null && dataFilter.hasFilters() && dataSource instanceof SQLDataSource) {
            String filteredQueryText = ((SQLDataSource) dataSource).getSQLDialect().addFiltersToQuery(
                    dataSource, queryText, dataFilter);
            sqlQuery = new SQLQuery(executionContext.getDataSource(), filteredQueryText, sqlQuery);
        } else {
            sqlQuery = new SQLQuery(executionContext.getDataSource(), queryText, sqlQuery);
        }*/

        final SQLQueryResult curResult = new SQLQueryResult(sqlQuery);
        if (rsOffset > 0) {
            curResult.setRowOffset(rsOffset);
        }

        try {
            // Prepare statement
            closeStatement();

            // Check and invalidate connection
            if (!connectionInvalidated && dataSource.getContainer().getPreferenceStore().getBoolean(SQLPreferenceConstants.STATEMENT_INVALIDATE_BEFORE_EXECUTE)) {
                executionContext.invalidateContext(session.getProgressMonitor(), true);
                connectionInvalidated = true;
            }

            startTime = System.currentTimeMillis();

            SQLQuery execStatement = sqlQuery;
            DBExecUtils.tryExecuteRecover(session, session.getDataSource(), param -> {
                try {
                    // We can't reset statistics here (we can be in script mode)
                    //statistics.setStatementsCount(0);
                    //statistics.setExecuteTime(0);
                    //statistics.setFetchTime(0);
                    //statistics.setRowsUpdated(0);
                    long execStartTime = System.currentTimeMillis();
                    executeStatement(session, execStatement, execStartTime, curResult);
                } catch (Throwable e) {
                    throw new InvocationTargetException(e);
                }
            });
        }
        catch (Throwable ex) {
            if (!(ex instanceof DBException)) {
                log.error("Unexpected error while processing SQL", ex);
            }
            curResult.setError(ex);
            lastError = ex;
        }
        finally {
            curResult.setQueryTime(System.currentTimeMillis() - startTime);

            scriptContext.clearStatementContext();
        }

        return curResult.getError() == null;
        // Success
    }

    private void executeStatement(@NotNull DBCSession session, SQLQuery sqlQuery, long startTime, SQLQueryResult curResult) throws DBCException {
        DBCExecutionSource source = new AbstractExecutionSource(dataContainer, session.getExecutionContext(), null/*partSite.getPart()*/, sqlQuery);
        final DBCStatement dbcStatement = DBUtils.makeStatement(
                source,
                session,
                DBCStatementType.SCRIPT,
                sqlQuery,
                rsOffset,
                rsMaxRows);
        DBExecUtils.setStatementFetchSize(dbcStatement, rsOffset, rsMaxRows, fetchSize);
        curStatement = dbcStatement;

        int statementTimeout = getDataSourceContainer().getPreferenceStore().getInt(SQLPreferenceConstants.STATEMENT_TIMEOUT);
        if (statementTimeout > 0) {
            try {
                dbcStatement.setStatementTimeout(statementTimeout);
            } catch (Throwable e) {
                log.debug("Can't set statement timeout:" + e.getMessage());
            }
        }

        // Execute statement
        try {
            session.getProgressMonitor().subTask("Execute query");

            boolean hasResultSet = dbcStatement.executeStatement();

            curResult.setHasResultSet(hasResultSet);

            long updateCount = -1;
            while (true) {
                // Fetch data only if we have to fetch all results or if it is rs requested
                if (hasResultSet && fetchResultSets) {
                    DBCResultSet resultSet = dbcStatement.openResultSet();
                    if (resultSet == null) {
                        // Kind of bug in the driver. It says it has resultset but returns null
                        break;
                    } else {
                        DBDDataReceiver dataReceiver = resultsConsumer.getDataReceiver(sqlQuery, 0);
                        if (dataReceiver != null) {
                            hasResultSet = fetchQueryData(session, resultSet, curResult, curResult.addExecuteResult(true), dataReceiver, true);
                        }
                    }
                }
                if (!hasResultSet) {
                    try {
                        updateCount = dbcStatement.getUpdateRowCount();
                        if (updateCount >= 0) {
                            curResult.addExecuteResult(false).setUpdateCount(updateCount);
                        }
                    } catch (DBCException e) {
                        // In some cases we can't read update count
                        // This is bad but we can live with it
                        // Just print a warning
                        log.warn("Can't obtain update count", e);
                    }
                }
                if (!hasResultSet && updateCount < 0) {
                    // Nothing else to fetch
                    break;
                }

                if (session.getDataSource().getInfo().supportsMultipleResults()) {
                    try {
                        hasResultSet = dbcStatement.nextResults();
                    } catch (DBCException e) {
                        if (session.getDataSource().getInfo().isMultipleResultsFetchBroken()) {
                            log.error(e);
                            // #2792: Check this twice. Some drivers (e.g. Sybase jConnect)
                            // throw error on n'th result fetch - but it still can keep fetching next results
                            hasResultSet = dbcStatement.nextResults();
                        } else {
                            throw e;
                        }
                    }
                    updateCount = hasResultSet ? -1 : 0;
                } else {
                    break;
                }
            }
        }
        finally {
            try {
                curResult.addWarnings(dbcStatement.getStatementWarnings());
            } catch (Throwable e) {
                log.warn("Can't read execution warnings", e);
            }
            //monitor.subTask("Close query");
            if (!keepStatementOpen()) {
                closeStatement();
            }
        }
    }

    private void fetchExecutionResult(@NotNull DBCSession session, @NotNull DBDDataReceiver dataReceiver, @NotNull SQLQuery query) throws DBCException
    {
        // Fetch fake result set
        StatResultSet fakeResultSet = new StatResultSet(session, curStatement);
        SQLQueryResult resultInfo = new SQLQueryResult(query);
        SQLQueryResult.ExecuteResult executeResult = resultInfo.addExecuteResult(true);

        fetchQueryData(session, fakeResultSet, resultInfo, executeResult, dataReceiver, false);
    }

    private boolean fetchQueryData(DBCSession session, DBCResultSet resultSet, SQLQueryResult result, SQLQueryResult.ExecuteResult executeResult, DBDDataReceiver dataReceiver, boolean updateStatistics)
            throws DBCException
    {
        if (dataReceiver == null) {
            // No data pump - skip fetching stage
            return false;
        }
        if (resultSet == null) {
            return false;
        }
        boolean keepCursor = keepStatementOpen();

        if (keepCursor) {
            curResultSets.add(resultSet);
        }
        DBRProgressMonitor monitor = session.getProgressMonitor();
        monitor.subTask("Fetch result set");
        long rowCount = 0;

        dataReceiver.fetchStart(session, resultSet, rsOffset, rsMaxRows);

        try {
            String sourceName = null;//resultSet.getResultSetName();
            if (result != null) {
                final String queryTitle = result.getStatement().getQueryTitle();
                if (!CommonUtils.isEmpty(queryTitle)) {
                    sourceName = queryTitle;
                } else {
                    // Retrieve source entity
                    DBCResultSetMetaData rsMeta = resultSet.getMeta();
                    for (DBCAttributeMetaData attr : rsMeta.getAttributes()) {
                        String entityName = attr.getEntityName();
                        if (!CommonUtils.isEmpty(entityName)) {
                            if (sourceName == null) {
                                sourceName = entityName;
                            } else if (!sourceName.equals(entityName)) {
                                // Multiple source entities
                                sourceName += "(+)";
                                break;
                            }
                        }
                    }
                    if (CommonUtils.isEmpty(sourceName)) {
                        try {
                            sourceName = resultSet.getResultSetName();
                        } catch (Exception e) {
                            // This will happen quite often, do not log it
                        }
                    }
                }
                if (CommonUtils.isEmpty(sourceName)) {
                    sourceName = "Result";
                }
                executeResult.setResultSetName(sourceName);
            }
            long fetchStartTime = System.currentTimeMillis();

            // Fetch all rows
            while ((!hasLimits() || rowCount < rsMaxRows) && resultSet.nextRow()) {
                if (monitor.isCanceled()) {
                    break;
                }
                rowCount++;

                if (rowCount > 0 && rowCount % 100 == 0) {
                    monitor.subTask(rowCount + " rows fetched");
                    monitor.worked(100);
                }

                dataReceiver.fetchRow(session, resultSet);
            }
        }
        finally {
            if (!keepCursor) {
                try {
                    resultSet.close();
                } catch (Throwable e) {
                    log.error("Error while closing resultset", e);
                }
            }
            try {
                dataReceiver.fetchEnd(session, resultSet);
            } catch (Throwable e) {
                log.error("Error while handling end of result set fetch", e);
            }
            dataReceiver.close();
        }

        if (result != null) {
            executeResult.setRowCount(rowCount);
        }
        monitor.subTask(rowCount + " rows fetched");

        return true;
    }

    private boolean keepStatementOpen()
    {
        // Only in single query mode and if pref option set to true
        return getDataSourceContainer().getPreferenceStore().getBoolean("keep.statement.open");
    }

    private void closeStatement()
    {
        if (curStatement != null) {
            try {
                for (DBCResultSet resultSet : curResultSets) {
                    resultSet.close();
                }
            } finally {
                curResultSets.clear();

                try {
                    curStatement.close();
                } catch (Throwable e) {
                    log.error("Error closing statement", e);
                } finally {
                    curStatement = null;
                }
            }
        }
    }

    public void extractData(@NotNull DBCSession session, @NotNull SQLScriptElement query, int resultNumber)
            throws DBCException
    {
        // Reset query to original. Otherwise multiple filters will corrupt it
        query.reset();

        //session.getProgressMonitor().beginTask(CommonUtils.truncateString(query.getText(), 512), 1);
        session.getProgressMonitor().subTask(CommonUtils.truncateString(query.getText(), 512));

        boolean result = executeSingleQuery(session, query);
        if (!result && lastError != null) {
            if (lastError instanceof DBCException) {
                throw (DBCException) lastError;
            } else {
                throw new DBCException(lastError, getExecutionContext().getDataSource());
            }
        }
    }

    public void setDataFilter(DBDDataFilter dataFilter)
    {
        this.dataFilter = dataFilter;
    }

    public void closeJob() {
        closeStatement();
    }
}
