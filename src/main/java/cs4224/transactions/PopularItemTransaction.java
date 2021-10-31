package cs4224.transactions;

import cs4224.dao.*;
import cs4224.entities.Customer;
import cs4224.entities.Item;
import cs4224.entities.Order;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PopularItemTransaction extends BaseTransaction {

    private final DistrictDao districtDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;
    private final ItemDao itemDao;

    public PopularItemTransaction(DistrictDao districtDao, CustomerDao customerDao, OrderDao orderDao,
                                  OrderLineDao orderLineDao, ItemDao itemDao) {
        this.districtDao = districtDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
        this.itemDao = itemDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final long warehouseId = Long.parseLong(parameters[1]);
        final long districtId = Long.parseLong(parameters[2]);
        final int L = Integer.parseInt(parameters[3]);

        final long nextOrderId = districtDao.getNextOrderId(warehouseId, districtId).getNextOrderId();
        final List<Order> orders = orderDao.getById(warehouseId, districtId,
                nextOrderId - L, nextOrderId);

        final Map<Order, BigDecimal> orderQuantity = new ConcurrentHashMap<>(); // Order -> max_quantity
        final Map<Long, List<Long>> orderItems = new ConcurrentHashMap<>(); // OrderId -> items
        final Map<Long, Customer> customerMap = new ConcurrentHashMap<>(); // CustomerId -> CustomerName
        final Map<Long, String> itemName = new ConcurrentHashMap<>(); // ItemId -> Item Name
        final Map<Long, Long> itemNumOrders = new ConcurrentHashMap<>(); // Item -> Num of Orders

        orders.parallelStream().forEach(order -> {
            BigDecimal max_quantity = getOrderLineMaxQuantity(warehouseId, districtId, order);
            if (max_quantity != null) {
                final List<Item> items = getMaxQtyOrderLineItems(warehouseId, districtId, order.getId(), max_quantity);
                orderItems.put(order.getId(), items.stream().map(Item::getId).collect(Collectors.toList()));
                items.forEach(item -> itemName.put(item.getId(), item.getName()));
            }
            max_quantity = max_quantity == null ? new BigDecimal(-1) : max_quantity;
            orderQuantity.put(order, max_quantity);
            customerMap.put(order.getCustomerId(), getCustomerName(warehouseId, districtId, order.getCustomerId()));
        });

        orderItems.forEach((order, oItems) ->
                oItems.forEach(item -> itemNumOrders.put(item, itemNumOrders.getOrDefault(item, 0L)+1)));

        printOutput(warehouseId, districtId, L, orderQuantity, orderItems, customerMap, itemNumOrders, itemName);
    }

    @Override
    public String getType() {
        return "Popular Item";
    }

    private BigDecimal getOrderLineMaxQuantity(final long warehouseId, final long districtId, final Order order) {
        try {
            return orderLineDao.getOLQuantity(warehouseId, districtId, order.getId()).getQuantity();
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while getting order line max quantity");
        }
    }

    private Customer getCustomerName(final long warehouseId, final long districtId, final long customerId) {
        try {
            return customerDao.getNameById(warehouseId, districtId, customerId);
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while getting customer");
        }
    }

    private List<Item> getMaxQtyOrderLineItems(final long warehouseId, final long districtId, final long orderId,
                                               final BigDecimal max_quantity) {
        try {
            return itemDao.getMaxQtyOrderLineItems(warehouseId, districtId, orderId, max_quantity);
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while getting item");
        }
    }

    private void printOutput(final long warehouseId, final long districtId, final int L,
                             final Map<Order, BigDecimal> orderQuantity, final Map<Long, List<Long>> orderItems,
                             final Map<Long, Customer> customerMap, final Map<Long, Long> itemPopularity,
                             final Map<Long, String> itemName) {
        System.out.printf(" Warehouse Id: %d, District Id: %d\n", warehouseId, districtId);
        System.out.printf(" Number of last orders to be examined: %d\n\n", L);

        orderQuantity.forEach((order, quantity) -> {
            System.out.printf(" Order number: %d, Entry date and time: %s\n", order.getId(), order.getEntryDateTime());
            System.out.printf(" Customer%s\n", customerMap.get(order.getCustomerId()).toName());
            orderItems.get(order.getId()).forEach(item ->
                    System.out.printf(" Item Name: %s, Order Line Quantity: %f\n", itemName.get(item), quantity));
            if (orderItems.get(order.getId()).size() == 0) {
                System.out.println(" No order lines added for the given order Id yet.");
            }
            System.out.println();
        });

        itemPopularity.forEach((item, numOrders) ->
                System.out.printf(" Item Name: %s, Percentage orders : %f\n", itemName.get(item),
                        ((float) numOrders / L)));
    }
}
