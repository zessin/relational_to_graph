package com.uem.dbconfig;

/**
 * Provides all the RDBMSs which are supported by the application
 *
 * @author zessin
 */
public enum DatabaseType {
    MYSQL, ORACLE, POSTGRESQL;

    /**
     * Returns the correct DatabaseType according to the property in the properties file
     * @param databaseTypeProperty The value of the property in the file
     * @return The correct DatabaseType for the property (null if not found)
     */
    public static DatabaseType getDatabaseTypeByProperty(String databaseTypeProperty) {
        switch (databaseTypeProperty.toUpperCase()) {
            case "MYSQL":
                return MYSQL;
            case "ORACLE":
                return ORACLE;
            case "POSTGRESQL":
                return POSTGRESQL;
            default:
                return null;
        }
    }
}
