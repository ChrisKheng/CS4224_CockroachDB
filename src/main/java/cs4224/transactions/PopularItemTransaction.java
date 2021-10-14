package cs4224.transactions;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PopularItemTransaction extends BaseTransaction {

    public PopularItemTransaction(DataSource ds) {
        super(ds);

    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int districtId = Integer.parseInt(parameters[2]);
        final int L = Integer.parseInt(parameters[3]);

    }
}
