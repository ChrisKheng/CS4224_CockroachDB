package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Order;
import cs4224.entities.OrderLine;
import cs4224.handlers.HashSetHandler;
import org.apache.commons.dbutils.QueryRunner;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class OrderLineDao {
    private final QueryResultToEntityMapper queryResultToEntityMapper;
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public OrderLineDao(final QueryResultToEntityMapper queryResultToEntityMapper, final ObjectMapper objectMapper,
                        final QueryRunner queryRunner, final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public HashSet<Long> getItemIdsOfOrder(Long warehouseId, Long districtId, Long orderId) throws SQLException {
        HashSetHandler<Long> handler = new HashSetHandler<>("OL_I_ID");
        String query = String.format("SELECT OL_I_ID FROM %s.order_line WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ?",
                schema);
        return queryRunner.query(query, handler, warehouseId, districtId, orderId);
    }

    public List<Order> getOrdersOfItem(Long itemId) throws SQLException {
        final String query = String.format("SELECT DISTINCT OL_W_ID AS O_W_ID, OL_D_ID AS O_D_ID, OL_O_ID AS O_ID " +
                "FROM %s.order_line " +
                "WHERE OL_I_ID = ?", schema);
        return queryResultToEntityMapper.getQueryResult(query, Order.class, itemId);
    }

    public OrderLine getOLQuantity(long warehouseId, long districtId, long orderId) throws SQLException {
        final String query = String.format("SELECT max(OL_QUANTITY) as OL_QUANTITY FROM %s.order_line WHERE OL_W_ID = ? AND " +
                "OL_D_ID = ? AND OL_O_ID = ?", schema);
        return queryResultToEntityMapper.getQueryResult(query, OrderLine.class, warehouseId, districtId,
                orderId).get(0);
    }

    public List<OrderLine> getOLItemId(long warehouseId, long districtId, long orderId, BigDecimal orderLineQuantity)
            throws SQLException {
        final String query = String.format("SELECT OL_I_ID FROM %s.order_line WHERE OL_W_ID = ? AND OL_D_ID = ? " +
                "AND OL_O_ID = ? AND OL_QUANTITY = ?", schema);
        return queryResultToEntityMapper.getQueryResult(query, OrderLine.class, warehouseId, districtId,
                orderId, orderLineQuantity);
    }
}
