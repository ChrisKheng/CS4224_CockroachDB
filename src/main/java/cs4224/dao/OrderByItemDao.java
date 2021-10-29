package cs4224.dao;

import cs4224.entities.Order;

import java.util.List;

public class OrderByItemDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public OrderByItemDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.dbQueryHelper = dbQueryHelper;
        this.schema = schema;
    }

    public List<Order> getOrdersOfItem(Long itemId) throws Exception {
        final String query = String.format("SELECT O_W_ID, O_D_ID, O_ID FROM %s.order_by_item WHERE I_ID = ?", schema);
        return dbQueryHelper.getQueryResult(query, Order.class, itemId);
    }
}
