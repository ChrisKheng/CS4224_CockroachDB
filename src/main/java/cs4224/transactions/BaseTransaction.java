package cs4224.transactions;


public abstract class BaseTransaction {

    public BaseTransaction() {

    }

    public abstract void execute(final String[] dataLines, final String[] parameters) throws Exception;
}
