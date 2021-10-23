package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Item;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.List;

public class ItemDao {
    private final QueryResultToEntityMapper queryResultToEntityMapper;
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public ItemDao(final QueryResultToEntityMapper queryResultToEntityMapper, final ObjectMapper objectMapper,
                   final QueryRunner queryRunner, final String schema) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public Item getNameById(long id) throws SQLException {
        final String query = String.format("SELECT I_NAME FROM %s.item WHERE I_ID = ?", schema);
        final List<Item> items = queryResultToEntityMapper.getQueryResult(query, Item.class, id);
        return items.get(0);
    }
}
