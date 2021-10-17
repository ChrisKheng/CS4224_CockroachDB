package cs4224.module;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import cs4224.dao.*;
import cs4224.transactions.*;
import org.apache.commons.dbutils.QueryRunner;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.SslMode;

import javax.sql.DataSource;
import java.nio.file.Paths;

public class BaseModule extends AbstractModule {
    private final String keyspace;
    private final String ip;
    private final int port;
    private final String certPath;
    private final String password;
    private final String user;
    private final String schema;

    public BaseModule(String keyspace, String ip, int port, String certPath, String password, String user) {
        this.keyspace = keyspace;
        this.ip = ip.equals("") ? "localhost" : ip;
        this.port = port == -1 ? 26257 : port;
        this.certPath = certPath;
        this.password = password;
        this.user = user;
        this.schema = "wholesale";
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
        ds.setPassword(password);
        ds.setUser(user);
        ds.setSslMode(SslMode.VERIFY_FULL.value);
        ds.setSslRootCert(Paths.get(certPath, "ca.crt").toString());
        ds.setSslCert(Paths.get(certPath,"client.root.crt").toString());
        ds.setSslKey(Paths.get(certPath, "client.root.key").toString());
        ds.setReWriteBatchedInserts(true); // add `rewriteBatchedInserts=true` to pg connection string
        ds.setApplicationName("Wholesale");
        return ds;
    }

    @Provides
    @Singleton
    public QueryRunner provideQueryRunner(DataSource ds) {
        return new QueryRunner(ds);
    }

    @Provides
    public ObjectMapper provideObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Provides
    @Inject
    public CustomerDao provideCustomerDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new CustomerDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public DistrictDao provideDistrictDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new DistrictDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public ItemDao provideItemDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new ItemDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public OrderByItemDao provideOrderByItemDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderByItemDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public OrderDao provideOrderDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public OrderLineDao provideOrderLineDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderLineDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public WarehouseDao provideWarehouseDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new WarehouseDao(objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public PaymentTransaction providePaymentTransaction(WarehouseDao warehouseDao, DistrictDao districtDao,
                                                        CustomerDao customerDao) {
        return new PaymentTransaction(warehouseDao, districtDao, customerDao);
    }

    @Provides
    @Inject
    public NewOrderTransaction provideNewOrderTransaction() {
        return new NewOrderTransaction();
    }

    @Provides
    @Inject
    public DeliveryTransaction provideDeliveryTransaction() {
        return new DeliveryTransaction();
    }

    @Provides
    @Inject
    public OrderStatusTransaction provideOrderStatusTransaction() {
        return new OrderStatusTransaction();
    }

    @Provides
    @Inject
    public PopularItemTransaction providePopularItemTransaction(DistrictDao districtDao, CustomerDao customerDao,
                                                                OrderDao orderDao, OrderLineDao orderLineDao,
                                                                ItemDao itemDao, OrderByItemDao orderByItemDao) {
        return new PopularItemTransaction(districtDao, customerDao, orderDao, orderLineDao, itemDao, orderByItemDao);
    }

    @Provides
    @Inject
    public RelatedCustomerTransaction provideRelatedCustomerTransaction() {
        return new RelatedCustomerTransaction();
    }

    @Provides
    @Inject
    public TopBalanceTransaction provideTopBalanceTransaction() {
        return new TopBalanceTransaction();
    }

    @Provides
    @Inject
    public StockLevelTransaction provideStockLevelTransaction() {
        return new StockLevelTransaction();
    }

}


