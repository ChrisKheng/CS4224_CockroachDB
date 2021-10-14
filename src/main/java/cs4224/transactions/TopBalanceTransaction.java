package cs4224.transactions;


import javax.sql.DataSource;

public class TopBalanceTransaction extends BaseTransaction {

    public TopBalanceTransaction(DataSource ds) {
        super(ds);
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) {

    }
}
