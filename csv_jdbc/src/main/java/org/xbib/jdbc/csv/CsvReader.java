package org.xbib.jdbc.csv;

import org.xbib.jdbc.io.DataReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 */
public class CsvReader extends DataReader {
    private CsvRawReader rawReader;
    private int transposedLines;
    private int transposedFieldsToSkip;
    private String[] columnNames;
    private String[] aliasedColumnNames;
    private String[] columnTypes;
    private String[] upperColumnNames;
    private Vector<String[]> firstTable;
    private int joiningValueNo;
    private int valuesToJoin;
    private String[] joiningValues;
    private StringConverter converter;
    private String[] fieldValues;
    private int lineNumber;

    public CsvReader(CsvRawReader rawReader, int transposedLines,
                     int transposedFieldsToSkip, String headerline) throws SQLException {
        super();

        this.rawReader = rawReader;
        this.transposedLines = transposedLines;
        this.transposedFieldsToSkip = transposedFieldsToSkip;
        this.columnNames = rawReader.parseLine(headerline, true);
        this.firstTable = null;
        columnTypes = null;

        if (!this.isPlainReader()) {
            firstTable = new Vector<String[]>();
            joiningValueNo = 0;
            joiningValues = null;
            try {
                String[] values = null;
                for (int i = 0; i < transposedLines; i++) {
                    String line;
                    line = rawReader.getNextDataLine();
                    values = rawReader.parseLine(line, false);
                    firstTable.add(values);
                }
                valuesToJoin = values.length;
                fieldValues = new String[columnNames.length];
            } catch (IOException e) {
                throw new SQLException(e.toString());
            }
        }
    }

    public void setConverter(StringConverter converter) {
        this.converter = converter;
    }

    private int getTransposedFieldsToSkip() {
        return transposedFieldsToSkip;
    }

    private boolean isPlainReader() {
        return transposedLines == 0 && transposedFieldsToSkip == 0;
    }

    @Override
    public boolean next() throws SQLException {
        if (this.isPlainReader()) {
            boolean result = rawReader.next();
            lineNumber = rawReader.getLineNumber();
            fieldValues = rawReader.getFieldValues();
            return result;
        } else {
            if (joiningValues == null ||
                    joiningValueNo + getTransposedFieldsToSkip() == valuesToJoin) {
                String line;
                try {
                    line = rawReader.getNextDataLine();
                    if (line != null) {
                        lineNumber = rawReader.getLineNumber();
                    }
                } catch (IOException e) {
                    throw new SQLException(e.toString());
                }
                if (line == null) {
                    return false;
                }
                joiningValues = rawReader.parseLine(line, false);
                joiningValueNo = 0;
            }
            for (int i = 0; i < transposedLines; i++) {
                fieldValues[i] = firstTable.get(i)[joiningValueNo
                        + getTransposedFieldsToSkip()];
            }
            for (int i = transposedLines; i < columnNames.length - 1; i++) {
                fieldValues[i] = joiningValues[i - transposedLines];
            }
            fieldValues[columnNames.length - 1] =
                    joiningValues[columnNames.length - transposedLines - 1 + joiningValueNo];
            joiningValueNo++;
            if (columnTypes == null) {
                getColumnTypes();
            }
            return true;
        }
    }

    @Override
    public String[] getColumnNames() {
        if (isPlainReader()) {
            return rawReader.getColumnNames();
        } else {
            return columnNames;
        }
    }

    private String[] getUpperColumnNames() {
        if (upperColumnNames == null) {
            String[] colNames = getColumnNames();
            upperColumnNames = new String[colNames.length];
            for (int i = 0; i < upperColumnNames.length; i++) {
                upperColumnNames[i] = colNames[i].toUpperCase();
            }
        }
        return upperColumnNames;
    }

    private String[] getAliasedColumnNames() {
        if (this.aliasedColumnNames == null) {
            String tableAlias = rawReader.getTableAlias();
            if (tableAlias != null) {
                /*
				 * Create array of "T.ID" column aliases that we can use for
				 * every row.
				 */
                String[] upperColumnNames = getUpperColumnNames();
                this.aliasedColumnNames = new String[upperColumnNames.length];
                for (int i = 0; i < upperColumnNames.length; i++) {
                    this.aliasedColumnNames[i] = tableAlias + "." + upperColumnNames[i];
                }
            }
        }
        return this.aliasedColumnNames;
    }

    @Override
    public Object getField(int i) throws SQLException {
        if (isPlainReader()) {
            return rawReader.getField(i);
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        rawReader.close();
    }

    @Override
    public Map<String, Object> getEnvironment() throws SQLException {
        if (fieldValues.length != getColumnNames().length) {
            throw new SQLException(CsvResources.getString("wrongColumnCount") + ": " +
                    lineNumber + " " +
                    CsvResources.getString("columnsRead") + ": " + fieldValues.length + " " +
                    CsvResources.getString("columnsExpected") + ": " + getColumnNames().length);
        }
        if (columnTypes == null) {
            getColumnTypes();
        }
        String[] columnNames = getUpperColumnNames();
        String[] columnAliases = getAliasedColumnNames();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put(StringConverter.COLUMN_NAME, converter);

        for (int i = 0; i < columnNames.length; i++) {
            String key = columnNames[i];
            Object value = converter.convert(columnTypes[i], fieldValues[i]);
            result.put(key, value);
            if (columnAliases != null) {
				/*
				 * Also allow column value to be accessed as S.ID if table alias
				 * S is set.
				 */
                result.put(columnAliases[i], value);
            }

        }
        return result;
    }

    public void setColumnTypes(String line) throws SQLException {
        String[] typeNamesLoc = line.split(",");
        if (typeNamesLoc.length == 0) {
            throw new SQLException(CsvResources.getString("invalidColumnType") + ": " + line);
        }
        columnTypes = new String[getColumnNames().length];
        for (int i = 0; i < Math.min(typeNamesLoc.length, columnTypes.length); i++) {
            String typeName = typeNamesLoc[i].trim();
            if (converter.forSQLName(typeName) == null) {
                throw new SQLException(CsvResources.getString("invalidColumnType") + ": " + typeName);
            }
            columnTypes[i] = typeName;
        }

		/*
		 * Use last column type for any remaining columns.
		 */
        for (int i = typeNamesLoc.length; i < columnTypes.length; i++) {
            columnTypes[i] = typeNamesLoc[typeNamesLoc.length - 1].trim();
        }
    }

    @Override
    public String[] getColumnTypes() throws SQLException {
        if (columnTypes == null) {
            inferColumnTypes();
        }
        return columnTypes;
    }

    private void inferColumnTypes() throws SQLException {
        if (fieldValues == null) {
            throw new SQLException(CsvResources.getString("cannotInferColumns"));
        }

        columnTypes = new String[fieldValues.length];
        for (int i = 0; i < fieldValues.length; i++) {
            try {
                String typeName = "String";
                String value = getField(i).toString();
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    typeName = "Boolean";
                } else if (value.equals(("" + converter.parseInt(value)))) {
                    typeName = "Int";
                } else if (value.equals(("" + converter.parseLong(value)))) {
                    typeName = "Long";
                } else if (value.equals(("" + converter.parseDouble(value)))) {
                    typeName = "Double";
                } else if (value.equals(("" + converter.parseBytes(value)))) {
                    typeName = "Bytes";
                } else if (value.equals(("" + converter.parseBigDecimal(value)))) {
                    typeName = "BigDecimal";
                } else if (converter.parseTimestamp(value) != null) {
                    typeName = "Timestamp";
                } else if (value.equals(("" + converter.parseDate(value) + "          ").substring(0, 10))) {
                    typeName = "Date";
                } else if (value.equals(("" + converter.parseTime(value) + "        ").substring(0, 8))) {
                    typeName = "Time";
                } else if (value.equals(("" + converter.parseAsciiStream(value)))) {
                    typeName = "AsciiStream";
                }
                columnTypes[i] = typeName;
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public int[] getColumnSizes() {
        return rawReader.getColumnSizes();
    }

    @Override
    public String getTableAlias() {
        return rawReader.getTableAlias();
    }
}
