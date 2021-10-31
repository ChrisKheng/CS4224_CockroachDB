package cs4224.transactions;

import cs4224.dao.*;
import cs4224.entities.Customer;
import cs4224.entities.Order;
import cs4224.entities.OrderLine;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class OrderStatusTransaction extends BaseTransaction {
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;

    public OrderStatusTransaction(CustomerDao customerDao, OrderDao orderDao, OrderLineDao orderLineDao) {
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
    }

    @Override
    public void execute(String[] dataLines,  String[] parameters) throws Exception {
        final long warehouseId = Long.parseLong(parameters[1]);
        final long districtId = Long.parseLong(parameters[2]);
        final long customerId = Long.parseLong(parameters[3]);

        Customer customer = customerDao.getCustomerInfoForOrderStatus(warehouseId, districtId, customerId);
        Order lastOrder = orderDao.getCustomerLastOrder(warehouseId, districtId, customerId);
        List<OrderLine> orderItems = orderLineDao.getOrderLinesOfLastOrder(warehouseId, districtId, lastOrder.getId());
        printOutput(customer, lastOrder, orderItems);
    }

    private void printOutput(Customer customer, Order lastOrder, List<OrderLine> orderLines) {
        System.out.printf("Customer info => customer's name: (%s, %s, %s), balance: %s\n", customer.getFirstName(),
                customer.getMiddleName(), customer.getLastName(), customer.getBalance().toString());
        System.out.printf("Last order info => order number: %d, entry date and time: %s, carrier id: %d\n",
                lastOrder.getId(), lastOrder.getEntryDateTime(), lastOrder.getCarrierId());

        System.out.printf("Last order's order lines =>\n");
        orderLines.forEach(ol -> {
            String formatString = "item number: %d, supplying warehouse number: %d, quantity ordered: %s, " +
                    "total price for ordered item: %s, delivery date and time: %s\n";
            System.out.printf(formatString, ol.getItemId(), ol.getSupplyingWarehouseId(), ol.getQuantity(),
                    ol.getAmount(), ol.getDeliveryDateTime());
        });
    }

    @Override
    public String getType() {
        return "Order Status";
    }
}