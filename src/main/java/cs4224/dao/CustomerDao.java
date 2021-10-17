package cs4224.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs4224.entities.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomerDao {
    private final ObjectMapper objectMapper;
    private final QueryRunner queryRunner;
    private final String schema;

    public CustomerDao(final ObjectMapper objectMapper, final QueryRunner queryRunner, final String schema) {
        this.objectMapper = objectMapper;
        this.queryRunner = queryRunner;
        this.schema = schema;
    }

    public Customer getCustomerById(Integer warehouseId, Integer districtId, Integer customerId) {
        List<Customer> customers = this.getQueryResult(String.format("SELECT * FROM %s.customer WHERE C_W_ID = ? AND " +
                        "C_D_ID = ? AND C_ID = ?", schema), warehouseId, districtId, customerId);
        return customers.get(0);
    }


    private List<Customer> getQueryResult(String query, Object... params) {
        try {
            List<Map<String, Object>> result = this.queryRunner.query(query, new MapListHandler(), params);
            return objectMapper.convertValue(result, new TypeReference<List<Customer>>(){});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
