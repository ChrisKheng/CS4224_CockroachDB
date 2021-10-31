package cs4224.transactions;


import cs4224.dao.CustomerDao;
import cs4224.dao.DistrictDao;
import cs4224.dao.DbQueryHelper;
import cs4224.dao.WarehouseDao;
import cs4224.entities.Customer;
import cs4224.entities.District;
import cs4224.entities.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PaymentTransaction extends BaseTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PopularItemTransaction.class);
    private final DbQueryHelper queryResultToEntityMapper;
    private final WarehouseDao warehouseDao;
    private final DistrictDao districtDao;
    private final CustomerDao customerDao;

    public PaymentTransaction(WarehouseDao warehouseDao, DistrictDao districtDao, CustomerDao customerDao,
                              DbQueryHelper queryResultToEntityMapper) {
        this.warehouseDao = warehouseDao;
        this.districtDao = districtDao;
        this.customerDao = customerDao;
        this.queryResultToEntityMapper = queryResultToEntityMapper;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final long customerWarehouseId = Long.parseLong(parameters[1]);
        final long customerDistrictId = Long.parseLong(parameters[2]);
        final long customerId = Long.parseLong(parameters[3]);
        final double paymentAmount = Double.parseDouble(parameters[4]);

        final List<Object> updatedEntities = updateEntities(customerWarehouseId, customerDistrictId, customerId,
                paymentAmount);
        printOutput((Warehouse) updatedEntities.get(0), (District) updatedEntities.get(1),
                (Customer) updatedEntities.get(2), paymentAmount);
    }

    @Override
    public String getType() {
        return "Payment";
    }

    private List<Object> updateEntities(final long customerWarehouseId, final long customerDistrictId,
                                        final long customerId, final double paymentAmount) throws Exception {
        final Connection connection = queryResultToEntityMapper.getConnection();
        connection.setAutoCommit(false);
        final List<Object> result = new ArrayList<>();
        try {
            result.add(warehouseDao.updateAndGetById(connection, paymentAmount, customerWarehouseId));
            result.add(districtDao.updateAndGetById(connection, paymentAmount, customerWarehouseId,
                    customerDistrictId));
            result.add(customerDao.updateAndGetById(connection, paymentAmount, customerWarehouseId, customerDistrictId,
                    customerId));
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (Exception ex) {
            LOGGER.error("Rolling back transaction due to error: ", ex);
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
            throw ex;
        }
        return result;
    }

    private void printOutput(final Warehouse warehouse, final District district, final Customer customer,
                             final double paymentAmount) {
        System.out.printf("\n Customer Identifier (C_W_ID, C_D_ID, C_ID): %s", customer.toSpecifier());
        System.out.println(customer.toName());
        System.out.println(customer.toAddress());
        System.out.println(customer.toOtherInfo());
        System.out.println(warehouse.toAddress());
        System.out.println(district.toAddress());
        System.out.printf(" Payment Amount: %f\n", paymentAmount);
    }
}
