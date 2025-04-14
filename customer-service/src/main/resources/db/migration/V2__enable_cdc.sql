-- Enable logical replication
ALTER SYSTEM SET wal_level = logical;

-- Create publication for outbox table
CREATE PUBLICATION outbox_publication FOR TABLE outbox;

-- Create replication slot
SELECT pg_create_logical_replication_slot('outbox_slot', 'pgoutput');

-- Grant necessary permissions
GRANT SELECT ON outbox TO postgres;
GRANT USAGE ON SCHEMA public TO postgres; 