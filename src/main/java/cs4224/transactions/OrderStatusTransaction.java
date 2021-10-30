package cs4224.transactions;

import javax.sql.DataSource;
import cs4224.dao.OrderDao;
import cs4224.entities.Customer;
import cs4224.entities.Order;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class OrderStatusTransaction extends BaseTransaction {

PreparedStatement getCustomerInfo;
PreparedStatement getCustLastOrder;
PreparedStatement getItemInfoFromLastOrder;


    public OrderStatusTransaction(final CqlSession session) {
        super(session);
        this.getCustomerInfo = = session.prepare("SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE " +
                "FROM customer " +
                "WHERE C_W_ID = :c_w_id and C_D_ID = :c_d_id and C_ID = :c_id");

        this.getCustLastOrder = session.prepare("SELECT O_ID, O_ENTRY_D, O_CARRIER_ID FROM order_by_customer " +
                "WHERE C_W_ID = :c_w_id and C_D_ID = :c_d_id and C_ID = :c_id " +
                "ORDER BY O_ID DESC LIMIT 1");

        this.getItemInfoFromLastOrder = session.prepare("SELECT OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D " +
                "FROM order_line " +
                "WHERE OL_W_ID = :ol_w_id and OL_D_ID = :ol_d_id and OL_O_ID = :ol_o_id");

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
            private static final String queryCustInfo;
            queryCustInfo = String.format("SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE " +
                    "FROM customer " +
                    "WHERE C_W_ID = %d and C_D_ID = %d and C_ID = %d", warehouseId, districtId, customerId);

            //OrderDao
            private static final String queryCustLastOrder;
            queryCustLastOrder = String.format("SELECT O_ID, O_ENTRY_ID, O_CARRIER_ID FROM orders " +
                    "WHERE C_W_ID = %d and C_D_ID = %d and C_ID = %d" +
                    "ORDER BY O_ID DESC LIMIT 1 ALLOW FILTERING", warehouseId, districtId, customerId); // should we allow filtering for O_ID?


//step 1 :


            Row customer = session.execute(getCustomerInfo.boundStatementBuilder()
                    .setInt("c_w_id", warehouseId)
                    .setInt("c_d_id", districtId)
                    .setInt("c_id", customerId)
                    .build()
            ).one();

            if (customer != null) {
                // 1
                String first = customer.getString("C_FIRST");
                String middle = customer.getString("C_MIDDLE");
                String last = customer.getString("C_LAST");
                BigDecimal balance = customer.getBigDecimal("C_BALANCE").doubleValue();

                //step 2 :

                Row lastOrder = session.execute(getCustLastOrder.boundStatementBuilder()
                        .setInt("c_w_id", warehouseId)
                        .setInt("c_d_id", districtId)
                        .setInt("c_id", customerId)
                        .build()
                ).one();

                if (lastOrder !null)
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

                    //OrderLineDao
                    private static final String queryItemInfoFromLastOrder;
                    queryItemInfoFromLastOrder = String.format("SELECT OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D " +
                            "FROM order_line WHERE OL_W_ID = %d and OL_D_ID = %d and OL_O_ID = %d", warehouseId, districtId, ordernum);

                    List<Row> items = session.execute(getItemInfoFromLastOrder
                            .boundStatementBuilder()
                            .setInt("ol_w_id", warehouseId)
                            .setInt("ol_d_id", districtId)
                            .setInt("ol_o_id", ordernum)
                            .build()
                    ).all();

                    for (Row i : items) {
                        int itemId = i.getInt("OL_I_ID");
                        int supplyWarehouseId = i.getInt("OL_SUPPLY_W_ID");
                        BigDecimal quantity = i.getBigDecimal("OL_QUANTITY");
                        BigDecimal amount = i.getBigDecimal("OL_AMOUNT");
                        date DeliveryDate = i.getTimestamp("OL_DELIVERY_ID");

                        //print out info for each item in last cust order
                        System.out.printf("Item ID: " + itemID);
                        System.out.printf("Warehouse number: " + supplyWarehouseId);
                        System.out.printf("Quantity ordered: " + quantity);
                        System.out.printf("Total cost of item: " + amount);
                        System.out.printf("Data and time of delivery: " + DeliveryDate);
                        System.out.println("\n\n");
                    }
                }
                else
                {
                    System.out.printf("Customer order is incomplete");
                }
            } else {
                System.out.printf("Customer ID not found");
            }
        }

            }
