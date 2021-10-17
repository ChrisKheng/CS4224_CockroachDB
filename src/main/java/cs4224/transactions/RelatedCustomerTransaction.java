package cs4224.transactions;

import java.util.HashSet;

public class RelatedCustomerTransaction extends BaseTransaction {


    public RelatedCustomerTransaction() {


    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int customerWarehouseId = Integer.parseInt(parameters[1]);
        final int customerDistrictId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);


    }
}
