package cs4224.dao;

import cs4224.entities.District;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DistrictDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public DistrictDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.schema = schema;
        this.dbQueryHelper = dbQueryHelper;
    }

    public District updateAndGetById(Connection connection, double payment, long warehouseId, long districtId) throws SQLException {
        final String query = String.format("UPDATE %s.DISTRICT SET " +
                "D_YTD = D_YTD + ? " +
                "WHERE (D_W_ID, D_ID) = (?, ?) " +
                "RETURNING D_STREET_1, D_STREET_2, D_CITY, D_STATE, D_ZIP", schema);
        final List<District> districts = dbQueryHelper.getQueryResult(connection, query, District.class,
                new BigDecimal(payment), warehouseId, districtId);
        return districts.get(0);
    }

    public District getNextOrderId(long warehouseId, long districtId) throws SQLException {
        final String query = String.format("SELECT D_NEXT_O_ID FROM %s.DISTRICT WHERE D_W_ID = ? AND D_ID = ?", schema);
        final List<District> districts = dbQueryHelper.getQueryResult(query, District.class, warehouseId,
                districtId);
        return districts.get(0);
    }

    public District getState() {
        try {
            final String query = String.format("SELECT sum(D_YTD) as D_YTD, sum(D_NEXT_O_ID) as D_NEXT_O_ID FROM " +
                    "%s.DISTRICT", schema);
            final List<District> districts = dbQueryHelper.getQueryResult(query, District.class);
            return districts.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new District();
        }
    }
}
