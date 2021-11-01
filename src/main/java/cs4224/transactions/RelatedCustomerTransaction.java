package cs4224.transactions;

import cs4224.dao.CustomerDao;
import cs4224.entities.Customer;

import java.sql.SQLException;
import java.util.List;

public class RelatedCustomerTransaction extends BaseTransaction {
    private final CustomerDao customerDao;

    public RelatedCustomerTransaction(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final long customerWarehouseId = Long.parseLong(parameters[1]);
        final long customerDistrictId = Long.parseLong(parameters[2]);
        final long customerId = Long.parseLong(parameters[3]);

        List<Customer> relatedCustomers = executeAndGetResult(customerWarehouseId, customerDistrictId, customerId);
        printOutput(relatedCustomers);
    }

    public List<Customer> executeAndGetResult(long customerWarehouseId, long customerDistrictId, long customerId)
            throws SQLException {
        return customerDao.getRelatedCustomer(customerWarehouseId, customerDistrictId, customerId);
    }

    private void printOutput(List<Customer> relatedCustomers) {
        System.out.printf("Number of relatedCustomers: %d\n", relatedCustomers.size());
        System.out.printf("Related customers (C_W_ID, C_D_ID, C_ID):");
        int count = 1;
        for (Customer customer : relatedCustomers) {
            if (count == relatedCustomers.size()) {
                System.out.printf(" %s", customer.toSpecifier());
            } else {
                System.out.printf(" %s,", customer.toSpecifier());
            }
            count++;
        }
        System.out.printf("\n");
    }

    @Override
    public String getType() {
        return "Related Customer";
    }
}
