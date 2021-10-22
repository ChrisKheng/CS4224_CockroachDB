package cs4224.transactions;

import java.util.List;
import java.util.stream.Collectors;

public class StockLevelTransaction extends BaseTransaction {

    public StockLevelTransaction() {
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int districtId = Integer.parseInt(parameters[2]);
        final int threshold = Integer.parseInt(parameters[3]);
        final int noOfOrders = Integer.parseInt(parameters[4]);

    }
}
