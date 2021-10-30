USE wholesaledb;

-- Index on orders that have not been delivered yet
DROP INDEX IF EXISTS wholesale.orders@undelivered_orders CASCADE;
CREATE INDEX undelivered_orders ON wholesale.orders (O_CARRIER_ID) WHERE O_CARRIER_ID = -1;

-- O_C_ID only will do because O_W_ID and O_D_ID are part of the primary index.
DROP INDEX IF EXISTS wholesale.orders@order_by_customer CASCADE;
CREATE INDEX order_by_customer ON wholesale.orders (O_C_ID);

DROP INDEX IF EXISTS wholesale.order_line@order_by_item CASCADE;
CREATE INDEX order_by_item ON wholesale.order_line (OL_I_ID);

DROP INDEX IF EXISTS wholesale.customer@customer_balance CASCADE;
CREATE INDEX customer_balance ON wholesale.customer (C_BALANCE DESC) STORING (C_FIRST, C_MIDDLE, C_LAST);
