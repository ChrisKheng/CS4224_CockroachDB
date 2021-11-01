package cs4224.dao;

import cs4224.entities.Item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ItemDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final String schema;

    public ItemDao(final DbQueryHelper queryResultToEntityMapper, final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.schema = schema;
    }

    public List<Item> getMaxQtyOrderLineItems(long warehouseId, long districtId, long orderId, BigDecimal orderLineQuantity) throws SQLException {
        final String query = String.format("SELECT I_ID, I_NAME FROM %s.item WHERE I_ID IN (SELECT OL_I_ID FROM " +
                "%s.order_line WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ? AND OL_QUANTITY = ?)", schema, schema);
        return queryResultToEntityMapper.getQueryResult(query, Item.class, warehouseId, districtId, orderId, orderLineQuantity);
    }
}
