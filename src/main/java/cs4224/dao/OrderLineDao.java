package cs4224.dao;

import cs4224.entities.OrderLine;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderLineDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public OrderLineDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.dbQueryHelper = dbQueryHelper;
        this.schema = schema;
    }

    public OrderLine getOLQuantity(long warehouseId, long districtId, long orderId) throws SQLException {
        final String query = String.format("SELECT max(OL_QUANTITY) as OL_QUANTITY FROM %s.order_line WHERE OL_W_ID = ? AND " +
                "OL_D_ID = ? AND OL_O_ID = ?", schema);
        return dbQueryHelper.getQueryResult(query, OrderLine.class, warehouseId, districtId,
                orderId).get(0);
    }

    public List<OrderLine> getOrderLinesOfLastOrder(long warehouseId, long districtId, long orderId)
            throws SQLException {
        String query = String.format(
                "SELECT OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D " +
                "FROM %s.order_line " +
                "WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ?",
                schema);
        return dbQueryHelper.getQueryResult(query, OrderLine.class, warehouseId, districtId, orderId);
    }

    public List<Long> getOLItemIds(long warehouseId, long districtId, long startOrderId, long endOrderId)
            throws SQLException {
        final ColumnListHandler<Long> handler = new ColumnListHandler<>("OL_I_ID");
        final String query = String.format("SELECT DISTINCT OL_I_ID FROM %s.order_line WHERE OL_W_ID = ? " +
                "AND OL_D_ID = ? AND OL_O_ID >= ? AND OL_O_ID <= ?", schema);
        return dbQueryHelper.getQueryRunner().query(query, handler, warehouseId, districtId, startOrderId, endOrderId);
    }

    public List<OrderLine> setOrderLineDeliveryDateTimeAndReturnAmount(Connection connection, long warehouseId,
                                                                       long districtId, long orderId)
            throws SQLException {
        final String query = String.format(
                "UPDATE %s.order_line\n" +
                        "SET OL_DELIVERY_D = now()\n" +
                        "WHERE (OL_W_ID, OL_D_ID, OL_O_ID) = (?, ?, ?)\n" +
                        "RETURNING OL_AMOUNT"
        , schema);
        return dbQueryHelper.getQueryResult(connection, query, OrderLine.class, warehouseId, districtId, orderId);
    }

    public OrderLine getState(long warehouseId) {
        try {
            final String query = String.format("SELECT sum(OL_AMOUNT) as OL_AMOUNT, sum(OL_QUANTITY) as OL_QUANTITY FROM " +
                    "%s.order_line where OL_W_ID = ?", schema);
            final List<OrderLine> orderLines = dbQueryHelper.getQueryResult(query, OrderLine.class, warehouseId);
            return orderLines.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new OrderLine();
        }
    }
}
