package cs4224.transactions;


import javax.sql.DataSource;

public class NewOrderTransaction extends BaseTransaction {
    private int customerId;
    private int warehouseId;
    private int districtId;
    private int noOfItems;

    public NewOrderTransaction() {
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        customerId = Integer.parseInt(parameters[1]);
        warehouseId = Integer.parseInt(parameters[2]);
        districtId = Integer.parseInt(parameters[3]);
        noOfItems = Integer.parseInt(parameters[4]);

        System.out.printf("Running New Order Transaction with C_ID= %d, W_ID=%d, D_ID=%d, N=%d \n", customerId, warehouseId, districtId, noOfItems);
        for (String line : dataLines) {
            System.out.println(line);
        }
    }
}
