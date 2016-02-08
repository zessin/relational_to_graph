create or replace view v_constraints as
  SELECT k.table_schema,
         k.constraint_name,
         CASE c.constraint_type
           WHEN 'PRIMARY KEY' THEN
             'PRIMARY_KEY'
           WHEN 'FOREIGN KEY' THEN
             'FOREIGN_KEY'
           WHEN 'UNIQUE' THEN
             'UNIQUE_KEY'
         END constraint_type,
         k.table_name,
         k.column_name,
         k.referenced_table_name,
         k.referenced_column_name
  FROM   information_schema.key_column_usage  k,
         information_schema.table_constraints c
  WHERE  k.constraint_name = c.constraint_name AND
         k.table_name      = c.table_name
  ORDER BY table_name, constraint_type;