create or replace view v_constraints as
  /*This view is returning wrong results (such as duplicated constraints) and must be analysed*/
  SELECT tc.constraint_schema table_schema,
         tc.constraint_name   constraint_name,
         CASE tc.constraint_type
           WHEN 'PRIMARY KEY' THEN
              'PRIMARY_KEY'
           WHEN 'FOREIGN KEY' THEN
             'FOREIGN_KEY'
           WHEN 'UNIQUE' THEN
             'UNIQUE_KEY'
           WHEN 'CHECK' THEN
             'CHECK'
           ELSE
             NULL
         END                  constraint_type,
         tc.table_name        table_name,
         kcu.column_name      column_name,
         ccu.table_name       referenced_table_name,
         ccu.column_name      referenced_column_name
  FROM   information_schema.table_constraints tc
  JOIN   information_schema.key_column_usage  kcu
  ON     tc.constraint_name = kcu.constraint_name
  JOIN   information_schema.constraint_column_usage ccu
  ON     ccu.constraint_name = tc.constraint_name
  ORDER BY tc.table_name, tc.constraint_type;