package cs4224.dao;
import cs4224.entities.Customer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CustomerDao {
    private final QueryResultToEntityMapper queryResultToEntityMapper;
    private final String schema;

    public CustomerDao(final QueryResultToEntityMapper queryResultToEntityMapper, final String schema) {
        this.schema = schema;
        this.queryResultToEntityMapper = queryResultToEntityMapper;
    }

    public Customer updateAndGetById(double payment, long warehouseId, long districtId, long customerId) throws SQLException {
        final String updateAndGetQuery = String.format("UPDATE %s.CUSTOMER SET (C_BALANCE, C_YTD_PAYMENT, C_PAYMENT_CNT) = " +
                "(C_BALANCE - ?, C_YTD_PAYMENT + ?, C_PAYMENT_CNT + 1) " +
                "WHERE (C_W_ID, C_D_ID, C_ID) = (?, ?, ?) " +
                "RETURNING C_FIRST, C_MIDDLE, C_LAST, C_STREET_1, C_STREET_2, C_CITY, C_STATE, C_ZIP, " +
                "C_PHONE, C_SINCE, C_CREDIT, C_CREDIT_LIM, C_DISCOUNT, C_BALANCE ", schema);
        final List<Customer> customers = queryResultToEntityMapper.getQueryResult(updateAndGetQuery, Customer.class,
                new BigDecimal(payment), payment, warehouseId, districtId, customerId);
        return customers.get(0);
    }

    public Customer getNameById(long warehouseId, long districtId, long customerId) throws SQLException {
        final String query = String.format("SELECT C_FIRST, C_MIDDLE, C_LAST FROM %s.customer WHERE C_W_ID = ? AND C_D_ID = ? " +
                "AND C_ID = ?", schema);
        final List<Customer> customers = queryResultToEntityMapper.getQueryResult(query, Customer.class, warehouseId,
                districtId, customerId);
        return customers.get(0);
    }

    //RS fr order status
    public Customer getCustInfo(long warehouseId, long districtId, long customerId) throws SQLException
    {
        final String getCustInfoQuery = String.format("SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE FROM %s.customer WHERE C_W_ID = ? " +
                "AND C_D_ID = ? AND C_ID = ?", schema);
        final List<Customer> customers = queryResultToEntityMapper.getQueryResult(getCustInfoQuery, Customer.class,
                warehouseId, districtId, customerId);
        return customers.get(0);


    }

}





