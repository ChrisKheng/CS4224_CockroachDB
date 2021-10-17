package cs4224.dao;

import java.sql.Connection;

public class StockDao {
    private final Connection connection;

    public StockDao(final Connection connection) {
        this.connection = connection;
    }
}
