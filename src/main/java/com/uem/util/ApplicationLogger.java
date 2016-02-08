package com.uem.util;

import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class which provides the logging methods for the application
 *
 * @author zessin
 */
public class ApplicationLogger {
    public static final String LOG_FILE_NAME = "results.log";

    private static ApplicationLogger applicationLogger;
    private Logger logger;

    /**
     * Initializes the class with the desired format for the log file
     */
    private ApplicationLogger() {
        try {
            final FileHandler handler = new FileHandler(LOG_FILE_NAME);
            final SimpleFormatter simpleFormatter = new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    return String.format("[%s] [%s]: %s%n", new Date(), record.getLevel().getLocalizedName(), record.getMessage());
                }
            };
            handler.setFormatter(simpleFormatter);

            logger = Logger.getLogger(ApplicationLogger.class.getName());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Logs a message with an INFO Level
     * @param message The message to be logged
     */
    public static void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with a WARNING Level
     * @param message The message to be logged
     */
    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    /**
     * Logs a message with a SEVERE Level
     * @param message The message to be logged
     */
    public static void error(String message) {
        log(Level.SEVERE, message);
    }

    /**
     * Singleton method which logs the desired message
     * using the active instance of the ApplicationLogger class
     * @param level Level of the log message
     * @param message The log message itself
     */
    private static void log(Level level, String message) {
        if (applicationLogger == null) {
            applicationLogger = new ApplicationLogger();
        }

        applicationLogger.logger.log(level, message);
    }
}
