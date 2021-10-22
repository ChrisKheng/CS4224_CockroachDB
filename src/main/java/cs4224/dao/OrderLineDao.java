package cs4224.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Order;
import cs4224.handlers.HashSetHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.util.HashSet;
import java.util.List;

public class OrderLineDao {
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public OrderLineDao(final ObjectMapper objectMapper, final QueryRunner queryRunner, final String schema) {
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public HashSet<Long> getItemIdsOfOrder(Long warehouseId, Long districtId, Long orderId) throws Exception {
        HashSetHandler<Long> handler = new HashSetHandler<>("OL_I_ID");
        String query = String.format("SELECT OL_I_ID FROM %s.order_line WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ?",
                schema);
        return queryRunner.query(query, handler, warehouseId, districtId, orderId);
    }
}
