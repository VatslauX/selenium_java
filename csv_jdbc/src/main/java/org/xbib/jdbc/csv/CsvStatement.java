package org.xbib.jdbc.csv;

import org.xbib.jdbc.io.CryptoFilter;
import org.xbib.jdbc.io.DataReader;
import org.xbib.jdbc.io.EncryptedFileInputStream;
import org.xbib.jdbc.io.FileSetInputStream;
import org.xbib.jdbc.io.ListDataReader;
import org.xbib.jdbc.io.TableReader;
import org.xbib.jdbc.dbf.DbfReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the java.sql.Statement JDBC interface for the
 * CsvJdbc driver.
 */
public class CsvStatement implements Statement {
    /*
     * Key for column name in database rows for accessing CsvStatement.
     */
    public static final String STATEMENT_COLUMN_NAME = "@STATEMENT";

    private CsvConnection connection;
    protected ResultSet lastResultSet = null;
    protected List<SqlParser> multipleParsers = null;
    private int maxRows = 0;
    private int fetchSize = 1;
    private int fetchDirection = ResultSet.FETCH_FORWARD;
    private boolean closed;

    protected int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;

    /**
     * Creates new Statement for use in executing SQL statements.
     *
     * @param connection    database connection to create statement for.
     * @param resultSetType either ResultSet.TYPE_SCROLL_INSENSITIVE or
     *                      ResultSet.TYPE_SCROLL_SENSITIVE.
     */
    protected CsvStatement(CsvConnection connection, int resultSetType) {
        CsvDriver.writeLog("CsvStatement() - connection="
                + connection);
        CsvDriver.writeLog("CsvStatement() - Asked for "
                + (resultSetType == ResultSet.TYPE_SCROLL_SENSITIVE ? "Scrollable"
                : "Not Scrollable"));
        this.connection = connection;
        this.resultSetType = resultSetType;
    }

    protected void checkOpen() throws SQLException {
        if (closed) {
            throw new SQLException(CsvResources.getString("statementClosed"));
        }
    }

    @Override
    public void setMaxFieldSize(int p0) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": setMaxFieldSize(int)");
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        checkOpen();

        maxRows = max;
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": setEscapeProcessing(boolean)");
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": setQueryTimeout(int)");
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": setCursorName(String)");
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        checkOpen();

        if (direction == ResultSet.FETCH_FORWARD ||
                direction == ResultSet.FETCH_REVERSE ||
                direction == ResultSet.FETCH_UNKNOWN) {
            this.fetchDirection = direction;
        } else {
            throw new SQLFeatureNotSupportedException(CsvResources.getString("unsupportedDirection") + ": " + direction);
        }
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        checkOpen();

        this.fetchSize = rows;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": getMaxFieldSize()");
    }

    @Override
    public int getMaxRows() throws SQLException {
        checkOpen();

        return maxRows;
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        checkOpen();

        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkOpen();

        return null;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        checkOpen();

        return lastResultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        checkOpen();

		/*
         * Driver is read-only, so no updates are possible.
		 */
        return -1;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        checkOpen();

        try {
			/*
			 * Close any ResultSet that is currently open.
			 */
            if (lastResultSet != null) {
                lastResultSet.close();
            }
        } finally {
            lastResultSet = null;
        }
        boolean retval;
        if (multipleParsers != null && multipleParsers.size() > 0) {
			/*
			 * There are multiple SELECT statements being executed.  Go to the next one.
			 */
            lastResultSet = executeParsedQuery(multipleParsers.remove(0));
            retval = true;
        } else {
            retval = false;
        }
        return retval;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        checkOpen();

        return fetchDirection;
    }

    @Override
    public int getFetchSize() throws SQLException {
        checkOpen();

        return fetchSize;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        checkOpen();

        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        checkOpen();

        return this.resultSetType;
    }

    @Override
    public Connection getConnection() throws SQLException {
        checkOpen();

        return connection;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        checkOpen();

        CsvDriver.writeLog("CsvStatement:executeQuery() - sql= " + sql);

		/*
		 * Close any previous ResultSet, as required by JDBC.
		 */
        try {
            if (lastResultSet != null) {
                lastResultSet.close();
            }
        } finally {
            lastResultSet = null;
            multipleParsers = null;
        }

        SqlParser parser = new SqlParser();
        try {
            parser.parse(sql);
        } catch (Exception e) {
            throw new SQLException(CsvResources.getString("syntaxError") + ": " + e.getMessage());
        }

        return executeParsedQuery(parser);
    }

    protected ResultSet executeParsedQuery(SqlParser parser)
            throws SQLException {
        String path = connection.getPath();
        TableReader tableReader = connection.getTableReader();
        if (path != null) {
            CsvDriver.writeLog("Connection Path: " + path);
        } else {
            CsvDriver.writeLog("Connection TableReader: " + tableReader.getClass().getName());
        }
        CsvDriver.writeLog("Parser Table Name: " + parser.getTableName());
        CsvDriver.writeLog("Connection Extension: " + connection.getExtension());

        DataReader reader = null;
        String fileName = null;
        String tableName = parser.getTableName();
        if (tableName == null) {
			/*
			 * Create an empty dataset with one row.
			 */
            String[] columnNames = new String[0];
            String[] columnTypes = new String[0];
            ArrayList<Object[]> rows = new ArrayList<Object[]>();
            rows.add(new Object[0]);
            reader = new ListDataReader(columnNames, columnTypes, rows);
        } else {
            if (path != null && (!connection.isIndexedFiles())) {
                fileName = path + tableName + connection.getExtension();

                CsvDriver.writeLog("CSV file name: " + fileName);

                File checkFile = new File(fileName);

                if (!checkFile.exists()) {
                    throw new SQLException(CsvResources.getString("fileNotFound") + ": " + fileName);
                }

                if (!checkFile.canRead()) {
                    throw new SQLException(CsvResources.getString("fileNotReadable") + ": " + fileName);
                }
            }

            try {
                if (connection.getExtension().equalsIgnoreCase(".dbf")) {
                    reader = new DbfReader(fileName, parser.getTableAlias(), connection.getCharset());
                } else {
                    LineNumberReader input;
                    if (tableReader == null) {
                        InputStream in;
                        CryptoFilter filter = connection.getDecryptingCodec();
                        if (connection.isIndexedFiles()) {
                            String fileNamePattern = parser.getTableName()
                                    + connection.getFileNamePattern()
                                    + connection.getExtension();
                            String[] nameParts = connection.getNameParts();
                            String dirName = connection.getPath();
                            in = new FileSetInputStream(dirName,
                                    fileNamePattern, nameParts,
                                    connection.getSeparator(),
                                    connection.isFileTailPrepend(),
                                    connection.isSuppressHeaders(), filter,
                                    connection.getSkipLeadingDataLines()
                                            + connection.getTransposedLines());
                        } else if (filter == null) {
                            in = new FileInputStream(fileName);
                        } else {
                            filter.reset();
                            in = new EncryptedFileInputStream(fileName, filter);
                        }
                        if (connection.getCharset() != null) {
                            input = new LineNumberReader(new InputStreamReader(in, connection.getCharset()));
                        } else {
                            input = new LineNumberReader(new InputStreamReader(in));
                        }
                    } else {
						/*
						 * Reader for table comes from user-provided class.
						 */
                        input = new LineNumberReader(tableReader.getReader(this, tableName));
                    }

                    String headerline = connection.getHeaderline(tableName);
                    CsvRawReader rawReader = new CsvRawReader(input,
                            parser.getTableAlias(), connection.getSeparator(),
                            connection.isSuppressHeaders(),
                            connection.isHeaderFixedWidth(),
                            connection.getQuotechar(),
                            connection.getCommentChar(), headerline,
                            connection.getTrimHeaders(),
                            connection.getTrimValues(),
                            connection.getSkipLeadingLines(),
                            connection.isIgnoreUnparseableLines(),
                            connection.isDefectiveHeaders(),
                            connection.getSkipLeadingDataLines(),
                            connection.getQuoteStyle(),
                            connection.getFixedWidthColumns());
                    reader = new CsvReader(rawReader,
                            connection.getTransposedLines(),
                            connection.getTransposedFieldsToSkip(), headerline);
                }
            } catch (IOException e) {
                throw new SQLException(CsvResources.getString("fileReadError") + ": " + e);
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException(CsvResources.getString("dataReaderError") + ": " + e);
            }
        }

        CsvResultSet resultSet = null;
        try {
            resultSet = new CsvResultSet(this, reader, tableName,
                    parser.getColumns(), parser.isDistinct(), this.resultSetType,
                    parser.getWhereClause(), parser.getGroupByColumns(),
                    parser.getHavingClause(), parser.getOrderByColumns(),
                    parser.getLimit(), parser.getOffset(),
                    connection.getColumnTypes(tableName),
                    connection.getSkipLeadingLines());
            lastResultSet = resultSet;
        } catch (ClassNotFoundException e) {
            CsvDriver.writeLog("" + e);
        }

        return resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": Statement.executeUpdate(String)");
    }

    @Override
    public void close() throws SQLException {
        try {
            if (lastResultSet != null) {
                lastResultSet.close();
            }
        } finally {
            lastResultSet = null;
            multipleParsers = null;
            closed = true;
            connection.removeStatement(this);
        }
    }

    @Override
    public void cancel() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("NotNotSupported") + ": Statement.cancel()");
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkOpen();
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        CsvDriver.writeLog("CsvStatement:execute() - sql= " + sql);

		/*
		 * Close any previous ResultSet, as required by JDBC.
		 */
        try {
            if (lastResultSet != null) {
                lastResultSet.close();
            }
        } finally {
            lastResultSet = null;
            multipleParsers = null;
        }

		/*
		 * Execute one or more SQL statements.
		 * The method getMoreResults() will be used to step through the results.
		 */
        MultipleSqlParser parser = new MultipleSqlParser();
        try {
            List<SqlParser> parsers = parser.parse(sql);
            lastResultSet = executeParsedQuery(parsers.remove(0));
            multipleParsers = parsers;
        } catch (Exception e) {
            throw new SQLException(CsvResources.getString("syntaxError") + ": " + e.getMessage());
        }

        return true;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": Statement.addBatch(String)");
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": Statement.clearBatch()");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") + ": Statement.executeBatch()");
    }

    // ---------------------------------------------------------------------
    // JDBC 3.0
    // ---------------------------------------------------------------------

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.getMoreResults(int)");
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.getGeneratedKeys()");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.executeUpdate(String,int)");
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.executeUpdate(String,int[])");
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.executeUpdate(String,String[])");
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.execute(String,int)");
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.execute(String,int[])");
    }

    @Override
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.execute(String,String[])");
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.getResultSetHoldability()");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        checkOpen();

        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        checkOpen();
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }

    public boolean isCloseOnCompletion() throws SQLException {
        checkOpen();

        return false;
    }

    public void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException(CsvResources.getString("methodNotSupported") +
                ": Statement.closeOnCompletion()");
    }
}
