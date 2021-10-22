package cs4224.transactions;

import cs4224.dao.*;

public class PopularItemTransaction extends BaseTransaction {

    private final DistrictDao districtDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;
    private final ItemDao itemDao;
    private final OrderByItemDao orderByItemDao;

    public PopularItemTransaction(DistrictDao districtDao, CustomerDao customerDao, OrderDao orderDao,
                                  OrderLineDao orderLineDao, ItemDao itemDao, OrderByItemDao orderByItemDao) {
        this.districtDao = districtDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
        this.itemDao = itemDao;
        this.orderByItemDao = orderByItemDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int districtId = Integer.parseInt(parameters[2]);
        final int L = Integer.parseInt(parameters[3]);

    }
}
