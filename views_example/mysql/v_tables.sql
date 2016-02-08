create or replace view v_tables as
  SELECT table_schema,
         table_name
  FROM   information_schema.tables
  WHERE  upper(table_type) = 'BASE TABLE'
  ORDER BY table_name;