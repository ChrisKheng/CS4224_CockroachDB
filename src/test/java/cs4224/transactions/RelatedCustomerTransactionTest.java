package cs4224.transactions;

import cs4224.entities.Customer;
import cs4224.extensions.InitializationExtension;
import cs4224.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

@ExtendWith({InitializationExtension.class})
public class RelatedCustomerTransactionTest {
    @BeforeAll
    public static void setup() {
        String createTableCommand = String.format("cockroach sql --insecure -u %s -f src/test/resources/create_table.sql",
                InitializationExtension.username);
        Utils.executeBashCommand(createTableCommand);

        String createIndexCommand = String.format("cockroach sql --insecure -u %s -f src/test/resources/create_index.sql",
                InitializationExtension.username);
        Utils.executeBashCommand(createIndexCommand);

        String insertDataCommand = String.format("cockroach sql --insecure -u %s -f " +
                        "src/test/resources/test_data/related_customer_transaction/insert_data.sql",
                InitializationExtension.username);
        Utils.executeBashCommand(insertDataCommand);
    }

    @Test
    public void testExecuteHasRelatedCustomer() throws Exception {
        RelatedCustomerTransaction transaction = new RelatedCustomerTransaction(InitializationExtension.customerDao);

        List<Customer> relatedCustomers = transaction.executeAndGetResult(1l, 1l, 1l);
        List<Customer> expectedResult = new ArrayList<>(Arrays.asList(
                Customer.builder().warehouseId(2l).districtId(2l).id(2l).build(),
                Customer.builder().warehouseId(5l).districtId(3l).id(3l).build()
        ));

        Assertions.assertEquals(expectedResult, relatedCustomers);
    }
}
