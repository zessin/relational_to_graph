package com.uem.graphviz;

import java.io.File;

import com.uem.graph.Graph;
import com.uem.util.ApplicationLogger;
import com.uem.util.PropertiesHelper;

/**
 * This class contains the logic responsible for generating the code
 * that represents the Graph model, which will be sent to Graphviz
 *
 * @author zessin
 */
public class GraphvizCodeGenerator {
    final Graph graph;
    final Graphviz gv;

    /**
     * Initializes the class with an existing Graph model
     * @param graph The graph for whom the code will be generated
     */
    public GraphvizCodeGenerator(Graph graph) {
        super();
        this.graph = graph;
        gv = new Graphviz();
    }

    /**
     * Generates the Graphviz code itself
     */
    public void generateCode() {
        ApplicationLogger.info("Generating Graphviz code");

        gv.addln(gv.startGraph());
        gv.addln("  nodesep=1;");
        gv.addln("  node[style=filled];");
        gv.addln();
        gv.addln(printPositiveDegreeVertices());
        gv.addln(printNeutralDegreeVertices());
        gv.addln(gv.endGraph());

        ApplicationLogger.info("Code generated");
    }

    /**
     * After the code is generated, this method calls the Graphviz API
     * for generating the image of the graph
     */
    public void writeGraphFile() {
        final String outputPath = PropertiesHelper.getOutputPath();
        final String fileName = "graph";
        final String type = "svg";
        final String representationType= "dot";
        final String absoluteFileName = String.format("%s%s%s.%s", outputPath, File.separator, fileName, type);
        final File outputFile = new File(absoluteFileName);

        gv.decreaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();

        ApplicationLogger.info("Writing graph to file " + absoluteFileName);

        final Integer returnValue = gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, representationType), outputFile);

        if (returnValue != 1) {
            ApplicationLogger.error("Error when writing graph to file " + absoluteFileName);
            throw new IllegalStateException();
        }
    }

    /**
     * Searches for all the vertices with positive degree and return them
     * @return A string with all the positive degree vertices (empty string if no vertices found)
     */
    private String printPositiveDegreeVertices() {
        final StringBuilder sb = new StringBuilder();

        graph.getVertices()
             .stream()
             .filter(v -> v.isDegreePositive())
             .forEach(v -> graph.getAdjacencyList()
                                .get(v)
                                .forEach(w -> sb.append(String.format("  %s -> %s;%n", v, w))));

        return sb.toString();
    }

    /**
     * Searches for all the vertices with neutral degree and return them
     * @return A string with all the neutral degree vertices (empty string if no vertices found)
     */
    private String printNeutralDegreeVertices() {
        final StringBuilder sb = new StringBuilder();

        graph.getVertices()
             .stream()
             .filter(v -> !v.isDegreePositive())
             .forEach(v -> sb.append(String.format("  %s;%n", v)));

        return sb.toString();
    }
}
