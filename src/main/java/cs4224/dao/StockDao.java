package cs4224.dao;

import cs4224.entities.Stock;

import java.sql.SQLException;
import java.util.List;

public class StockDao {
    private final DbQueryHelper queryResultToEntityMapper;
    private final String schema;

    public StockDao(DbQueryHelper queryResultToEntityMapper, String schema) {

        this.queryResultToEntityMapper = queryResultToEntityMapper;
        this.schema = schema;
    }

    public Stock getState(long warehouseId) {
        try {
            final String query = String.format("SELECT sum(S_QUANTITY) as S_QUANTITY, sum(S_YTD) as S_YTD, " +
                    "sum(S_ORDER_CNT) as S_ORDER_CNT, sum(S_REMOTE_CNT) as S_REMOTE_CNT FROM %s.stock WHERE S_W_ID = ?", schema);
            final List<Stock> stocks = queryResultToEntityMapper.getQueryResult(query, Stock.class, warehouseId);
            return stocks.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Stock();
        }
    }
}
