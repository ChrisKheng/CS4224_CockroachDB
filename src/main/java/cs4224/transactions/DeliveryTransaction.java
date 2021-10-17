package cs4224.transactions;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DeliveryTransaction extends BaseTransaction{


    private static final int NO_OF_DISTRICTS = 10;
    private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public DeliveryTransaction() {
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int carrierId = Integer.parseInt(parameters[2]);

    }
}
