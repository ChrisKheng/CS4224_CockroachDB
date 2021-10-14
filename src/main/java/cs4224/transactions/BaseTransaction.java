package cs4224.transactions;


import javax.sql.DataSource;

public abstract class BaseTransaction {
    protected final DataSource dataSource;

    public BaseTransaction(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract void execute(final String[] dataLines, final String[] parameters);
}
