package com.uem.dbconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.uem.util.ApplicationLogger;
import com.uem.util.PropertiesHelper;

/**
 * Provides all the methods needed to connect and query the relational database
 *
 * @author zessin
 */
public final class DatabaseConnection {
    public static DatabaseConnection databaseConnection;

    private final Connection connection;
    private Statement statement;
    private static final String MYSQL_DRIVER      = "com.mysql.jdbc.Driver";
    private static final String ORACLE_DRIVER     = "oracle.jdbc.driver.OracleDriver";
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    /**
     * Initializes the correct JDBC Driver according to the RDBMS
     * and starts the connection with the database
     */
    private DatabaseConnection() {
        try {
            switch (PropertiesHelper.getDatabaseType()) {
                case MYSQL:
                    loadMysqlDriver();
                    break;
                case ORACLE:
                    loadOracleDriver();
                    break;
                case POSTGRESQL:
                    loadPostgresqlDriver();
                    break;
            }

            connection = DriverManager.getConnection(PropertiesHelper.getDatabaseUrl(),
                                                     PropertiesHelper.getDatabaseUsername(),
                                                     PropertiesHelper.getDatabasePassword());
        } catch (final SQLException ex) {
            ApplicationLogger.error(ex.getMessage());
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Singleton method that provides the active instance of this class
     * @return The active instance of the DatabaseConnection class
     */
    public static synchronized DatabaseConnection getConnection() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }

        return databaseConnection;
    }

    /**
     * Executes a query in the database
     * @param query The query to be executed
     * @return The ResultSet obtained with the query (null if nothing was found)
     * @throws SQLException When the query couldn't be executed for some reason
     */
    public ResultSet query(String query) throws SQLException {
        statement = databaseConnection.connection.createStatement();
        final ResultSet result = statement.executeQuery(query);

        return result;
    }

    /**
     * Closes the statement used for previous queries
     * @throws SQLException When the statement couldn't be closed for some reason
     */
    public void closeStatement() throws SQLException {
    	statement.close();
    }

    /**
     * Initializes the driver for MySQL RDMBS
     */
    private void loadMysqlDriver() {
        try {
			Class.forName(MYSQL_DRIVER).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		}
    }

    /**
     * Initializes the driver for Oracle RDMBS
     */
    private void loadOracleDriver() {
        try {
			Class.forName(ORACLE_DRIVER).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		}
    }

    /**
     * Initializes the driver for PostgreSQL RDMBS
     */
    private void loadPostgresqlDriver() {
        try {
            Class.forName(POSTGRESQL_DRIVER).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
