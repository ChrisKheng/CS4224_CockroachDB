package cs4224.dao;

import cs4224.entities.Order;

import java.util.List;

public class OrderByItemDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final String schema;

    public OrderByItemDao(final DbQueryHelper queryResultToEntityMapper, final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.schema = schema;
    }

    public List<Order> getOrdersOfItem(Long itemId) throws Exception {
        final String query = String.format("SELECT O_W_ID, O_D_ID, O_ID FROM %s.order_by_item WHERE I_ID = ?", schema);
        return queryResultToEntityMapper.getQueryResult(query, Order.class, itemId);
    }
}
