package com.uem.graph;

/**
 * Represents and Edge in the Graph
 *
 * @author zessin
 */
public class Edge {
    private String name;
    private Vertex v1;
    private Vertex v2;

    /**
     * Initializes the Edge with a name and two vertices
     * @param name The Edge's name
     * @param v1 The source Vertex
     * @param v2 The destination Vertex
     */
    public Edge(String name, Vertex v1, Vertex v2) {
        super();
        this.name = name;
        this.v1 = v1;
        this.v2 = v2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Edge)) {
            return false;
        }

        final Edge otherEdge = (Edge) other;
        return this.getV1().equals(otherEdge.getV1()) &&
               this.getV2().equals(otherEdge.getV2());
    }

    @Override
    public String toString() {
        return String.format("[%s]->[%s]", v1, v2);
    }
}
