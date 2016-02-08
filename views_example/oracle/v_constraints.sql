create or replace view v_constraints as
  SELECT colSrc.owner           table_schema,
         colSrc.constraint_name constraint_name,
         CASE conSrc.constraint_type
           WHEN 'P' THEN
             'PRIMARY_KEY'
           WHEN 'R' THEN
             'FOREIGN_KEY'
           WHEN 'U' THEN
             'UNIQUE_KEY'
           WHEN 'C' THEN
             'CHECK'
         END                    constraint_type,
         colSrc.table_name      table_name,
         colSrc.column_name     column_name,
         colRef.table_name      referenced_table_name,
         colRef.column_name     referenced_column_name
  FROM   all_constraints  conSrc,
         all_cons_columns colSrc,
         all_cons_columns colRef
  WHERE  conSrc.owner             = colSrc.owner              AND
         conSrc.constraint_name   = colSrc.constraint_name    AND
         conSrc.owner             = colRef.owner(+)           AND
         conSrc.r_constraint_name = colRef.constraint_name(+)
  ORDER BY colSrc.table_name, conSrc.constraint_type;