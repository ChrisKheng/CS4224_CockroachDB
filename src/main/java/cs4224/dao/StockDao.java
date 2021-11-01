package cs4224.dao;

import cs4224.entities.Order;
import cs4224.entities.Stock;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.Connection;
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

    public Stock updateAndGetStock(Connection connection, long warehouseId, long itemId, long districtId,
                                   long supplyWarehouseId, BigDecimal quantityOrdered) throws SQLException {
        String paddedSDist = String.format("S_DIST_%02d", districtId);

        final String query = String.format("UPDATE %s.stock SET " +
                "S_QUANTITY = IF(S_QUANTITY - ? < 10, S_QUANTITY - ? + 100, S_QUANTITY - ?)," +
                "S_YTD = S_YTD + ?," +
                "S_ORDER_CNT = S_ORDER_CNT + 1," +
                "S_REMOTE_CNT = S_REMOTE_CNT + ? " +
                "WHERE (S_W_ID, S_I_ID) = (?, ?) " +
                "RETURNING S_QUANTITY, %s", schema, paddedSDist);

        return dbQueryHelper.getQueryResult(connection, query, Stock.class, quantityOrdered, quantityOrdered,
                quantityOrdered, quantityOrdered, warehouseId == supplyWarehouseId ? 0 : 1, warehouseId, itemId).get(0);
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
