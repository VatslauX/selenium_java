/*
 *  CsvJdbc - a JDBC driver for CSV files
 *  Copyright (C) 2008  Mario Frasca
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Map;

class SQLAvgFunction extends SQLSumFunction {
    public SQLAvgFunction(boolean isDistinct, Expression expression) {
        super(isDistinct, expression);
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object o = super.eval(env);
        if (o != null) {
            double average = ((Number) o).doubleValue() / counter;
            o = new Double(average);
        }
        return o;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AVG(");
        if (distinctValues != null) {
            sb.append("DISTINCT ");
        }
        sb.append(expression);
        sb.append(")");
        return sb.toString();
    }
}
