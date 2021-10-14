package cs4224.transactions;


import cs4224.ParallelExecutor;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class PaymentTransaction extends BaseTransaction {


    public PaymentTransaction(DataSource ds) {
        super(ds);
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int customerWarehouseId = Integer.parseInt(parameters[1]);
        final int customerDistrictId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);
        final double paymentAmount = Double.parseDouble(parameters[4]);
    }
}
