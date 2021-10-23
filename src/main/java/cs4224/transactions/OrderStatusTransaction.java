package cs4224.transactions;

public class OrderStatusTransaction extends BaseTransaction {

    public OrderStatusTransaction() {
    }

    @Override
    public void execute(String[] dataLines,  String[] parameters) throws Exception {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int districtId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);
    }

    @Override
    public String getType() {
        return "Order Status";
    }
}
