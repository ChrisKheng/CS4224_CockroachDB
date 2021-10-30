USE wholesaledb;

-- Index on orders that have not been delivered yet
DROP INDEX IF EXISTS wholesale.orders@undelivered_orders CASCADE;
CREATE INDEX undelivered_orders ON wholesale.orders (O_CARRIER_ID) WHERE O_CARRIER_ID = -1;

DROP INDEX IF EXISTS wholesale.orders@order_by_customer CASCADE;
CREATE INDEX order_by_customer ON wholesale.orders (O_C_ID) STORING (O_ENTRY_D, O_CARRIER_ID);

DROP INDEX IF EXISTS wholesale.customer@customer_balance CASCADE;
CREATE INDEX customer_balance ON wholesale.customer (C_BALANCE DESC) STORING (C_FIRST, C_MIDDLE, C_LAST);
