package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Order;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

public class OrderByItemDao {
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public OrderByItemDao(final ObjectMapper objectMapper, final QueryRunner queryRunner, final String schema) {
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public List<Order> getOrdersOfItem(Long itemId) throws Exception {
        String query = String.format("SELECT O_W_ID, O_D_ID, O_ID FROM %s.order_by_item WHERE I_ID = ?", schema);
        return QueryResultToEntityMapper.getOrderQueryResult(queryRunner, objectMapper, query, itemId);
    }
}
