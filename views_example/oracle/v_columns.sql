create or replace view v_columns as
  select owner       table_schema,
         table_name  table_name,
         column_name column_name
  from   all_tab_columns;