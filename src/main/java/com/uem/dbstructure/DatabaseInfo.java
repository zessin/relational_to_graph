package com.uem.dbstructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uem.dbconfig.DatabaseConnection;
import com.uem.util.ApplicationLogger;
import com.uem.util.PropertiesHelper;

/**
 * Provides all the methods needed to load the metadata from a relational database
 *
 * @author zessin
 */
public class DatabaseInfo {
    private final List<Table> tables;
    private final List<Column> columns;
    private final List<Constraint> constraints;
    private final DatabaseConnection databaseConnection;
    private final String schema;
    private ResultSet resultSet;

    /**
     * Initializes all the attributes and the schema based on the properties file
     */
    public DatabaseInfo() {
        super();
        tables = new ArrayList<>();
        columns = new ArrayList<>();
        constraints = new ArrayList<>();
        databaseConnection = DatabaseConnection.getConnection();
        schema = PropertiesHelper.getDatabaseSchema();
        resultSet = null;
    }

    /**
     * Queries and organizes all the metadata needed for the application
     * @throws SQLException When the metadata couldn't be queried for some reason
     */
    public void loadDatabaseInformation() throws SQLException {
        loadAllTables();
        loadAllColumns();
        loadAllConstraints();
        loadRelationshipTables();
    }

    /**
     * Queries and organizes all the tables metadata needed for the application
     * @throws SQLException When the tables metadata couldn't be queried for some reason
     */
    private void loadAllTables() throws SQLException {
        ApplicationLogger.info("Loading tables metadata for schema " + schema);

        final String tablesQuery = "SELECT table_name " +
                                   "FROM   " + schema + "." + PropertiesHelper.getTablesView() + " " +
                                   "WHERE  UPPER(table_schema) = UPPER('" + schema + "') " +
                                   "ORDER BY table_name";

        resultSet = databaseConnection.query(tablesQuery);

        while (resultSet.next()) {
            tables.add(new Table(resultSet.getString("table_name")));
        }

        databaseConnection.closeStatement();
    }

    /**
     * Queries and organizes all the columns metadata needed for the application
     * @throws SQLException When the columns metadata couldn't be queried for some reason
     */
    private void loadAllColumns() throws SQLException {
        for (final Table table : tables) {
            loadTableColumns(table);
        }
    }

    /**
     * Queries and organizes all the table's columns metadata needed for the application
     * @param table The Table whose columns will be queried
     * @throws SQLException When the table's columns metadata couldn't be queried for some reason
     */
    private void loadTableColumns(Table table) throws SQLException {
        ApplicationLogger.info("Loading columns metadata for table " + table);

        final String columnsQuery = "SELECT column_name " +
                                    "FROM   " + schema + "." + PropertiesHelper.getColumnsView() + " " +
                                    "WHERE  UPPER(table_schema) = UPPER('" + schema + "') AND " +
                                    "       UPPER(table_name)   = UPPER('" + table.getName() + "') " +
                                    "ORDER BY table_name";

        resultSet = databaseConnection.query(columnsQuery);

        while (resultSet.next()) {
            columns.add(new Column(table, resultSet.getString("column_name")));
        }

        databaseConnection.closeStatement();
    }

    /**
     * Queries and organizes all the constraints metadata needed for the application
     * @throws SQLException When the constraints metadata couldn't be queried for some reason
     */
    private void loadAllConstraints() throws SQLException {
        for (final Table table : tables) {
            loadTableConstraints(table);
        }
    }

    /**
     * Queries and organizes all the table's constraints metadata needed for the application
     * @param table The Table whose constraints will be queried
     * @throws SQLException When the table's constraints metadata couldn't be queried for some reason
     */
    private void loadTableConstraints(Table table) throws SQLException {
        ApplicationLogger.info("Loading constraints metadata for table " + table);

        final String constraintsQuery = "SELECT constraint_name, " +
                                        "       constraint_type, " +
                                        "       table_name, " +
                                        "       column_name, " +
                                        "       referenced_table_name, " +
                                        "       referenced_column_name " +
                                        "FROM   " + schema + "." + PropertiesHelper.getConstraintsView() + " " +
                                        "WHERE  UPPER(table_schema) = UPPER('" + schema + "') AND " +
                                        "       UPPER(table_name)   = UPPER('" + table.getName() + "') " +
                                        "ORDER BY table_name, constraint_type";

        resultSet = databaseConnection.query(constraintsQuery);

        while (resultSet.next()) {
            final Column column = findColumnByTableAndColumnNames(table.getName(), resultSet.getString("column_name"));
            final ConstraintType constraintType = ConstraintType.getConstraintTypeByName(resultSet.getString("constraint_type"));
            final Table referencedTable = constraintType.isForeignKey() ? findTableByTableName(resultSet.getString("referenced_table_name")) : null;
            final Column referencedColumn = constraintType.isForeignKey() ? findColumnByTableAndColumnNames(resultSet.getString("referenced_table_name"), resultSet.getString("referenced_column_name")) : null;

            final Constraint constraint =
                new Constraint(resultSet.getString("constraint_name"),
                               table,
                               column,
                               referencedTable,
                               referencedColumn,
                               constraintType);

            constraints.add(constraint);
        }

        databaseConnection.closeStatement();
    }

    /**
     * Finds all the tables which are exclusively used for a "many to many"
     * relationship and mark them as so
     * @throws SQLException When the tables metadata couldn't be queried for some reason
     */
    private void loadRelationshipTables() throws SQLException {
        final String tablesQuery = "SELECT c1.table_name " +
                                   "FROM   " + schema + "." + PropertiesHelper.getConstraintsView() + " c1 " +
                                   "WHERE  c1.constraint_type = 'PRIMARY_KEY' AND " +
                                   "       UPPER(c1.table_schema) = UPPER('" + schema + "') AND " +
                                   "       EXISTS (SELECT 1 " +
                                   "               FROM   " + schema + "." + PropertiesHelper.getConstraintsView() + " c2 " +
                                   "               WHERE  c2.constraint_type = 'FOREIGN_KEY'   AND " +
                                   "                      c2.table_schema    = c1.table_schema AND " +
                                   "                      c2.table_name      = c1.table_name   AND " +
                                   "                      c2.column_name     = c1.column_name) " +
                                   "GROUP BY c1.table_name " +
                                   "HAVING count(*) = 2";

        resultSet = databaseConnection.query(tablesQuery);

        while (resultSet.next()) {
            final Table relationshipTable = findTableByTableName(resultSet.getString("table_name"));
            relationshipTable.setRelationshipTable(true);

            final List<Constraint> foreignKeys = findForeignKeysByRelationshipTableName(relationshipTable.getName());
            foreignKeys.get(0).setTable(foreignKeys.get(1).getReferencedTable());
        }
    }

    /**
     * Finds a Table amongst all the loaded ones by its name
     * @param tableName The name of the Table which will be searched
     * @return The Table found (null if not found)
     */
    private Table findTableByTableName(String tableName) {
        return tables.stream()
                     .filter(t -> t.getName().equals(tableName))
                     .findFirst()
                     .get();
    }

    /**
     * Finds a Column amongst all the loaded ones by its name and its Table's name
     * @param tableName The name of the Table which will be used in the search
     * @param columnName The name of the Column which will be used in the search
     * @return The Column found (null if not found)
     */
    private Column findColumnByTableAndColumnNames(String tableName, String columnName) {
        return columns.stream()
                      .filter(c -> c.getTable().getName().equals(tableName) &&
                                   c.getName().equals(columnName))
                      .findFirst()
                      .get();
    }

    /**
     * Finds all the constraints related to a "many to many" relationship table
     * @param relationshipTableName The name of the "many to many" relationship table
     * @return All the constraints found
     */
    private List<Constraint> findForeignKeysByRelationshipTableName(String relationshipTableName) {
        return constraints.stream()
                          .filter(c -> c.getTable().getName().equals(relationshipTableName) &&
                                       c.getType().equals(ConstraintType.FOREIGN_KEY) &&
                                       constraints.stream().anyMatch(x -> x.getTable().equals(c.getTable()) &&
                                                                          x.getColumn().equals(c.getColumn()) &&
                                                                          x.getType().equals(ConstraintType.PRIMARY_KEY)))
                          .collect(Collectors.toList());
    }

    /**
     * Simple method for printing all the loaded metadata in Console
     */
    public void printLoadedData() {
        System.out.println("Tables: ");
        tables.forEach(t -> System.out.println(t));

        System.out.println();
        System.out.println("Columns: ");
        columns.forEach(c -> System.out.println(c));

        System.out.println();
        System.out.println("Constraints: ");
        constraints.forEach(c -> System.out.println(c));
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
