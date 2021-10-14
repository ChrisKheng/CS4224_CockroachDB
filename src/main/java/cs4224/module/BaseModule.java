package cs4224.module;


import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import cs4224.transactions.*;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.net.InetSocketAddress;

import static cs4224.utils.Constants.*;

public class BaseModule extends AbstractModule {
    private final String keyspace;
    private final String ip;
    private final int port;

    public BaseModule(String keyspace, String ip, int port) {
        this.keyspace = keyspace;
        this.ip = ip.equals("") ? "localhost" : ip;
        this.port = port == -1 ? 26257 : port;
    }

    @Override
    protected void configure() {
    }


    @Provides
    @Singleton
    public DataSource provideDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{ip});
        ds.setPortNumbers(new int[]{port});
        ds.setDatabaseName(keyspace);
        ds.setUser("root");
        ds.setSsl(false);
        ds.setSslMode("disable");
        ds.setReWriteBatchedInserts(true); // add `rewriteBatchedInserts=true` to pg connection string
        ds.setApplicationName("Wholesale");

        return ds;
    }
    @Provides
    @Inject
    public PaymentTransaction providePaymentTransaction(DataSource ds) {
        return new PaymentTransaction(ds);
    }

    @Provides
    @Inject
    public NewOrderTransaction provideNewOrderTransaction(DataSource ds) {
        return new NewOrderTransaction(ds);
    }

    @Provides
    @Inject
    public DeliveryTransaction provideDeliveryTransaction(DataSource ds) {
        return new DeliveryTransaction(ds);
    }

    @Provides
    @Inject
    public OrderStatusTransaction provideOrderStatusTransaction(DataSource ds) {
        return new OrderStatusTransaction(ds);
    }

    @Provides
    @Inject
    public PopularItemTransaction providePopularItemTransaction(DataSource ds) {
        return new PopularItemTransaction(ds);
    }

    @Provides
    @Inject
    public RelatedCustomerTransaction provideRelatedCustomerTransaction(DataSource ds) {
        return new RelatedCustomerTransaction(ds);
    }

    @Provides
    @Inject
    public TopBalanceTransaction provideTopBalanceTransaction(DataSource ds) {
        return new TopBalanceTransaction(ds);
    }

    @Provides
    @Inject
    public StockLevelTransaction provideStockLevelTransaction(DataSource ds) {
        return new StockLevelTransaction(ds);
    }

}


