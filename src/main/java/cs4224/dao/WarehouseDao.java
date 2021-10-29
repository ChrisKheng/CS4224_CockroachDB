package cs4224.dao;

import cs4224.entities.Warehouse;
import org.apache.commons.dbutils.QueryRunner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public WarehouseDao(final DbQueryHelper queryResultToEntityMapper, final QueryRunner queryRunner,
                        final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public Warehouse updateAndGetById(Connection connection, double payment, long warehouseId) throws SQLException {
        final String query = String.format("UPDATE %s.WAREHOUSE SET " +
                "W_YTD = W_YTD + ? " +
                "WHERE W_ID = ? " +
                "RETURNING W_STREET_1, W_STREET_2, W_CITY, W_STATE, W_ZIP", schema);
        final List<Warehouse> warehouses = queryResultToEntityMapper.getQueryResult(connection, query, Warehouse.class,
                new BigDecimal(payment), warehouseId);
        return warehouses.get(0);
    }

    public Warehouse getState() {
        try {
            final String query = String.format("SELECT sum(W_YTD) as W_YTD FROM %s.WAREHOUSE", schema);
            final List<Warehouse> warehouses = queryResultToEntityMapper.getQueryResult(query, Warehouse.class);
            return warehouses.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Warehouse();
        }
    }

    public List<Warehouse> getAllWarehouseIDs() {
        try {
            final String query = String.format("SELECT W_ID FROM %s.WAREHOUSE", schema);
            return queryResultToEntityMapper.getQueryResult(query, Warehouse.class);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
