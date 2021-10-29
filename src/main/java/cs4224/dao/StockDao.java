package cs4224.dao;

import cs4224.entities.Stock;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class StockDao {
    private final DbQueryHelper dbQueryHelper;
    private final String schema;

    public StockDao(final DbQueryHelper dbQueryHelper, final String schema) {
        this.dbQueryHelper = dbQueryHelper;
        this.schema = schema;
    }

    public long getCountOfLowStockQuantities(long warehouseId, long[] itemIds, long threshold)
            throws SQLException {
        final ScalarHandler<Long> handler = new ScalarHandler<>();
        final String query = String.format("SELECT COUNT(S_QUANTITY) FROM %s.stock WHERE S_W_ID = ? " +
                "AND S_I_ID = ANY(?) AND S_QUANTITY < ?", schema);
        return dbQueryHelper.getQueryRunner().query(query, handler, warehouseId, itemIds, threshold);
    }

    public Stock getState(long warehouseId) {
        try {
            final String query = String.format("SELECT sum(S_QUANTITY) as S_QUANTITY, sum(S_YTD) as S_YTD, " +
                    "sum(S_ORDER_CNT) as S_ORDER_CNT, sum(S_REMOTE_CNT) as S_REMOTE_CNT FROM %s.stock WHERE S_W_ID = ?", schema);
            final List<Stock> stocks = dbQueryHelper.getQueryResult(query, Stock.class, warehouseId);
            return stocks.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Stock();
        }
    }
}
