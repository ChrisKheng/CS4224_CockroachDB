package cs4224.transactions;

import javax.sql.DataSource;
import cs4224.dao.OrderDao;
import cs4224.entities.Customer;
import cs4224.entities.Order;

public class OrderStatusTransaction extends BaseTransaction {

    public OrderStatusTransaction(final CqlSession session) {
        super(session);
    }


    //private final OrderDao orderDao;
    private final WarehouseDao warehouseDao;
    private final DistrictDao districtDao;
    private final CustomerDao customerDao;

    public OrderStatusTransaction(WarehouseDao warehouseDao, DistrictDao districtDao, CustomerDao customerDao) {
        this.warehouseDao = warehouseDao;
        this.districtDao = districtDao;
        this.customerDao = customerDao;


    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final long warehouseId = Long.parseLong(parameters[1]);
        final long districtId = Long.parseLong(parameters[2]);
        final long customerId = Long.parseLong(parameters[3]);

        /*
         1) Get customer info :c_first,c_middle, c_last, c_balance from customer dao
         2) Query order table for customer's last order by filtering to latest order for O_ID,O_ENTRY_ID,O_CARRIER_ID
         3) Query the items in the customer last order. This is from order_line dao?. Retrieve OL_I_ID,OL_SUPPLY_W_ID,OL_QUANTITIY,OL_AMOUNT, OL_DELIVERY_D
         */

        //CustomerDao
        private static final String query1;
                = "SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE " +
                "FROM customer " +
        "WHERE C_W_ID = %d and C_D_ID = %d and C_ID = %d");

        //OrderDao
        private static final String query2;
                 ="SELECT O_ID, O_ENTRY_ID, O_CARRIER_ID FROM orders " +
                "WHERE O_W_ID = %d and O_D_ID = %d and O_C_ID = %d" +
            "ORDER BY O_ID DESC LIMIT 1 ALLOW FILTERING");

        //OrderLineDao
        private static final String query3;
                ="SELECT OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D " +
                 "FROM order_line WHERE OL_W_ID = %d and OL_D_ID = %d and OL_O_ID = %d"

//step 1 :
        String getCustInfoBal () = String.format(query1,  warehouseId, districtId, customerId);

        Row customer = session.execute(getCustInfoBal).one();

            if (customer != null) {
                // 1
                String first = customer.getString("C_FIRST");
                String middle = customer.getString("C_MIDDLE");
                String last = customer.getString("C_LAST");
                BigDecimal balance = customer.getBigDecimal("C_BALANCE").doubleValue();

                //step 2 :
                String getCustLastOrder () = String.format(query2, warehouseId, districtId, customerId);

                Row lastOrder = session.execute(getCustLastOrder).one();

                if (lastOrder!null)
                {
                    int ordernum = lastOrder.getInt("O_ID");
                    Date entry_d = lastOrder.getTimestamp("O_ENTRY_D").toString();
                    int carrier_id = lastOrder.getInt("C_CARRIER_ID");

                    //print out last order customer info nd last order info
                    System.out.printf("Customer's first: %s, middle: %s, last: %s \nBalance: %f \n", first, middle, last, balance);
                    System.out.printf("Last order's ID: %d \n",
                            ordernum);
                    System.out.printf("Last order's entry data and time is: %s \n",
                            entry_d);
                    System.out.printf("Last order's carrier identifier id is: %d \n",
                            carrier_id);
                    System.out.printf("---------Item info is given below----------");

                }

            }
