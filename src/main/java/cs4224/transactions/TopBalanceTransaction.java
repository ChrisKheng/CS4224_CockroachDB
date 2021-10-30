package cs4224.transactions;


import cs4224.dao.CustomerDao;

public class TopBalanceTransaction extends BaseTransaction {
    private final CustomerDao customerDao;

    public TopBalanceTransaction(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        customerDao.getTopBalance()
                .forEach(customer ->
                        System.out.printf(
                                "Name of customer: %s%n" +
                                        "Balance of customer's outstanding payment: %.2f%n" +
                                        "Warehouse name of customer: %s%n" +
                                        "District name of customer: %s%n%n",
                                String.join(
                                        " ",
                                        customer.getFirstName(),
                                        customer.getMiddleName(),
                                        customer.getLastName()
                                ),
                                customer.getBalance(),
                                customer.getWarehouseName(),
                                customer.getDistrictName()
                        )
                );
    }

    @Override
    public String getType() {
        return "Top Balance";
    }
}
