package cs4224.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Order;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryResultToEntityMapper {
    public static List<Order> getOrderQueryResult(QueryRunner queryRunner, ObjectMapper objectMapper,
                                              String query, Object... params) throws SQLException {
        List<Map<String, Object>> result = queryRunner.query(query, new MapListHandler(), params);
        return objectMapper.convertValue(result, new TypeReference<List<Order>>(){});
    }
}
