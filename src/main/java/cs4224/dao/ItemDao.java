package cs4224.dao;

import cs4224.entities.Item;

import java.sql.SQLException;
import java.util.List;

public class ItemDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final String schema;

    public ItemDao(final DbQueryHelper queryResultToEntityMapper, final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.schema = schema;
    }

    public Item getNameById(long id) throws SQLException {
        final String query = String.format("SELECT I_NAME FROM %s.item WHERE I_ID = ?", schema);
        final List<Item> items = queryResultToEntityMapper.getQueryResult(query, Item.class, id);
        return items.get(0);
    }
}
