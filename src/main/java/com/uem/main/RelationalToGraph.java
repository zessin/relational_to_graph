package com.uem.main;

import java.sql.SQLException;

import com.uem.dbstructure.DatabaseInfo;
import com.uem.graph.Graph;
import com.uem.graphviz.GraphvizCodeGenerator;
import com.uem.util.ApplicationLogger;

/**
 * Responsible for calling all the necessary methods in an organized way,
 * printing some useful information in the console
 *
 * @author zessin
 */
public class RelationalToGraph {
    private final DatabaseInfo databaseInfo;

    /**
     * Initializes the class
     */
    public RelationalToGraph() {
        super();
        databaseInfo = new DatabaseInfo();
    }

    /**
     * Executes all the necessary methods for the application to work
     */
    public void execute() {
        try {
            System.out.println("Loading database information...");
            databaseInfo.loadDatabaseInformation();

            System.out.println("Generating graph for database...");
            final Graph graph = new Graph(databaseInfo, true);
            final GraphvizCodeGenerator graphvizCodeGenerator = new GraphvizCodeGenerator(graph);
            graphvizCodeGenerator.generateCode();

            System.out.println("Writing graph to file...");
            graphvizCodeGenerator.writeGraphFile();
        } catch (final SQLException sqlEx) {
            ApplicationLogger.error("SQLException: " + sqlEx.getMessage());
            ApplicationLogger.error("SQLState: " + sqlEx.getSQLState());
            ApplicationLogger.error("VendorError: " + sqlEx.getErrorCode());
        } catch (final Exception ex) {
            ApplicationLogger.error(ex.getMessage());
        } finally {
            if (databaseInfo.getResultSet() != null) {
                try {
                    databaseInfo.getResultSet().close();
                } catch (final SQLException sqlEx) { }

                databaseInfo.setResultSet(null);
            }
        }
    }
}
