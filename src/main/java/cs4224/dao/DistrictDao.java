package cs4224.dao;

import cs4224.entities.District;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DistrictDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final String schema;

    public DistrictDao(final DbQueryHelper queryResultToEntityMapper, final String schema) {
        this.schema = schema;
        this.queryResultToEntityMapper = queryResultToEntityMapper;
    }

    public District updateAndGetById(Connection connection, double payment, long warehouseId, long districtId) throws SQLException {
        final String query = String.format("UPDATE %s.DISTRICT SET " +
                "D_YTD = D_YTD + ? " +
                "WHERE (D_W_ID, D_ID) = (?, ?) " +
                "RETURNING D_STREET_1, D_STREET_2, D_CITY, D_STATE, D_ZIP", schema);
        final List<District> districts = queryResultToEntityMapper.getQueryResult(connection, query, District.class,
                new BigDecimal(payment), warehouseId, districtId);
        return districts.get(0);
    }

    public District getNextOrderId(long warehouseId, long districtId) throws SQLException {
        final String query = String.format("SELECT D_NEXT_O_ID FROM %s.DISTRICT WHERE D_W_ID = ? AND D_ID = ?", schema);
        final List<District> districts = queryResultToEntityMapper.getQueryResult(query, District.class, warehouseId,
                districtId);
        return districts.get(0);
    }
}
