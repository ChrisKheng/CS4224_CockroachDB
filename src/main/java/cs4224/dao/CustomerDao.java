package cs4224.dao;
import cs4224.entities.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public CustomerDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.schema = schema;
        this.dbQueryHelper = dbQueryHelper;
    }

    public Customer updateAndGetById(Connection connection, double payment, long warehouseId, long districtId, long customerId) throws SQLException {
        final String updateAndGetQuery = String.format("UPDATE %s.CUSTOMER SET (C_BALANCE, C_YTD_PAYMENT, C_PAYMENT_CNT) = " +
                "(C_BALANCE - ?, C_YTD_PAYMENT + ?, C_PAYMENT_CNT + 1) " +
                "WHERE (C_W_ID, C_D_ID, C_ID) = (?, ?, ?) " +
                "RETURNING C_FIRST, C_MIDDLE, C_LAST, C_STREET_1, C_STREET_2, C_CITY, C_STATE, C_ZIP, " +
                "C_PHONE, C_SINCE, C_CREDIT, C_CREDIT_LIM, C_DISCOUNT, C_BALANCE ", schema);
        final List<Customer> customers = dbQueryHelper.getQueryResult(connection, updateAndGetQuery,
                Customer.class, new BigDecimal(payment), payment, warehouseId, districtId, customerId);
        return customers.get(0);
    }

    public Customer getNameById(long warehouseId, long districtId, long customerId) throws SQLException {
        final String query = String.format("SELECT C_FIRST, C_MIDDLE, C_LAST FROM %s.customer WHERE C_W_ID = ? AND C_D_ID = ? " +
                "AND C_ID = ?",  schema);
        final List<Customer> customers = dbQueryHelper.getQueryResult(query, Customer.class, warehouseId, districtId,
                customerId);
        return customers.get(0);
    }

    public Customer getNewOrderInfoById(Connection connection, long warehouseId, long districtId, long customerId) throws SQLException {
        final String query = String.format("SELECT C_LAST, C_CREDIT, C_DISCOUNT FROM %s.customer WHERE C_W_ID = ? AND C_D_ID = ? " +
                "AND C_ID = ?",  schema);
        final List<Customer> customers = dbQueryHelper.getQueryResult(connection, query, Customer.class, warehouseId, districtId,
                customerId);
        return customers.get(0);
    }

    public Customer getCustomerInfoForOrderStatus(long warehouseId, long districtId, long customerId)
            throws SQLException {
        final String getCustomerInfoQuery = String.format(
                "SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE " +
                "FROM %s.customer " +
                "WHERE C_W_ID = ? AND C_D_ID = ? AND C_ID = ?", schema);
        final List<Customer> customers = dbQueryHelper.getQueryResult(getCustomerInfoQuery, Customer.class, warehouseId,
                districtId, customerId);
        return customers.get(0);
    }

    public List<Customer> getTopBalance() throws SQLException {
        final String query = String.format(
                "SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE, W_NAME, D_NAME " +
                "FROM wholesale.warehouse AS w, wholesale.district AS d, " +
                        "(SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE, C_W_ID, C_D_ID " +
                        "FROM wholesale.customer " +
                        "ORDER BY C_BALANCE DESC " +
                        "LIMIT 10) AS c " +
                "WHERE w.W_ID = c.C_W_ID AND d.D_W_ID = c.C_W_ID AND d.D_ID = c.C_D_ID");
        return dbQueryHelper.getQueryResult(query, Customer.class);
    }

    public List<Customer> getRelatedCustomer(long warehouseId, long districtId, long customerId) throws SQLException {
        final String query = String.format(
                "WITH\n" +
                        " A as (\n" +
                        "   SELECT O_W_ID, O_D_ID, O_ID\n" +
                        "   FROM %s.orders\n" +
                        "   WHERE O_W_ID = ? AND O_D_ID = ? AND O_C_ID = ?\n" +
                        " ),\n" +
                        " B AS (\n" +
                        "    SELECT DISTINCT OL_W_ID, OL_D_ID, OL_O_ID, OL_I_ID\n" +
                        "    FROM A JOIN wholesale.order_line AS T\n" +
                        "    ON A.O_W_ID = T.OL_W_ID AND A.O_D_ID = T.OL_D_ID AND A.O_ID = T.OL_O_ID\n" +
                        "),\n" +
                        " C AS (\n" +
                        "    SELECT T.OL_W_ID AS O_W_ID, T.OL_D_ID AS O_D_ID, T.OL_O_ID AS O_ID\n" +
                        "    FROM B JOIN wholesale.order_line AS T\n" +
                        "    ON B.OL_I_ID = T.OL_I_ID\n" +
                        "    AND B.OL_W_ID != T.OL_W_ID\n" +
                        "    GROUP BY (B.OL_W_ID, B.OL_D_ID, B.OL_O_ID, T.OL_W_ID, T.OL_D_ID, T.OL_O_ID)\n" +
                        "    HAVING COUNT(DISTINCT B.OL_I_ID) > 1\n" +
                        ")\n" +
                        "SELECT DISTINCT T.O_W_ID AS C_W_ID, T.O_D_ID AS C_D_ID, T.O_C_ID AS C_ID\n" +
                        "FROM C JOIN wholesale.orders AS T\n" +
                        "ON C.O_W_ID = T.O_W_ID AND C.O_D_ID = T.O_D_ID AND C.O_ID = T.O_ID"
        , schema);

        return dbQueryHelper.getQueryResult(query, Customer.class, warehouseId, districtId, customerId);
    }

    public void updateCustomerForDeliveryXact(Connection connection, long warehouseId, long districtId,
                                              long customerId, BigDecimal totalOrderLinesAmount)
            throws SQLException {
        final String query = String.format(
                "UPDATE %s.customer\n" +
                        "SET (C_BALANCE, C_DELIVERY_CNT) = (C_BALANCE + ?, C_DELIVERY_CNT + 1)\n" +
                        "WHERE (C_W_ID, C_D_ID, C_ID) = (?, ?, ?)"
        , schema);
        dbQueryHelper.getQueryRunner().execute(connection, query, totalOrderLinesAmount, warehouseId, districtId,
                customerId);
    }

    public Customer getState() {
        try {
            final String query = String.format("SELECT sum(C_BALANCE) as C_BALANCE, sum(C_YTD_PAYMENT) as C_YTD_PAYMENT, " +
                    "sum(C_PAYMENT_CNT) as C_PAYMENT_CNT, sum(C_DELIVERY_CNT) as C_DELIVERY_CNT FROM %s.CUSTOMER", schema);
            final List<Customer> customers = dbQueryHelper.getQueryResult(query, Customer.class);
            return customers.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Customer();
        }
    }
}
