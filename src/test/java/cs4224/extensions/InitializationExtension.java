package cs4224.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cs4224.dao.*;
import cs4224.utils.Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.sql.DataSource;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * IMPORTANT:
 * This test initialisation assumes that you already have a locally running insecure CockroachDB instance.
 * It also assumes that a CockroachDB user called 'cs4224o' has been created.
 * This test is tested on MacOS platform.
 *
 * Step to start a local cluster: https://www.cockroachlabs.com/docs/stable/start-a-local-cluster.html
 * 1) cockroach start \
 * --insecure \
 * --store=node1 \
 * --listen-addr=localhost:26257 \
 * --http-addr=localhost:8080 \
 * --join=localhost:26257,localhost:26258,localhost:26259 \
 * --background
 *
 * 2) cockroach init --insecure --host=localhost:26257
 *
 * 3) grep 'node starting' node1/logs/cockroach.log -A 11
 *
 *
 * Step to create the user 'cs4224o'
 * 1) cockroach sql --insecure --host=localhost:26257
 * > CREATE USER cs4224o;
 * > GRANT admin TO cs4224o;
 */
public class InitializationExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static boolean started = false;
    private DataSource dataSource;
    public static QueryRunner queryRunner;
    public static OrderDao orderDao;
    public static OrderLineDao orderLineDao;
    public static OrderByItemDao orderByItemDao;
    public static final String username = "cs4224o";
    public static final String schema = "wholesale";
    private static final String database = "wholesaledb_test";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            context.getRoot().getStore(GLOBAL).put("InitializationExtension", this);
        }

        System.out.println("Initializing tests...");
        initialiseAttributes();
        System.out.println("Done initializing!");
    }

    private void initialiseAttributes() throws Exception {
        dataSource = getDataSource();
        queryRunner = getQueryRunner(dataSource);
        ObjectMapper mapper = getObjectMapper();
        orderDao = getOrderDao(mapper, queryRunner);
        orderLineDao = getOrderLineDao(mapper, queryRunner);
        orderByItemDao = getOrderByItemDao(mapper, queryRunner);
    }

    private DataSource getDataSource() throws Exception {
        HikariConfig config = new HikariConfig();
        String url = String.format("jdbc:postgresql://localhost:26257/%s", database);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword("");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        config.setAutoCommit(false);
        config.setMaximumPoolSize(40);
        config.setKeepaliveTime(150000);
        return new HikariDataSource(config);
    }

    private QueryRunner getQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    private ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private OrderByItemDao getOrderByItemDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderByItemDao(objectMapper, queryRunner, schema);
    }

    private OrderDao getOrderDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderDao(objectMapper, queryRunner, schema);
    }

    private OrderLineDao getOrderLineDao(ObjectMapper objectMapper, QueryRunner queryRunner) {
        return new OrderLineDao(objectMapper, queryRunner, schema);
    }

    @Override
    public void close() {
        System.out.println("Complete all tests!");
    }
}
