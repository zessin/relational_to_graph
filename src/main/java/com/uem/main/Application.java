package com.uem.main;

import com.uem.util.ApplicationLogger;

/**
 * The Main class of the application. Responsible for initializing all the process.
 *
 * @author zessin
 */
public class Application {
    public static void main(String[] args) {
        try {
            System.out.println("Starting the application...");
            ApplicationLogger.info("Application started");

            new RelationalToGraph().execute();

            System.out.println(String.format("Application ended. Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.info("Application ended. Check above lines for detailed information.");
            System.exit(0);
        } catch (final IllegalStateException ex) {
            System.out.println(String.format("Failure! Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.error("Application ended with errors. Check above lines for detailed information.");
            System.exit(1);
        }
    }
}
