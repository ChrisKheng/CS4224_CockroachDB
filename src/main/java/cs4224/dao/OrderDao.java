package cs4224.dao;

import cs4224.entities.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class OrderDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public OrderDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.dbQueryHelper = dbQueryHelper;
        this.schema = schema;
    }

    public List<Order> getById(long warehouseId, long districtId, long greaterThanId, long lessThanId) throws SQLException {
        final String query = String.format("SELECT O_ID, O_ENTRY_D, O_C_ID FROM %s.orders WHERE O_W_ID = ? AND O_D_ID = ? " +
                "AND O_ID >= ? AND O_ID < ?", schema);
        return dbQueryHelper.getQueryResult(query, Order.class, warehouseId, districtId,
                greaterThanId, lessThanId);
    }

    public Order getCustomerLastOrder(Long warehouseId, Long districtId, Long customerId) throws SQLException {
        final String query = String.format(
                "SELECT O_ID, O_ENTRY_D, O_CARRIER_ID " +
                "FROM %s.orders " +
                "WHERE O_W_ID = ? AND O_D_ID = ? AND O_C_ID = ? " +
                "ORDER BY O_ID DESC " +
                "LIMIT 1",
                schema);
        return dbQueryHelper.getQueryResult(query, Order.class, warehouseId, districtId, customerId).get(0);
    }

    public Order insertAndReturnOrder(Connection connection, long orderId, long warehouseId, long districtId,
                                      long customerId, BigDecimal numItems, BigDecimal allLocal) throws SQLException {
        final String query = String.format(
                "INSERT INTO %s.orders (O_ID, O_D_ID, O_W_ID, O_C_ID, O_ENTRY_D, O_CARRIER_ID, O_OL_CNT, O_ALL_LOCAL)\n" +
                        "VALUES (?, ?, ?, ?, now(), NULL, ?, ?)\n"+
                        "RETURNING O_ID, O_ENTRY_D",
                schema);
        return dbQueryHelper.getQueryResult(connection, query, Order.class, orderId, districtId, warehouseId, customerId,
                 numItems, allLocal).get(0);
    }

    public Order getState() {
        try {
            final String query = String.format("SELECT max(O_ID) as O_ID, sum(O_OL_CNT) as O_OL_CNT FROM %s.orders", schema);
            final List<Order> orders = dbQueryHelper.getQueryResult(query, Order.class);
            return orders.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Order();
        }
    }
}