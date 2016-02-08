create or replace view v_columns as
  SELECT table_schema,
         table_name,
         column_name
  FROM   information_schema.columns
  ORDER BY table_name;