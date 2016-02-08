package com.uem.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uem.dbstructure.Constraint;
import com.uem.dbstructure.ConstraintType;
import com.uem.dbstructure.DatabaseInfo;
import com.uem.dbstructure.Table;
import com.uem.util.ApplicationLogger;

/**
 * Represents the graph model for the relational model obtained with the metadata from the RDBMS
 *
 * @author zessin
 */
public class Graph {
    private List<Vertex> vertices;
    private List<Edge> edges;
    private Map<Vertex, List<Vertex>> adjacencyList;
    private Map<Pair, Edge> edgesFromVertices;
    private boolean directed;

    /**
     * Initializes the Graph telling if it's a directed one or not
     * @param directed Tells whether the Graph is directed or not
     */
    public Graph(boolean directed) {
        super();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyList = new HashMap<>();
        edgesFromVertices = new HashMap<>();
        this.directed = directed;
    }

    /**
     * Initializes the Graph with some existing relational database information,
     * previously obtained and organized, and telling if it's a directed one or not
     * @param databaseInfo The relational database information
     * @param directed Tells whether the Graph is directed or not
     */
    public Graph(DatabaseInfo databaseInfo, boolean directed) {
        super();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyList = new HashMap<>();
        edgesFromVertices = new HashMap<>();
        this.directed = directed;

        generateGraphFromDatabaseInfo(databaseInfo);
    }

    /**
     * Generates a graph model based on the database information previously obtained and organized
     * @param databaseInfo The relational database information
     */
    private void generateGraphFromDatabaseInfo(DatabaseInfo databaseInfo) {
        ApplicationLogger.info("Generating graph structure");

        databaseInfo.getTables()
                    .forEach(t -> addVertexFromTable(t));

        databaseInfo.getConstraints()
                    .stream()
                    .filter(c -> c.getType().equals(ConstraintType.FOREIGN_KEY))
                    .forEach(c -> addEdgeFromForeignKey(c));
    }

    /**
     * Adds a new Vertex based on an existing Table
     * @param table The Table which will become a Vertex in the Graph
     */
    private void addVertexFromTable(Table table) {
        if (!table.isRelationshipTable()) {
            addVertex(new Vertex(table));
        }
    }

    /**
     * Adds a new Edge based on an existing foreign key Constraint
     * @param foreignKey The foreign key Constraint which will become an Edge in the Graph
     */
    private void addEdgeFromForeignKey(Constraint foreignKey) {
        if (!foreignKey.getTable().isRelationshipTable()) {
            final Vertex v1 = getVertexByName(foreignKey.getTable().getName());
            final Vertex v2 = getVertexByName(foreignKey.getReferencedTable().getName());

            addEdge(new Edge(String.format("%s-%s", v1.getName(), v2.getName()), v1, v2));
        }
    }

    /**
     * Adds a Vertex in the graph
     * @param vertex The Vertex to be added in the Graph
     */
    private void addVertex(Vertex vertex) {
        vertices.add(vertex);
        adjacencyList.put(vertex, new ArrayList<>());
    }

    /**
     * Adds an Edge in the graph
     * @param edge The Edge to be added in the Graph
     */
    private void addEdge(Edge edge) {
        edges.add(edge);

        adjacencyList.get(edge.getV1()).add(edge.getV2());
        edgesFromVertices.put(new Pair(edge.getV1(), edge.getV2()), edge);
        edge.getV1().increaseDegree();

        if (!directed) {
            adjacencyList.get(edge.getV2()).add(edge.getV1());
            edgesFromVertices.put(new Pair(edge.getV2(), edge.getV1()), edge);
            edge.getV2().increaseDegree();
        }
    }

    /**
     * Finds a Vertex by its name
     * @param vertexName The name of the Vertex to be searched
     * @return The Vertex found (null if not found)
     */
    private Vertex getVertexByName(String vertexName) {
        return vertices.stream()
                       .filter(v -> v.getName().equals(vertexName))
                       .findFirst()
                       .get();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Map<Vertex, List<Vertex>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<Vertex, List<Vertex>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Map<Pair, Edge> getEdgesFromVertices() {
        return edgesFromVertices;
    }

    public void setEdgesFromVertices(Map<Pair, Edge> edgesFromVertices) {
        this.edgesFromVertices = edgesFromVertices;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }
}
