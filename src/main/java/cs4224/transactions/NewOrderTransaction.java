package cs4224.transactions;


import cs4224.dao.*;
import cs4224.entities.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NewOrderTransaction extends BaseTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderTransaction.class);
    private int customerId;
    private int warehouseId;
    private int districtId;
    private int noOfItems;

    private final DistrictDao districtDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;
    private final ItemDao itemDao;
    private final OrderByItemDao orderByItemDao;
    private final WarehouseDao warehouseDao;
    private final StockDao stockDao;
    private final DbQueryHelper queryResultToEntityMapper;

    @RequiredArgsConstructor
    @Accessors(fluent = true) @Getter
    private class NewOrderLine {
        private final int itemId;
        private final int supplierWarehouseId;
        private final int quantity;
    }

    private List<NewOrderLine> parseNewOrderLines(String[] datalines) {
        return Arrays.stream(datalines)
                .map(s -> s.split(","))
                .map(tokens -> new NewOrderLine(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
                        Integer.parseInt(tokens[2])))
                .collect(Collectors.toList());
    }

    public NewOrderTransaction(DistrictDao districtDao, CustomerDao customerDao,
                               OrderDao orderDao, OrderLineDao orderLineDao,
                               ItemDao itemDao, OrderByItemDao orderByItemDao,
                               WarehouseDao warehouseDao, StockDao stockDao, DbQueryHelper queryResultToEntityMapper) {
        this.districtDao = districtDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
        this.itemDao = itemDao;
        this.orderByItemDao = orderByItemDao;
        this.warehouseDao = warehouseDao;
        this.stockDao = stockDao;
        this.queryResultToEntityMapper = queryResultToEntityMapper;

    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {

        customerId = Integer.parseInt(parameters[1]);
        warehouseId = Integer.parseInt(parameters[2]);
        districtId = Integer.parseInt(parameters[3]);
        noOfItems = Integer.parseInt(parameters[4]);

        System.out.printf("Running New Order Transaction with C_ID= %d, W_ID=%d, D_ID=%d, N=%d \n", customerId, warehouseId, districtId, noOfItems);
        for (String line : dataLines) {
            System.out.println(line);
        }
        final Connection connection = queryResultToEntityMapper.getConnection();
        connection.setAutoCommit(false);

        List<NewOrderLine> newOrderLines = parseNewOrderLines(dataLines);

        String[] output = new String[noOfItems];
        try {

            // 1
            final long districtNextOrderId = districtDao.getNextOrderId(warehouseId, districtId).getNextOrderId();

            // 2
            BigDecimal districtTax = districtDao.updateIncrementNextOrderId(connection, warehouseId, districtId).getTax();

            // 3
            /*
            Connection connection, long id, long warehouseId, long districtId,
                                      long customerId, long carrierId, BigDecimal numItems, BigDecimal allLocal, Instant entryDateTime
             */
            BigDecimal isAllMatch = newOrderLines.stream().allMatch(x -> x.supplierWarehouseId == warehouseId)
                    ? BigDecimal.ONE
                    : BigDecimal.ZERO;
            Order newOrder = orderDao.insertAndReturnOrder(connection, districtNextOrderId, warehouseId, districtId,
                    customerId, -1, BigDecimal.valueOf(noOfItems), isAllMatch, System.currentTimeMillis());

            LocalDateTime ldt = Instant.ofEpochMilli(newOrder.getEntryDateTime())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            // 4
            double totalAmount = 0.0;

            // 5
            Customer customer = customerDao.getNewOrderInfoById(warehouseId, districtId, customerId);
            String lastName = customer.getLastName();
            String credit = customer.getCreditStatus();
            BigDecimal discount = customer.getDiscountRate();
            BigDecimal warehouseTax = warehouseDao.getWarehouseTax(warehouseId).getTax();

            PreparedStatement p = connection.prepareStatement(orderLineDao.orderLineMultiRowsInsertTemplate());

            for (int i = 0; i < noOfItems; i++) {
                long itemId = newOrderLines.get(i).itemId;
                int supplyWarehouseId = newOrderLines.get(i).supplierWarehouseId;
                BigDecimal orderedQuantity = BigDecimal.valueOf(newOrderLines.get(i).quantity);

                Stock updatedStock = stockDao.updateAndGetStock(connection, warehouseId, itemId, districtId, supplyWarehouseId, orderedQuantity);
                Long sQuant = updatedStock.getQuantity().longValue();
                String sDist = getSdist(updatedStock, districtId);

                Item item = itemDao.getItemById(itemId);
                String itemName = item.getName();
                Double itemPrice = item.getPrice().doubleValue();

                double itemAmount = orderedQuantity.longValue() * itemPrice;
                totalAmount += itemAmount;

                p.setLong(1, districtNextOrderId);
                p.setInt(2, districtId);
                p.setInt(3, warehouseId);
                p.setInt(4, i+1);
                p.setLong(5, itemId);
                p.setInt(6, supplyWarehouseId);
                p.setLong(7, orderedQuantity.longValue());
                p.setDouble(8, itemAmount);
                p.setString(9, sDist);
                p.addBatch();

                output[i] = String.format("Item number: %d, name: %s, supplier warehouse: %d, quantity: %d, " +
                        "ol_amount: %f, s_quantity: %d\n", itemId, itemName, supplyWarehouseId, orderedQuantity.longValue(), itemAmount, sQuant);
            }

            p.execute();
            totalAmount = totalAmount * (1 + districtTax.doubleValue() + warehouseTax.doubleValue()) * (1 - discount.doubleValue());

            System.out.printf("Customer -> last name: %s, credit status: %s, discount: %f\n", lastName, credit, discount.doubleValue());
            System.out.printf("Warehouse tax: %f, district tax: %f\n", warehouseTax.doubleValue(), districtTax.doubleValue());
            System.out.printf("Order number: %d, entry date: %s\n", newOrder.getId(), ldt.toString());
            System.out.printf("Number of items: %d, total amount for order: %f\n", noOfItems, totalAmount);
            Arrays.stream(output).forEach(System.out::println);






            // 6



        } catch (Exception ex) {
            LOGGER.error("Rolling back transaction due to error: ", ex);
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
            throw ex;
        }


    }

    private String getSdist(Stock stock, int districtId) {
        switch (districtId) {
            case 1:
                return stock.getSDist01();
            case 2:
                return stock.getSDist02();
            case 3:
                return stock.getSDist03();
            case 4:
                return stock.getSDist04();
            case 5:
                return stock.getSDist05();
            case 6:
                return stock.getSDist06();
            case 7:
                return stock.getSDist07();
            case 8:
                return stock.getSDist08();
            case 9:
                return stock.getSDist09();
            case 10:
                return stock.getSDist10();

            default:
                return "";
        }
    }

    @Override
    public String getType() {
        return "New Order";
    }

}
