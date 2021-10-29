package cs4224.module;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cs4224.dao.*;
import cs4224.transactions.*;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;

public class BaseModule extends AbstractModule {
    private final String database;
    private final String ip;
    private final int port;
    private final String password;
    private final String user;
    private final String schema;

    public BaseModule(String database, String ip, int port, String password, String user) {
        this.database = database;
        this.ip = ip.equals("") ? "localhost" : ip;
        this.port = port == -1 ? 26257 : port;
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
        HikariConfig config = new HikariConfig();
        String url = String.format("jdbc:postgresql://%s:%s/%s", ip, port, database);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("ssl", "true");
        config.addDataSourceProperty("sslmode", "require");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        config.setMaximumPoolSize(40);
        config.setKeepaliveTime(150000);

        HikariDataSource ds = new HikariDataSource(config);

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
    @Singleton
    public DbQueryHelper provideQueryResultToEntityMapper(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new DbQueryHelper(queryRunner, objectMapper);
    }

    @Provides
    @Inject
    public CustomerDao provideCustomerDao(DbQueryHelper queryResultToEntityMapper) {
        return new CustomerDao(queryResultToEntityMapper, schema);
    }

    @Provides
    @Inject
    public DistrictDao provideDistrictDao(DbQueryHelper queryResultToEntityMapper) {
        return new DistrictDao(queryResultToEntityMapper, schema);
    }

    @Provides
    @Inject
    public ItemDao provideItemDao(DbQueryHelper queryResultToEntityMapper, ObjectMapper objectMapper,
                                  QueryRunner queryRunner) {
        return new ItemDao(queryResultToEntityMapper, objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public OrderByItemDao provideOrderByItemDao(DbQueryHelper queryResultToEntityMapper) {
        return new OrderByItemDao(queryResultToEntityMapper, schema);
    }

    @Provides
    @Inject
    public OrderDao provideOrderDao(DbQueryHelper queryResultToEntityMapper, ObjectMapper objectMapper,
                                    QueryRunner queryRunner) {
        return new OrderDao(queryResultToEntityMapper, objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public OrderLineDao provideOrderLineDao(DbQueryHelper queryResultToEntityMapper, ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderLineDao(queryResultToEntityMapper, objectMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public WarehouseDao provideWarehouseDao(DbQueryHelper queryResultToEntityMapper, QueryRunner queryRunner) {
        return new WarehouseDao(queryResultToEntityMapper, queryRunner, schema);
    }

    @Provides
    @Inject
    public PaymentTransaction providePaymentTransaction(WarehouseDao warehouseDao, DistrictDao districtDao,
                                                        DbQueryHelper queryResultToEntityMapper,
                                                        CustomerDao customerDao) {
        return new PaymentTransaction(warehouseDao, districtDao, customerDao, queryResultToEntityMapper);
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
    public OrderStatusTransaction provideOrderStatusTransaction(WarehouseDao warehouseDao, DistrictDao districtDao,
                                                                CustomerDao customerDao) {
        return new OrderStatusTransaction(warehouseDao, districtDao, customerDao);
    }

    @Provides
    @Inject
    public PopularItemTransaction providePopularItemTransaction(DistrictDao districtDao, CustomerDao customerDao,
                                                                OrderDao orderDao, OrderLineDao orderLineDao,
                                                                ItemDao itemDao) {
        return new PopularItemTransaction(districtDao, customerDao, orderDao, orderLineDao, itemDao);
    }

    @Provides
    @Inject
    public RelatedCustomerTransaction provideRelatedCustomerTransaction(OrderDao orderDao, OrderLineDao orderLineDao,
                                                                        OrderByItemDao orderByItemDao) {
        return new RelatedCustomerTransaction(orderDao, orderLineDao, orderByItemDao);
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


