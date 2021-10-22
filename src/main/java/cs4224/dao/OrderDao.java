package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Order;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.util.List;

public class OrderDao {
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public OrderDao(final ObjectMapper objectMapper, final QueryRunner queryRunner, final String schema) {
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public List<Long> getOrderIdsOfCustomer(Long warehouseId, Long districtId, Long customerId) throws SQLException {
        ColumnListHandler<Long> handler = new ColumnListHandler<>("O_ID");
        String query = String.format("SELECT O_ID FROM %s.orders WHERE O_W_ID = ? AND O_D_ID = ? AND O_C_ID = ?",
                schema);
        return queryRunner.query(query, handler, warehouseId, districtId, customerId);
    }

    public Long getCustomerIdOfOrder(Long warehouseId, Long districtId, Long orderId) throws SQLException {
        ScalarHandler<Long> handler = new ScalarHandler<>("O_C_ID");
        String query = String.format("SELECT O_C_ID FROM %s.orders WHERE O_W_ID = ? AND O_D_ID = ? AND O_ID = ?",
                schema);
        return queryRunner.query(query, handler, warehouseId, districtId, orderId);
    }
}
