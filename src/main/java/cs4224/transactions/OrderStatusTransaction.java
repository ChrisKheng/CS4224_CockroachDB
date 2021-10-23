package cs4224.transactions;

import javax.sql.DataSource;
import cs4224.dao.OrderDao;
import cs4224.entities.Customer;
import cs4224.entities.Order;

public class OrderStatusTransaction extends BaseTransaction {

    //private final OrderDao orderDao;
    private final WarehouseDao warehouseDao;
    private final DistrictDao districtDao;
    private final CustomerDao customerDao;

    public OrderStatusTransaction(WarehouseDao warehouseDao, DistrictDao districtDao, CustomerDao customerDao) {
        this.warehouseDao = warehouseDao;
        this.districtDao = districtDao;
        this.customerDao = customerDao;
    }

    @Override
    public void execute(String[] dataLines,  String[] parameters) throws Exception {
        final long warehouseId = Long.parseLong(parameters[1]);
        final long districtId = Long.parseLong(parameters[2]);
        final long customerId = Long.parseLong(parameters[3]);

        /*
         1) Get customer info :c_first,c_middle, c_last, c_balance from customer dao
         2) Query order table for customer's last order by filtering to latest order for O_ID,O_ENTRY_ID,O_CARRIER_ID
         3) Query the items in the customer last order. This is from order_line dao?. Retrieve OL_I_ID,OL_SUPPLY_W_ID,OL_QUANTITIY,OL_AMOUNT, OL_DELIVERY_D
         */
    }
}
