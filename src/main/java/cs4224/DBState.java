package cs4224;

import com.google.inject.Inject;
import cs4224.dao.*;

public class DBState {

    private static final String STATS_FILE = "dbstate.csv";

    private final WarehouseDao warehouseDao;
    private final DistrictDao districtDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final OrderLineDao orderLineDao;
    private final StockDao stockDao;

    @Inject
    public DBState(WarehouseDao warehouseDao, DistrictDao districtDao, CustomerDao customerDao,
                   OrderDao orderDao, OrderLineDao orderLineDao, StockDao stockDao) {
        this.warehouseDao = warehouseDao;
        this.districtDao = districtDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.orderLineDao = orderLineDao;
        this.stockDao = stockDao;
    }

    public void save() {

    }
}
