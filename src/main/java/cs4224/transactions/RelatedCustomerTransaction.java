package cs4224.transactions;

import javax.sql.DataSource;
import java.util.HashSet;

public class RelatedCustomerTransaction extends BaseTransaction {


    public RelatedCustomerTransaction(DataSource ds) {
        super(ds);


    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int customerWarehouseId = Integer.parseInt(parameters[1]);
        final int customerDistrictId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);


    }
}
