package com.uem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.uem.dbconfig.DatabaseType;

/**
 * Utility class which provides the methods for querying the values
 * of the properties in the application.properties file
 *
 * @author zessin
 */
public class PropertiesHelper {
    private static final String PROPERTIES_DIR_NAME    = ".rtg";
    private static final String PROPERTIES_FILE_NAME   = "application.properties";
    private static final String PROP_DATABASE_TYPE     = "database_type";
    private static final String PROP_DATABASE_URL      = "database_url";
    private static final String PROP_DATABASE_SCHEMA   = "database_schema";
    private static final String PROP_DATABASE_USERNAME = "database_username";
    private static final String PROP_DATABASE_PASSWORD = "database_password";
    private static final String PROP_TABLES_VIEW       = "tables_view";
    private static final String PROP_COLUMNS_VIEW      = "columns_view";
    private static final String PROP_CONSTRAINTS_VIEW  = "constraints_view";
    private static final String PROP_OUTPUT_PATH       = "output_path";
    private static final String PROP_DOT_PATH          = "dot_path";

    /**
     * Finds the property which represents the database type
     * @return The value of the property found
     */
    public static DatabaseType getDatabaseType() {
        return DatabaseType.getDatabaseTypeByProperty(getPropertyValue(PROP_DATABASE_TYPE));
    }

    /**
     * Finds the property which represents the database URL
     * @return The value of the property found
     */
    public static String getDatabaseUrl() {
        return getPropertyValue(PROP_DATABASE_URL);
    }

    /**
     * Finds the property which represents the database schema
     * @return The value of the property found
     */
    public static String getDatabaseSchema() {
        return getPropertyValue(PROP_DATABASE_SCHEMA);
    }

    /**
     * Finds the property which represents the database username
     * @return The value of the property found
     */
    public static String getDatabaseUsername() {
        return getPropertyValue(PROP_DATABASE_USERNAME);
    }

    /**
     * Finds the property which represents the database password
     * @return The value of the property found
     */
    public static String getDatabasePassword() {
        return getPropertyValue(PROP_DATABASE_PASSWORD);
    }

    /**
     * Finds the property which represents the name of the table's view
     * @return The value of the property found
     */
    public static String getTablesView() {
        return getPropertyValue(PROP_TABLES_VIEW);
    }

    /**
     * Finds the property which represents the name of the column's view
     * @return The value of the property found
     */
    public static String getColumnsView() {
        return getPropertyValue(PROP_COLUMNS_VIEW);
    }

    /**
     * Finds the property which represents the name of the constraint's view
     * @return The value of the property found
     */
    public static String getConstraintsView() {
        return getPropertyValue(PROP_CONSTRAINTS_VIEW);
    }

    /**
     * Finds the property which represents the output path for the generated graph image
     * @return The value of the property found
     */
    public static String getOutputPath() {
        return getPropertyValue(PROP_OUTPUT_PATH);
    }

    /**
     * Finds the property which represents the dot file path
     * @return The value of the property found
     */
    public static String getDotPath() {
        return getPropertyValue(PROP_DOT_PATH);
    }

    /**
     * Finds the desired property in the application.properties file
     * @param propertyName The name of the property being searched
     * @return The value of the property found
     */
    private static String getPropertyValue(String propertyName) {
        final String propertyValue = getPropertiesFile().getProperty(propertyName);

        if (propertyValue == null || propertyValue.isEmpty()) {
            ApplicationLogger.error(String.format("Property %s is not set in %s file.", propertyName, PROPERTIES_FILE_NAME));
            throw new IllegalStateException();
        }

        return propertyValue;
    }

    /**
     * Provides the Properties file for the application.
     * Creates a default one if none was found
     * @return The Properties file found or created
     */
    private static Properties getPropertiesFile() {
        final File propertiesFile = new File(getPropertiesDirectory(), PROPERTIES_FILE_NAME);
        final Properties properties = new Properties();

        try {
            if (!propertiesFile.exists()) {
                createDefaultPropertiesFile(propertiesFile, properties);
            }

            properties.load(new FileInputStream(propertiesFile));
        } catch (final IOException ex) {
            ApplicationLogger.error(String.format("Could not load or create %s file.", PROPERTIES_FILE_NAME));
            throw new IllegalStateException(ex);
        }

        return properties;
    }

    /**
     * Provides the directory in which the application.properties file shall be located
     * @return The File representing the correct directory
     */
    private static File getPropertiesDirectory() {
        final String userHome = System.getProperty("user.home");

        if (userHome == null) {
            ApplicationLogger.error("Could not find user home directory.");
            throw new IllegalStateException();
        }

        final File home = new File(userHome);
        final File propertiesDirectory = new File(home, PROPERTIES_DIR_NAME);

        if(!propertiesDirectory.exists()) {
            if(!propertiesDirectory.mkdir()) {
                ApplicationLogger.error(String.format("Could not create directory for properties file: %s", propertiesDirectory.getAbsolutePath()));
                throw new IllegalStateException();
            }
        }

        return propertiesDirectory;
    }

    /**
     * Creates a default application.properties file
     * @param propertiesFile The File which shall be created
     * @param properties The Properties which shall be put in the File created
     * @throws IOException When the File couldn't be created for some reason
     */
    private static void createDefaultPropertiesFile(File propertiesFile, Properties properties) throws IOException {
        FileOutputStream newPropertiesFile = null;

        try {
            newPropertiesFile = new FileOutputStream(propertiesFile);

            properties.setProperty(PROP_DATABASE_TYPE, "");
            properties.setProperty(PROP_DATABASE_URL, "");
            properties.setProperty(PROP_DATABASE_SCHEMA, "");
            properties.setProperty(PROP_DATABASE_USERNAME, "");
            properties.setProperty(PROP_DATABASE_PASSWORD, "");
            properties.setProperty(PROP_TABLES_VIEW, "");
            properties.setProperty(PROP_COLUMNS_VIEW, "");
            properties.setProperty(PROP_CONSTRAINTS_VIEW, "");
            properties.setProperty(PROP_OUTPUT_PATH, "");
            properties.setProperty(PROP_DOT_PATH, "");

            properties.store(newPropertiesFile, "Generated properties file");
            ApplicationLogger.warning(String.format("New properties file (%s) was generated with null values. " +
                                                    "You should set the correct values for each property before running the application again.", propertiesFile.getAbsolutePath()));
        } finally {
            if (newPropertiesFile != null) {
                newPropertiesFile.close();
                throw new IllegalStateException();
            }
        }
    }
}
