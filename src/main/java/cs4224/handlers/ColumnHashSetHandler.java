package cs4224.handlers;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * This class maps a column to a hash set.
 * Reference of how to use JDBC ResultSet: https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
 */
public class ColumnHashSetHandler<T> implements ResultSetHandler<HashSet<T>> {
    private final String columnName;

    public ColumnHashSetHandler(String columnName) {
        this.columnName = columnName;
    }

    public HashSet<T> handle(ResultSet rs) throws SQLException {
        HashSet<T> result = new HashSet<>();

        while(rs.next()) {
            result.add((T) rs.getObject(columnName));
        }

        return result;
    }
}
