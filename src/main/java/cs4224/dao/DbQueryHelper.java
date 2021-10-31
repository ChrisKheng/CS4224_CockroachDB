package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.google.inject.Inject;
import lombok.Getter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Getter
public class DbQueryHelper {

    private final QueryRunner queryRunner;
    private final ObjectMapper objectMapper;

    @Inject
    public DbQueryHelper(final QueryRunner queryRunner, final ObjectMapper objectMapper) {
        this.queryRunner = queryRunner;
        this.objectMapper = objectMapper;
    }

    public<T> List<T> getQueryResult(String query, Class<T> clazz, Object... params) throws SQLException {
        final List<Map<String, Object>> result = queryRunner.query(query, new MapListHandler(), params);
        final CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, clazz);
        return objectMapper.convertValue(result, type);
    }

    public<T> List<T> getQueryResult(Connection connection, String query, Class<T> clazz, Object... params)
            throws SQLException {
        final List<Map<String, Object>> result = queryRunner.query(connection, query, new MapListHandler(), params);
        final CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, clazz);
        return objectMapper.convertValue(result, type);
    }

    public Connection getConnection() throws SQLException {
        return queryRunner.getDataSource().getConnection();
    }
}
