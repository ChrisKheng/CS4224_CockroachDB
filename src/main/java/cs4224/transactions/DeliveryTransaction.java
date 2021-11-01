package cs4224.transactions;

import cs4224.dao.CustomerDao;
import cs4224.dao.DbQueryHelper;
import cs4224.dao.OrderDao;
import cs4224.dao.OrderLineDao;
import cs4224.entities.Order;
import cs4224.entities.OrderLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.IntStream;

public class DeliveryTransaction extends BaseTransaction{
    private static final int NO_OF_DISTRICTS = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(PopularItemTransaction.class);
    private final DbQueryHelper queryResultToEntityMapper;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;
    private final CustomerDao customerDao;

    public DeliveryTransaction(OrderDao orderDao, OrderLineDao orderLineDao, CustomerDao customerDao,
                               DbQueryHelper queryResultToEntityMapper) {
        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
        this.customerDao = customerDao;
    }

    @Override
    public void execute(String[] dataLins, String[] parameters) throws Exception {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int carrierId = Integer.parseInt(parameters[2]);

        IntStream.rangeClosed(1, NO_OF_DISTRICTS).forEach(districtId -> {
            try {
                deliverySmallestUndeliveredOrder(warehouseId, districtId, carrierId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void deliverySmallestUndeliveredOrder(long warehouseId, long districtId, long carrierId) throws Exception {
        final Connection connection = queryResultToEntityMapper.getConnection();
        connection.setAutoCommit(false);

        try {
            Order order = orderDao.getSmallestUndeliveredOrderId(connection, warehouseId, districtId);
            if (order != null) {
                long oid = order.getId();
                long customerId = orderDao.updateOrderCarrierIdAndReturnCustomerId(connection, carrierId, warehouseId,
                        districtId, oid).getCustomerId();
                List<OrderLine> updatedOrderLines = orderLineDao.setOrderLineDeliveryDateTimeAndReturnAmount(connection,
                        warehouseId, districtId, oid);
                BigDecimal totalAmount = updatedOrderLines.stream()
                        .map(ol -> ol.getAmount())
                        .reduce(new BigDecimal(0), (x, y) -> x.add(y));
                customerDao.updateCustomerForDeliveryXact(connection, warehouseId, districtId, customerId, totalAmount);
            } else {
                System.out.printf("Skipping district (%d, %d) as there is no undelivered order\n", warehouseId,
                        districtId);
            }
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
    }

    @Override
    public String getType() {
        return "Delivery";
    }
}
