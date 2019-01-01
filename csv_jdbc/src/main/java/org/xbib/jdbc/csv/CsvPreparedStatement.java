package org.xbib.jdbc.csv;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 */
public class CsvPreparedStatement extends CsvStatement implements PreparedStatement {
    private Object[] parameters;
    private String templateQuery;
    private SqlParser parser;

    protected CsvPreparedStatement(CsvConnection connection, String sql,
                                   int resultSetType) throws SQLException {
        super(connection, resultSetType);

        parser = new SqlParser();
        try {
            parser.parse(sql);
        } catch (Exception e) {
            throw new SQLException(CsvResources.getString("syntaxError") + ": " + e.getMessage());
        }

        parameters = new Object[parser.getPlaceholdersCount() + 1];
        templateQuery = sql;
    }

    private void checkParameterIndex(int parameterIndex) throws SQLException {
        if (parameterIndex < 1 || parameterIndex >= parameters.length) {
            throw new SQLException(CsvResources.getString("parameterIndex") + ": " + parameterIndex);
        }
    }

    @Override
    public void addBatch() throws SQLException {
    }

    @Override
    public void clearParameters() throws SQLException {
        for (int i = 1; i < parameters.length; i++) {
            parameters[i] = null;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        throw new SQLException(CsvResources.getString("methodNotSupported") + ": execute()");
    }

    @Override
    public ResultSet executeQuery() throws SQLException {

        checkOpen();

        CsvDriver.writeLog("CsvStatement:executeQuery() - sql= " + templateQuery);

		/*
         * Close any previous ResultSet, as required by JDBC.
		 */
        try {
            if (lastResultSet != null) {
                lastResultSet.close();
            }
        } finally {
            lastResultSet = null;
        }

        parser.setPlaceholdersValues(parameters);
        return executeParsedQuery(parser);
    }

    @Override
    public int executeUpdate() throws SQLException {
        checkOpen();
        return 0;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setClob(int parameterIndex, Reader x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long length) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long length) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setNString(int parameterIndex, String x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);

        this.parameters[parameterIndex] = x;
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
        this.parameters[parameterIndex] = x;
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        checkOpen();
        checkParameterIndex(parameterIndex);
    }
}
