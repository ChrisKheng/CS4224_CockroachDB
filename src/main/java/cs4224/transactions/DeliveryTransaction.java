package cs4224.transactions;

import java.text.Format;
import java.text.SimpleDateFormat;

public class DeliveryTransaction extends BaseTransaction{


    private static final int NO_OF_DISTRICTS = 10;
    private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public DeliveryTransaction() {
    }

    @Override
    public void execute(String[] dataLins, String[] parameters) throws Exception {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int carrierId = Integer.parseInt(parameters[2]);

    }

    @Override
    public String getType() {
        return "Delivery";
    }
}
