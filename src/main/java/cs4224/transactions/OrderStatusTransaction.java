package cs4224.transactions;

import javax.sql.DataSource;

public class OrderStatusTransaction extends BaseTransaction {

    public OrderStatusTransaction(final DataSource ds) {
        super(ds);


    }

    @Override
    public void execute(String[] dataLines,  String[] parameters) {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int districtId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);
    }
}
