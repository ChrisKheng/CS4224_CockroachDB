package cs4224.transactions;

import cs4224.dao.*;

public class StockLevelTransaction extends BaseTransaction {
    private final DistrictDao districtDao;
    private final OrderLineDao orderLineDao;
    private final StockDao stockDao;

    public StockLevelTransaction(DistrictDao districtDao, OrderLineDao orderLineDao, StockDao stockDao) {
        this.districtDao = districtDao;
        this.orderLineDao = orderLineDao;
        this.stockDao = stockDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final long warehouseId = Long.parseLong(parameters[1]);
        final long districtId = Long.parseLong(parameters[2]);
        final long threshold = Long.parseLong(parameters[3]);
        final long numberOfOrders = Long.parseLong(parameters[4]);

        final long districtNextOrderId = districtDao.getNextOrderId(warehouseId, districtId).getNextOrderId();

        final long[] matchingOrderLineItemIds = orderLineDao
                .getOLItemIds(warehouseId, districtId,
                        districtNextOrderId - numberOfOrders, districtNextOrderId - 1)
                .stream()
                .mapToLong(id -> id)
                .toArray();

        final long count = stockDao.getCountOfLowStockQuantities(warehouseId, matchingOrderLineItemIds, threshold);

        System.out.printf("Number of items with stock quantities below the threshold: %d%n", count);
    }

    @Override
    public String getType() {
        return "Stock Level";
    }
}
