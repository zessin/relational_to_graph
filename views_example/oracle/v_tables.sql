create or replace view v_tables as
  select owner      table_schema,
         table_name table_name
  from   all_tables;