package cs4224.dao;

import cs4224.entities.Order;

import java.sql.Connection;
import java.sql.SQLException;
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

    public Order getSmallestUndeliveredOrderId(Connection connection, long warehouseId, long districtId) throws SQLException {
        final String query = String.format(
                "SELECT O_ID\n" +
                        "FROM %s.orders\n" +
                        "WHERE (O_W_ID, O_D_ID) = (?, ?)\n" +
                        "AND O_CARRIER_ID IS NULL\n" +
                        "ORDER BY O_ID ASC\n" +
                        "LIMIT 1;"
        , schema);
        List<Order> result = dbQueryHelper.getQueryResult(connection, query, Order.class, warehouseId, districtId);
        return result.isEmpty() ? null : result.get(0);
    }

    public Order updateOrderCarrierIdAndReturnCustomerId(Connection connection, long carrierId, long warehouseId,
                                                         long districtId, long orderId)
            throws SQLException {
        final String query = String.format(
                "UPDATE %s.orders\n" +
                        "SET O_CARRIER_ID = ?\n" +
                        "WHERE (O_W_ID, O_D_ID, O_ID) = (?, ?, ?)\n" +
                        "RETURNING O_C_ID"
        , schema);
        return dbQueryHelper.getQueryResult(connection, query, Order.class, carrierId, warehouseId, districtId, orderId).get(0);
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