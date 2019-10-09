package org.orbisgis.ui.editors.groovy.sql;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.text.SimpleTemplateEngine;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.*;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.impl.AbstractExecutionSource;
import org.jkiss.dbeaver.model.runtime.VoidProgressMonitor;
import org.orbisgis.core.logger.Logger;
import org.orbisgis.datamanagerapi.dataset.IDataSet;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.orbisgis.datamanagerapi.dataset.ITable;
import org.orbisgis.datamanagerapi.datasource.IDataSourceLocation;
import org.orbisgis.datamanagerapi.datasource.IJdbcDataSource;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class DataSource extends Sql implements IJdbcDataSource {

    private static final Logger LOGGER = new Logger(DataSource.class);

    /** MetaClass used for the implementation of the {@link GroovyObject} methods */
    private MetaClass metaClass;
    private DBPDataSourceContainer container;

    public DataSource(DBPDataSourceContainer container){
        this.container = container;
        this.metaClass = InvokerHelper.getMetaClass(getClass());
    }

    @Override
    public IDataSet getDataSet(String name) {
        return getTable(name);
    }

    @Override
    public IDataSourceLocation getLocation() {
        return new DataSourceLocation(container.getFolder());
    }

    @Override
    public void close() {
        container.getDataSource().shutdown(new VoidProgressMonitor());
    }

    @Override
    public ITable getTable(String tableName) {
        return new Table(tableName, container);
    }

    @Override
    public ISpatialTable getSpatialTable(String tableName) {
        return new SpatialTable(tableName, container);
    }

    //TODO
    @Override
    public Collection<String> getTableNames() {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'getTableNames'");
        throw e;
    }

    @Override
    public boolean save(String tableName, String filePath) {
        return save(tableName, filePath, null);
    }

    //TODO
    @Override
    public boolean save(String tableName, String filePath, String encoding) {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'save'");
        throw e;
        //return IOMethods.saveAsFile(getConnection(), tableName, filePath, encoding);
    }

    @Override
    public boolean save(String tableName, URL url) {
        return save(tableName, url, null);
    }

    @Override
    public boolean save(String tableName, URL url, String encoding) {
        try {
            return save(tableName, url.toURI(), encoding);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public boolean save(String tableName, URI uri) {
        return save(tableName, uri, null);
    }

    @Override
    public boolean save(String tableName, URI uri, String encoding) {
        return save(tableName, new File(uri), encoding);
    }

    @Override
    public boolean save(String tableName, File file) {
        return save(tableName, file, null);
    }

    @Override
    public boolean save(String tableName, File file, String encoding) {
        return save(tableName, file.getAbsolutePath(), encoding);
    }

    private String getTableNameFromPath(String filePath){
        int start = filePath.lastIndexOf("/")+1;
        int end = filePath.lastIndexOf(".");
        if(end == -1){
            end = filePath.length();
        }
        return filePath.substring(start, end).toUpperCase();
    }

    //TODO
    @Override
    public ITable link(String filePath, String tableName, boolean delete) {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'link'");
        throw e;
        //IOMethods.link(filePath, tableName, delete, this);
    }

    @Override
    public ITable link(String filePath, String tableName) {
        return link(filePath, tableName, false);
    }

    @Override
    public ITable link(String filePath, boolean delete) {
        String tableName = getTableNameFromPath(filePath);
        if (Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$").matcher(tableName).find()) {
            return link(filePath, tableName, delete);
        } else {
            LOGGER.error("The file name contains unsupported characters");
        }
        return null;
    }

    @Override
    public ITable link(String filePath) {
        return link(filePath, false);
    }

    @Override
    public ITable link(URL url, String tableName, boolean delete) {
        try {
            return link(url.toURI(), tableName, delete);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable link(URL url, String tableName) {
        try {
            return link(url.toURI(), tableName);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable link(URL url, boolean delete) {
        try {
            return link(url.toURI(), delete);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable link(URL url) {
        try {
            return link(url.toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable link(URI uri, String tableName, boolean delete) {
        return link(new File(uri), tableName, delete);
    }

    @Override
    public ITable link(URI uri, String tableName) {
        return link(new File(uri), tableName);
    }

    @Override
    public ITable link(URI uri, boolean delete) {
        return link(new File(uri), delete);
    }

    @Override
    public ITable link(URI uri) {
        return link(new File(uri));
    }

    @Override
    public ITable link(File file, String tableName, boolean delete) {
        return link(file.getAbsolutePath(), tableName, delete);
    }

    @Override
    public ITable link(File file, String tableName) {
        return link(file.getAbsolutePath(), tableName);
    }

    @Override
    public ITable link(File file, boolean delete) {
        return link(file.getAbsolutePath(), delete);
    }

    @Override
    public ITable link(File file) {
        return link(file.getAbsolutePath());
    }

    @Override
    public ITable load(Map<String, String> properties, String inputTableName) {
        return load(properties, inputTableName, inputTableName, false);
    }

    @Override
    public ITable load(Map<String, String> properties, String inputTableName, boolean delete) {
        return load(properties, inputTableName, inputTableName, delete);
    }

    @Override
    public ITable load(Map<String, String> properties, String inputTableName, String outputTableName) {
        return load(properties, inputTableName, outputTableName, false);
    }

    //TODO
    @Override
    public ITable load(Map<String, String> properties, String inputTableName, String outputTableName, boolean delete) {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'load'");
        throw e;
        //IOMethods.loadTable(properties, inputTableName, outputTableName, delete, this);
    }

    //TODO
    @Override
    public ITable load(String filePath, String tableName, String encoding, boolean delete) {
        UnsupportedOperationException e = new UnsupportedOperationException();
        LOGGER.error("Unsupported operation 'load'");
        throw e;
        //IOMethods.loadFile(filePath, tableName, encoding, delete, this);
    }

    @Override
    public ITable load(String filePath, String tableName) {
        return load(filePath, tableName, null, false);
    }

    @Override
    public ITable load(String filePath, String tableName, boolean delete) {
        return load(filePath, tableName, null, delete);
    }

    @Override
    public ITable load(String filePath) {
        return load(filePath, false);
    }

    @Override
    public ITable load(String filePath, boolean delete) {
        String tableName = getTableNameFromPath(filePath);
        if (Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$").matcher(tableName).find()) {
            return load(filePath,tableName, null, delete);
        } else {
            LOGGER.error("Unsupported file characters");
        }
        return null;
    }

    @Override
    public ITable load(URL url, String tableName) {
        try {
            return load(url.toURI(), tableName, null, false);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable load(URL url, String tableName, boolean delete) {
        try {
            return load(url.toURI(), tableName, null, delete);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable load(URL url) {
        try {
            return load(url.toURI(), false);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable load(URL url, boolean delete) {
        try {
            return load(url.toURI(), delete);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable load(URL url, String tableName, String encoding, boolean delete) {
        try {
            return load(url.toURI(), tableName, encoding, delete);
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the file from the URL '" + url.toString() + "'\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public ITable load(URI uri, String tableName) {
        return load(new File(uri), tableName, null, false);
    }

    @Override
    public ITable load(URI uri, String tableName, boolean delete) {
        return load(new File(uri), tableName, null, delete);
    }

    @Override
    public ITable load(URI uri) {
        return load(new File(uri), false);
    }

    @Override
    public ITable load(URI uri, boolean delete) {
        return load(new File(uri), delete);
    }

    @Override
    public ITable load(URI uri, String tableName, String encoding, boolean delete) {
        return load(new File(uri), tableName, encoding, delete);
    }

    @Override
    public ITable load(File file, String tableName) {
        return load(file.getAbsolutePath(), tableName, null, false);
    }

    @Override
    public ITable load(File file, String tableName, boolean delete) {
        return load(file.getAbsolutePath(), tableName, null, delete);
    }

    @Override
    public ITable load(File file) {
        return load(file.getAbsolutePath(), false);
    }

    @Override
    public ITable load(File file, boolean delete) {
        return load(file.getAbsolutePath(), delete);
    }

    @Override
    public ITable load(File file, String tableName, String encoding, boolean delete) {
        return load(file.getAbsolutePath(), tableName, encoding, delete);
    }

    @Override
    public boolean executeScript(String fileName, Map<String, String> bindings) {
        File file = new File(fileName);
        if(file.exists()) {
            try {
                return executeScript(new FileInputStream(file), bindings);
            } catch (FileNotFoundException e) {
                LOGGER.error("Unable to execute the script '" + fileName + "'.", e);
            }
        }
        else{
            LOGGER.error("Script file '" + fileName + "' doesn't exists.");
        }
        return false;
    }

    @Override
    public boolean executeScript(InputStream stream, Map<String, String> bindings) {
        SimpleTemplateEngine engine = null;
        if (bindings != null && !bindings.isEmpty()) {
            engine = new SimpleTemplateEngine();
        }
        ScriptReader scriptReader = new ScriptReader(new InputStreamReader(stream));
        scriptReader.setSkipRemarks(true);
        while (true) {
            String commandSQL = scriptReader.readStatement();
            if (commandSQL == null) {
                break;
            }
            if (!commandSQL.isEmpty()) {
                if (engine != null) {
                    try {
                        commandSQL = engine.createTemplate(commandSQL).make(bindings).toString();
                    } catch (ClassNotFoundException | IOException e) {
                        LOGGER.error("Unable to create the template for the Sql command '" + commandSQL + "'.\n" +
                                e.getLocalizedMessage());
                        return false;
                    }
                }
                try {
                    if(!executeQuery(commandSQL)){
                        LOGGER.error("Unable to execute '" + commandSQL + "'");
                        return false;
                    }
                } catch (DBCException e) {
                    LOGGER.error("Unable to execute the Sql command '" + commandSQL + "'.\n" + e.getLocalizedMessage());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean executeQuery(String query) throws DBCException {
        DBCExecutionContext context = DBUtils.getDefaultContext(container.getDataSource(), false);
        DBCSession session = context.openSession(new VoidProgressMonitor(), DBCExecutionPurpose.USER, "SQL Query");
        DBCExecutionSource source = new AbstractExecutionSource(null, session.getExecutionContext(), null, query);
        final DBCStatement dbcStatement = DBUtils.makeStatement(source, session, DBCStatementType.QUERY, query, 0, 0);
        return dbcStatement.executeStatement();
    }

    @Override
    protected JDBCResultSet executeQueryWithRs(String query) throws DBCException {
        DBCExecutionContext context = DBUtils.getDefaultContext(container.getDataSource(), false);
        DBCSession session = context.openSession(new VoidProgressMonitor(), DBCExecutionPurpose.USER, "SQL Query");
        DBCExecutionSource source = new AbstractExecutionSource(null, session.getExecutionContext(), null, query);
        final DBCStatement dbcStatement = DBUtils.makeStatement(source, session, DBCStatementType.QUERY, query, 0, 0);
        if (dbcStatement.executeStatement()) {
            return (JDBCResultSet) dbcStatement.openResultSet();
        } else {
            return null;
        }
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }
}
