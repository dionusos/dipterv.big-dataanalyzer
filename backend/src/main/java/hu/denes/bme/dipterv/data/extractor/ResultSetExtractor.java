package hu.denes.bme.dipterv.data.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ResultSetExtractor {
    public abstract Object extract(ResultSet rs, int col) throws SQLException;


    public static class StringResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return rs.getString(col);
        }
    }

    public static class BooleanResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return new Boolean(rs.getBoolean(col));
        }
    }

    public static class FloatResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return new Float(rs.getFloat(col));
        }
    }

    public static class DoubleResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return new Float(rs.getDouble(col));
        }
    }

    public static class IntegerResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return new Integer(rs.getInt(col));
        }
    }

    public static class LongResultSetExtractor extends ResultSetExtractor {
        public Object extract(ResultSet rs, int col) throws SQLException {
            return new Long(rs.getLong(col));
        }
    }
}
