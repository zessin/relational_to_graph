package com.uem.graph;

/**
 * Represents a pair of vertices in the Graph.
 * Used in the Graph class to easily find the edges between two vertices
 *
 * @author zessin
 */
public class Pair {
    private Vertex v1;
    private Vertex v2;

    /**
     * Initializes the Pair with two vertices
     * @param v1 The source Vertex
     * @param v2 The destination Vertex
     */
    public Pair(Vertex v1, Vertex v2) {
        super();
        this.v1 = v1;
        this.v2 = v2;
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
        return String.format("(%s, %s)", v1, v2);
    }
}
