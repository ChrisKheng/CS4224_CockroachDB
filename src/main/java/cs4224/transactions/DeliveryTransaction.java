package cs4224.transactions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DeliveryTransaction extends BaseTransaction{


    private static final int NO_OF_DISTRICTS = 10;
    private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public DeliveryTransaction(DataSource ds) {
        super(ds);
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {
        final int warehouseId = Integer.parseInt(parameters[1]);
        final int carrierId = Integer.parseInt(parameters[2]);
        try (Connection connection = dataSource.getConnection()) {
            ResultSet res = connection.createStatement()
                    .executeQuery(String.format("SELECT balance FROM accounts WHERE id = '%s'", id.toString()));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
